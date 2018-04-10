package sky.epaperscreenupdater;

import com.pi4j.io.gpio.PinState;

public class RotaryEvent
{
    private final RotaryClock rotaryClock;
    private final PinState pinState;
    private final long time;

    public RotaryEvent(RotaryClock rotaryClock,PinState pinState,long time)
    {
        this.rotaryClock=rotaryClock;
        this.pinState=pinState;
        this.time=time;
    }

    public RotaryClock getRotaryClock()
    {
        return rotaryClock;
    }

    public PinState getPinState()
    {
        return pinState;
    }

    public long getTime()
    {
        return time;
    }

    public String toString(RotaryEvent firstRotaryEvent)
    {
        return rotaryClock.name()+"/"+pinState.getName()+"/"+(time-firstRotaryEvent.getTime());
    }
}
