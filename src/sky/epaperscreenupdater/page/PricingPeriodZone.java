package sky.epaperscreenupdater.page;

public class PricingPeriodZone
{
    private final int startHour;
    private final int startMinute;
    private final int stopHour;
    private final int stopMinute;
    private final PricingPeriod pricingPeriod;

    public PricingPeriodZone(int startHour,int startMinute,int stopHour,int stopMinute,PricingPeriod pricingPeriod)
    {
        this.startHour=startHour;
        this.startMinute=startMinute;
        this.stopHour=stopHour;
        this.stopMinute=stopMinute;
        this.pricingPeriod=pricingPeriod;
    }

    public int getStartHour()
    {
        return startHour;
    }

    public int getStartMinute()
    {
        return startMinute;
    }

    public int getStopHour()
    {
        return stopHour;
    }

    public int getStopMinute()
    {
        return stopMinute;
    }

    public boolean isTimeInside(int hour,int minute)
    {
        int time=hour*60+minute;
        return time>=startHour*60+startMinute&&time<=stopHour*60+stopMinute;
    }

    public PricingPeriod getPricingPeriod()
    {
        return pricingPeriod;
    }
}
