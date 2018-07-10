package sky.epaperscreenupdater;

import java.util.Objects;

public class MeasureDatabaseKey
{
    private final long time;
    private final String measureKind;

    public MeasureDatabaseKey(long time,String measureKind)
    {
        this.time=time;
        this.measureKind=measureKind;
    }

    public long getTime()
    {
        return time;
    }

    public String getMeasureKind()
    {
        return measureKind;
    }

    @Override
    public int hashCode()
    {
        int hash=7;
        hash=89*hash+(int)(this.time^(this.time>>>32));
        hash=89*hash+Objects.hashCode(this.measureKind);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this==obj)
            return true;
        if(obj==null)
            return false;
        if(getClass()!=obj.getClass())
            return false;
        final MeasureDatabaseKey other=(MeasureDatabaseKey)obj;
        if(this.time!=other.time)
            return false;
        if(!Objects.equals(this.measureKind,other.measureKind))
            return false;
        return true;
    }
}
