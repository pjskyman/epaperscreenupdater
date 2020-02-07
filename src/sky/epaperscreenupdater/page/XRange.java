package sky.epaperscreenupdater.page;

public class XRange
{
    private final long min;
    private final long max;

    public XRange(long min,long max)
    {
        this.min=min;
        this.max=max;
    }

    public long getMin()
    {
        return min;
    }

    public long getMax()
    {
        return max;
    }

    public long getAmplitude()
    {
        return max-min;
    }
}
