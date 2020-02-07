package sky.epaperscreenupdater;

public class OffPeakHourPeriodEfficiency
{
    private final double consumptionEfficiency;
    private final double priceEfficiency;

    public OffPeakHourPeriodEfficiency(double consumptionEfficiency,double priceEfficiency)
    {
        this.consumptionEfficiency=consumptionEfficiency;
        this.priceEfficiency=priceEfficiency;
    }

    public double getConsumptionEfficiency()
    {
        return consumptionEfficiency;
    }

    public double getPriceEfficiency()
    {
        return priceEfficiency;
    }
}
