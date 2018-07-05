package sky.epaperscreenupdater;

public class YRange
{
    private final double min;
    private final double max;

    public YRange(double min,double max)
    {
        this.min=min;
        this.max=max;
    }

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
    }

    public double getAmplitude()
    {
        return max-min;
    }
}
