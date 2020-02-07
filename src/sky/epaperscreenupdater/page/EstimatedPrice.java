package sky.epaperscreenupdater.page;

public class EstimatedPrice
{
    private final int startHour;
    private final int startMinute;
    private final double price;

    public EstimatedPrice(int startHour,int startMinute,double price)
    {
        this.startHour=startHour;
        this.startMinute=startMinute;
        this.price=price;
    }

    public int getStartHour()
    {
        return startHour;
    }

    public int getStartMinute()
    {
        return startMinute;
    }

    public double getPrice()
    {
        return price;
    }
}
