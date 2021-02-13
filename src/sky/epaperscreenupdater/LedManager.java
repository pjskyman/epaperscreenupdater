package sky.epaperscreenupdater;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import sky.housecommon.Logger;
import sky.program.Duration;

public class LedManager
{
    private static final GpioPinDigitalOutput GREEN_LED;
    private static final GpioPinDigitalOutput RED_LED;

    static
    {
        if(System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            GREEN_LED=null;
            RED_LED=null;
        }
        else
        {
            GpioPinDigitalOutput greenLed=null;
            try
            {
                for(int i=0;i<10;i++)
                    try
                    {
                        greenLed=GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_04,PinState.LOW);
                        Logger.LOGGER.info("GPIO pin 4 opened");
                        break;
                    }
                    catch(RuntimeException e)
                    {
                        Logger.LOGGER.warn("Unable to open the GPIO pin 4");
                        Thread.sleep(Duration.of(200).millisecond());
                    }
            }
            catch(InterruptedException e)
            {
            }
            if(greenLed==null)
            {
                Logger.LOGGER.error("Unable to open the GPIO pin 4 after 10 attempts");
                System.exit(1);
            }
            GREEN_LED=greenLed;
            GREEN_LED.setShutdownOptions(Boolean.TRUE,PinState.LOW);
            GpioPinDigitalOutput redLed=null;
            try
            {
                for(int i=0;i<10;i++)
                    try
                    {
                        redLed=GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_03,PinState.LOW);
                        Logger.LOGGER.info("GPIO pin 3 opened");
                        break;
                    }
                    catch(RuntimeException e)
                    {
                        Logger.LOGGER.warn("Unable to open the GPIO pin 3");
                        Thread.sleep(Duration.of(200).millisecond());
                    }
            }
            catch(InterruptedException e)
            {
            }
            if(redLed==null)
            {
                Logger.LOGGER.error("Unable to open the GPIO pin 3 after 10 attempts");
                System.exit(1);
            }
            RED_LED=redLed;
            RED_LED.setShutdownOptions(Boolean.TRUE,PinState.LOW);
            Logger.LOGGER.info("GPIO pins successfully initialized");
        }
    }

    private LedManager()
    {
    }

    public static void setGreenLedOff()
    {
        GREEN_LED.low();
    }

    public static void setGreenLedOn()
    {
        GREEN_LED.high();
    }

    public static void setRedLedOff()
    {
        RED_LED.low();
    }

    public static void setRedLedOn()
    {
        RED_LED.high();
    }
}
