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

    public YRange(YRange yRange1,YRange yRange2)
    {
        min=Math.min(yRange1.getMin(),yRange2.getMin());
        max=Math.max(yRange1.getMax(),yRange2.getMax());
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
