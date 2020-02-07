package sky.epaperscreenupdater.page;

import java.util.Date;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementType;
import sky.netatmo.Measurer;

public class StandAloneMeasure implements Measure
{
    private final Date date;
    private final MeasurementType measurementType;
    private final double value;

    public StandAloneMeasure(Date date,MeasurementType measurementType,double value)
    {
        this.date=date;
        this.measurementType=measurementType;
        this.value=value;
    }

    public Measurer getMeasurer()
    {
        return null;
    }

    public Date getDate()
    {
        return date;
    }

    public MeasurementType getMeasurementType()
    {
        return measurementType;
    }

    public double getValue()
    {
        return value;
    }

    public int compareTo(Measure o)
    {
        return Long.compare(date.getTime(),o.getDate().getTime());
    }
}
