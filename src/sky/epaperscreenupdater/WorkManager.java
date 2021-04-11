package sky.epaperscreenupdater;

import java.util.HashMap;
import java.util.Map;
import sky.epaperscreenupdater.page.Page;
import sky.housecommon.Logger;

public class WorkManager
{
    private static final Map<Page,Long> WORKING_PAGE_MAP=new HashMap<>();
    private static final Object LOCK_OBJECT=new Object();

    private WorkManager()
    {
    }

    public static void notifyPageWorking(Page page)
    {
        synchronized(LOCK_OBJECT)
        {
            WORKING_PAGE_MAP.put(page,Long.valueOf(System.currentTimeMillis()));
            LedManager.setRedLedOn();
        }
    }

    public static void notifyPageWorkEnded(Page page)
    {
        synchronized(LOCK_OBJECT)
        {
            long startTime=WORKING_PAGE_MAP.remove(page).longValue();
            if(page!=null)
                Logger.LOGGER.debug("Work time for page "+page.getName()+": "+(System.currentTimeMillis()-startTime)+" ms");
            else
                Logger.LOGGER.debug("Work time: "+(System.currentTimeMillis()-startTime)+" ms");
            if(WORKING_PAGE_MAP.isEmpty())
                LedManager.setRedLedOff();
            else
                LedManager.setRedLedOn();//on sait jamais, une autre tâche pourrait l'avoir éteinte
        }
    }
}
