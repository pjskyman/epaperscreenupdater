package sky.epaperscreenupdater;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.ArrayList;
import java.util.List;
import sky.housecommon.Logger;

public class RotaryEncoderManager
{
    private static final GpioPinDigitalInput CLOCK_1;
    private static final GpioPinDigitalInput CLOCK_2;
    private static final GpioPinDigitalInput CLICK;
    private static final RotaryEventQueue ROTARY_EVENT_QUEUE;
    private static final List<SwitchListener> SWITCH_LISTENERS;

    static
    {
        GpioPinDigitalInput clock1=null;
        try
        {
            clock1=GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.GPIO_21);
            clock1.addListener(new GpioPinListenerDigital()
            {
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
                {
                    ROTARY_EVENT_QUEUE.add(new RotaryEvent(RotaryClock.CLOCK_1,event.getState(),System.currentTimeMillis()));
                }
            });
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unable to get clock 1 pin ("+e.toString()+")");
            e.printStackTrace();
            System.exit(1);
        }
        CLOCK_1=clock1;
        GpioPinDigitalInput clock2=null;
        try
        {
            clock2=GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.GPIO_22);
            clock2.addListener(new GpioPinListenerDigital()
            {
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
                {
                    ROTARY_EVENT_QUEUE.add(new RotaryEvent(RotaryClock.CLOCK_2,event.getState(),System.currentTimeMillis()));
                }
            });
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unable to get clock 2 pin ("+e.toString()+")");
            e.printStackTrace();
            System.exit(1);
        }
        CLOCK_2=clock2;
        GpioPinDigitalInput click=null;
        try
        {
            click=GpioFactory.getInstance().provisionDigitalInputPin(RaspiPin.GPIO_02);
            click.addListener(new GpioPinListenerDigital()
            {
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
                {
//                    System.out.println("click "+event.getState()+" "+System.currentTimeMillis());
                    if(event.getState()==PinState.HIGH)
                        SWITCH_LISTENERS.forEach(SwitchListener::switched);
                }
            });
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unable to get click pin ("+e.toString()+")");
            e.printStackTrace();
            System.exit(1);
        }
        CLICK=click;
        ROTARY_EVENT_QUEUE=new RotaryEventQueue();
        SWITCH_LISTENERS=new ArrayList<>();
    }

    public static void addRotationListener(RotationListener rotationListener)
    {
        ROTARY_EVENT_QUEUE.addRotationListener(rotationListener);
    }

    public static void addSwitchListener(SwitchListener switchListener)
    {
        SWITCH_LISTENERS.add(switchListener);
    }
}
