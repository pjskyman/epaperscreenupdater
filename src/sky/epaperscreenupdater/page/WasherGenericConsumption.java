package sky.epaperscreenupdater.page;

import sky.housecommon.InstantaneousConsumption;

public class WasherGenericConsumption
{
    private final long time;
    private final int consumption;

    public WasherGenericConsumption(InstantaneousConsumption instantaneousConsumption)
    {
        time=instantaneousConsumption.getTime();
        consumption=instantaneousConsumption.getConsumer7Consumption();
    }

    public WasherGenericConsumption(long time,int consumption)
    {
        this.time=time;
        this.consumption=consumption;
    }

    public long getTime()
    {
        return time;
    }

    public int getConsumption()
    {
        return consumption;
    }
}
