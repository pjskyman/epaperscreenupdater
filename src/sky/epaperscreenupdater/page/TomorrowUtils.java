package sky.epaperscreenupdater.page;

import com.google.gson.JsonObject;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import sky.housecommon.Logger;
import sky.program.Duration;

public class TomorrowUtils
{
    private static long lastTomorrowVerificationTime=0L;
    private static String tomorrow="ND";

    private TomorrowUtils()
    {
    }

    public static synchronized String getTomorrow()
    {
        long now=System.currentTimeMillis();
        if(now-lastTomorrowVerificationTime>Duration.of(9).minutePlus(3).second())
            try
            {
                GregorianCalendar calendar=new GregorianCalendar(Locale.FRANCE);
                calendar.setTimeInMillis(now);
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                if(hour>=6)//on est en journée, après la bascule de jour du matin à 6h00 et avant minuit
                    calendar.setTimeInMillis(now+Duration.of(1).day());//pour avoir demain
                String day=""+calendar.get(Calendar.DAY_OF_MONTH);
                if(day.length()==1)
                    day="0"+day;
                String month=""+(calendar.get(Calendar.MONTH)+1);
                if(month.length()==1)
                    month="0"+month;
                JsonObject tomorrowObject=JsonUtils.getJsonResponse("https://particulier.edf.fr/bin/edf_rc/servlets/ejptemponew?Date_a_remonter="+calendar.get(Calendar.YEAR)+"-"+month+"-"+day+"&TypeAlerte=TEMPO");
                String oldTomorrow=tomorrow;
                if(tomorrowObject!=null)
                {
                    tomorrow=tomorrowObject.getAsJsonObject("JourJ").getAsJsonPrimitive("Tempo").getAsString();
                    if(tomorrow.equals(oldTomorrow))
                        Logger.LOGGER.info("Tomorrow's color verified, no change, always "+oldTomorrow);
                    else
                        Logger.LOGGER.info("Tomorrow's color updated: "+oldTomorrow+" -> "+tomorrow);
                    lastTomorrowVerificationTime=now;
                }
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unable to get tomorrow's color ("+e.toString()+")");
                e.printStackTrace();
            }
        return tomorrow;
    }

    public static void main(String[] args)
    {
        System.out.println(getTomorrow());
    }
}
