package sky.epaperscreenupdater;

import java.util.GregorianCalendar;

public class Birthday
{
    private final GregorianCalendar calendar;
    private final int birthYear;

    public Birthday(GregorianCalendar calendar,int birthYear)
    {
        this.calendar=calendar;
        this.birthYear=birthYear;
    }

    public GregorianCalendar getCalendar()
    {
        return calendar;
    }

    public int getBirthYear()
    {
        return birthYear;
    }
}
