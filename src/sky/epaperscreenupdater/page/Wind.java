package sky.epaperscreenupdater.page;

import java.util.Date;

public class Wind
{
    private final Date time;
    private final double wind;
    private final double windAngle;
    private final double gust;
    private final double gustAngle;
    private final double x;
    private final double windY;
    private final double gustY;

    public Wind(Date time,double wind,double windAngle,double gust,double gustAngle,double x,double windY,double gustY)
    {
        this.time=time;
        this.wind=wind;
        this.windAngle=windAngle;
        this.gust=gust;
        this.gustAngle=gustAngle;
        this.x=x;
        this.windY=windY;
        this.gustY=gustY;
    }

    public Date getTime()
    {
        return time;
    }

    public double getWind()
    {
        return wind;
    }

    public double getWindAngle()
    {
        return windAngle;
    }

    public double getGust()
    {
        return gust;
    }

    public double getGustAngle()
    {
        return gustAngle;
    }

    public double getX()
    {
        return x;
    }

    public double getWindY()
    {
        return windY;
    }

    public double getGustY()
    {
        return gustY;
    }
}
