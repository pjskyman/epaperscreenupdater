package sky.epaperscreenupdater;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import sky.housecommon.Logger;

public class RotaryEventQueue
{
    private final List<RotationListener> rotationListeners;
    private RotaryEvent previousRotaryEvent;
    private RotaryEvent previousPreviousRotaryEvent;
    private RotaryEvent previousPreviousPreviousRotaryEvent;

    public RotaryEventQueue()
    {
        rotationListeners=new ArrayList<>();
        previousRotaryEvent=null;
        previousPreviousRotaryEvent=null;
        previousPreviousPreviousRotaryEvent=null;
    }

    public RotaryEventQueue addRotationListener(RotationListener rotationListener)
    {
        rotationListeners.add(rotationListener);
        return this;
    }

    public synchronized void add(RotaryEvent currentRotaryEvent)
    {
        if(previousPreviousPreviousRotaryEvent!=null&&previousPreviousRotaryEvent!=null&&previousRotaryEvent!=null)
        {
            int highCount=(int)Stream.of(previousPreviousPreviousRotaryEvent,previousPreviousRotaryEvent,previousRotaryEvent,currentRotaryEvent).filter(event->event.getPinState().isHigh()).count();
            int clock1Count=(int)Stream.of(previousPreviousPreviousRotaryEvent,previousPreviousRotaryEvent,previousRotaryEvent,currentRotaryEvent).filter(event->event.getRotaryClock()==RotaryClock.CLOCK_1).count();
            if(highCount==2&&clock1Count==2)
            {
                long minTime=Stream.of(previousPreviousPreviousRotaryEvent,previousPreviousRotaryEvent,previousRotaryEvent,currentRotaryEvent).min((o1,o2)->Double.compare(o1.getTime(),o2.getTime())).get().getTime();
                double clock1AverageTime=Stream.of(previousPreviousPreviousRotaryEvent,previousPreviousRotaryEvent,previousRotaryEvent,currentRotaryEvent).filter(event->event.getRotaryClock()==RotaryClock.CLOCK_1).mapToInt(value->(int)(value.getTime()-minTime)).average().getAsDouble();
                double clock2AverageTime=Stream.of(previousPreviousPreviousRotaryEvent,previousPreviousRotaryEvent,previousRotaryEvent,currentRotaryEvent).filter(event->event.getRotaryClock()==RotaryClock.CLOCK_2).mapToInt(value->(int)(value.getTime()-minTime)).average().getAsDouble();
                if(clock1AverageTime<clock2AverageTime)
                {
                    Logger.LOGGER.info("Rotation direction="+RotationDirection.CLOCKWISE.name());
                    rotationListeners.forEach(rotationListener->rotationListener.rotated(RotationDirection.CLOCKWISE));
                }
                else
                    if(clock1AverageTime>clock2AverageTime)
                    {
                        Logger.LOGGER.info("Rotation direction="+RotationDirection.COUNTERCLOCKWISE.name());
                        rotationListeners.forEach(rotationListener->rotationListener.rotated(RotationDirection.COUNTERCLOCKWISE));
                    }
            }
            previousPreviousPreviousRotaryEvent=previousPreviousRotaryEvent=previousRotaryEvent=null;
        }
        else
        {
            previousPreviousPreviousRotaryEvent=previousPreviousRotaryEvent;
            previousPreviousRotaryEvent=previousRotaryEvent;
            previousRotaryEvent=currentRotaryEvent;
        }
    }
}
