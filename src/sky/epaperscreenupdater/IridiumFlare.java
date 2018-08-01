package sky.epaperscreenupdater;

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
        double magnitudeIndex=magnitude>-.3d?0d:
                              magnitude<-8.4d?1d:
                              .015532243525986d*magnitude*magnitude+
                              .00990603588329d*magnitude+
                              .01472952107232d-
                              .01315007d;
        magnitudeIndex=Math.max(Math.min(magnitudeIndex,1d),0d);
        double altitudeIndex=altitude<10d?0d:
                             altitude>70d?1d:
                             (altitude-10d)/60d;
        double sunAltitudeIndex=sunAltitude<-20d?1d:
                                sunAltitude>0d?0d:
                                sunAltitude/-20d;
        return Math.sqrt(magnitudeIndex*altitudeIndex*sunAltitudeIndex);
    }
}
