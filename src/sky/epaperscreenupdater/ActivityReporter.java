package sky.epaperscreenupdater;

import java.util.HashMap;
import java.util.Map;
import sky.epaperscreenupdater.page.Page;

public class ActivityReporter
{
    private static final Map<Page,Long> ACTIVE_PAGE_MAP=new HashMap<>();
    private static final Object LOCK_OBJECT=new Object();

    private ActivityReporter()
    {
    }

    public static void notifyActivityOn(Page page)
    {
        synchronized(LOCK_OBJECT)
        {
            ACTIVE_PAGE_MAP.put(page,Long.valueOf(System.currentTimeMillis()));
            LedManager.setRedLedOn();
        }
    }

    public static void notifyActivityOff(Page page)
    {
        synchronized(LOCK_OBJECT)
        {
            Long startTime=ACTIVE_PAGE_MAP.remove(page);
            if(ACTIVE_PAGE_MAP.isEmpty())
                LedManager.setRedLedOff();
        }
    }
}
