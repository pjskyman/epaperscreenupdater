package sky.epaperscreenupdater.page;

import java.util.Date;

public class IridiumFlare
{
    private final Date date;
    private final double magnitude;
    private final int altitude;
    private final int azimuth;
    private final String name;
    private final int centerDistance;
    private final double centerMagnitude;
    private final int sunAltitude;

    public IridiumFlare(Date date,double magnitude,int altitude,int azimuth,String name,int centerDistance,double centerMagnitude,int sunAltitude)
    {
        this.date=date;
        this.magnitude=magnitude;
        this.altitude=altitude;
        this.azimuth=azimuth;
        this.name=name;
        this.centerDistance=centerDistance;
        this.centerMagnitude=centerMagnitude;
        this.sunAltitude=sunAltitude;
    }

    public Date getDate()
    {
        return date;
    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public int getAltitude()
    {
        return altitude;
    }

    public int getAzimuth()
    {
        return azimuth;
    }

    public String getName()
    {
        return name;
    }

    public int getCenterDistance()
    {
        return centerDistance;
    }

    public double getCenterMagnitude()
    {
        return centerMagnitude;
    }

    public int getSunAltitude()
    {
        return sunAltitude;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName()+"{"+"date="+date+", magnitude="+magnitude+", altitude="+altitude+", azimuth="+azimuth+", name="+name+", centerDistance="+centerDistance+", centerMagnitude="+centerMagnitude+", sunAltitude="+sunAltitude+'}';
    }

    public double getPJIndex()
    {
        double magnitudeIndex=Math.min(Math.max(-.09523809524d*magnitude+.2d,0d),1d);
        double altitudeIndex=Math.min(Math.max(.02222222222d*altitude,0d),1d);
        double sunAltitudeIndex=Math.min(Math.max(-.05d*sunAltitude,0d),1d);
        return Math.sqrt(magnitudeIndex*altitudeIndex*sunAltitudeIndex);
    }

    public static void main(String[] args)
    {
        System.out.println(new IridiumFlare(new Date(),-6.5d,45,45,"",0,-8.4d,-17).getPJIndex());
    }
}
