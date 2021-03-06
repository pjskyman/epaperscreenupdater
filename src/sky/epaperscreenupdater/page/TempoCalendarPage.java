package sky.epaperscreenupdater.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import sky.housecommon.Logger;
import sky.program.Duration;

public class TempoCalendarPage extends AbstractSinglePage
{
    public TempoCalendarPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Calendrier Tempo";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(11).minutePlus(15).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        for(int i=0;i<=12;i++)
            g2d.drawLine(12,9*i,i==0?282:291,9*i);
        for(int i=0;i<=28;i++)
            g2d.drawLine(12+9*i,0,12+9*i,108);
        g2d.drawLine(273,0,273,45);
        g2d.drawLine(273,54,273,108);
        g2d.drawLine(282,0,282,45);
        g2d.drawLine(282,54,282,108);
        g2d.drawLine(291,9,291,18);
        g2d.drawLine(291,27,291,45);
        g2d.drawLine(291,54,291,63);
        g2d.drawLine(291,72,291,81);
        g2d.drawLine(291,90,291,108);

        int elapsedBlueDayCount=0;
        int remainingBlueDayCount=300;
        int elapsedWhiteDayCount=0;
        int remainingWhiteDayCount=43;
        int elapsedRedDayCount=0;
        int remainingRedDayCount=22;
        try
        {
            GregorianCalendar calendar=new GregorianCalendar(Locale.FRANCE);
            calendar.setTimeInMillis(System.currentTimeMillis());
            int yearBegin=calendar.get(Calendar.MONTH)>Calendar.SEPTEMBER||
                          calendar.get(Calendar.MONTH)==Calendar.SEPTEMBER&&calendar.get(Calendar.DAY_OF_MONTH)>1||
                          calendar.get(Calendar.MONTH)==Calendar.SEPTEMBER&&calendar.get(Calendar.DAY_OF_MONTH)==1&&calendar.get(Calendar.HOUR_OF_DAY)>6?
                          calendar.get(Calendar.YEAR):
                          (calendar.get(Calendar.YEAR)-1);
            if(calendar.isLeapYear(yearBegin+1))
            {
                g2d.drawLine(273,45,273,54);//barre supplémentaire de l'année bissextile
                remainingBlueDayCount+=1;
            }
            JsonObject historyObject=JsonUtils.getJsonResponse("https://particulier.edf.fr/services/rest/referentiel/historicTEMPOStore?dateBegin="+yearBegin+"&dateEnd="+(yearBegin+1));
            if(historyObject!=null)
            {
                JsonArray history=historyObject.getAsJsonArray("dates");
                if(history!=null)
                {
                    int lastDateDay=0;
                    String lastDateColor=null;
                    for(int i=0;i<history.size();i++)
                    {
                        String date=history.get(i).getAsJsonObject().getAsJsonPrimitive("date").getAsString();
                        int month=Integer.parseInt(date.substring(5,7));
                        int day=Integer.parseInt(date.substring(8,10));
                        int x=13+(day-1)*9;
                        int y=1+((month-1+4)%12)*9;
                        String color=history.get(i).getAsJsonObject().getAsJsonPrimitive("couleur").getAsString();
                        lastDateDay=day;
                        lastDateColor=color;
                        if(color.contains("BLEU"))
                        {
                            g2d.drawLine(x+3,y+3,x+4,y+3);
                            g2d.drawLine(x+3,y+4,x+4,y+4);
                            elapsedBlueDayCount++;
                            remainingBlueDayCount--;
                        }
                        else
                            if(color.contains("BLANC"))
                            {
                                g2d.drawLine(x+3,y+2,x+4,y+2);
                                g2d.drawLine(x+2,y+3,x+5,y+3);
                                g2d.drawLine(x+2,y+4,x+5,y+4);
                                g2d.drawLine(x+3,y+5,x+4,y+5);
                                elapsedWhiteDayCount++;
                                remainingWhiteDayCount--;
                            }
                            else
                                if(color.contains("ROUGE"))
                                {
                                    g2d.drawLine(x+2,y+1,x+5,y+1);
                                    g2d.drawLine(x+1,y+2,x+6,y+2);
                                    g2d.drawLine(x+1,y+3,x+6,y+3);
                                    g2d.drawLine(x+1,y+4,x+6,y+4);
                                    g2d.drawLine(x+1,y+5,x+6,y+5);
                                    g2d.drawLine(x+2,y+6,x+5,y+6);
                                    elapsedRedDayCount++;
                                    remainingRedDayCount--;
                                }
                        int year=Integer.parseInt(date.substring(0,4));
                        calendar.clear();
                        calendar.set(year,month-1,day);
                        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                        {
                            g2d.drawLine(x+0,y+0,x+7,y+0);
                            g2d.drawLine(x+7,y+0,x+7,y+7);
                            g2d.drawLine(x+7,y+7,x+0,y+7);
                            g2d.drawLine(x+0,y+7,x+0,y+0);
                        }
                    }
                    //correction si la couleur du lendemain a été comptée dans ce qu'il reste
                    if(lastDateDay!=0)//simple précaution peut-être inutile...
                    {
                        calendar.clear();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        int currentDay=calendar.get(Calendar.DAY_OF_MONTH);
                        int currentHour=calendar.get(Calendar.HOUR_OF_DAY);
                        if(currentDay!=lastDateDay||
                           currentDay==lastDateDay&&currentHour<6)
                            if(lastDateColor!=null&&lastDateColor.contains("BLEU"))
                            {
                                elapsedBlueDayCount--;
                                remainingBlueDayCount++;
                            }
                            else
                                if(lastDateColor!=null&&lastDateColor.contains("BLANC"))
                                {
                                    elapsedWhiteDayCount--;
                                    remainingWhiteDayCount++;
                                }
                                else
                                    if(lastDateColor!=null&&lastDateColor.contains("ROUGE"))
                                    {
                                        elapsedRedDayCount--;
                                        remainingRedDayCount++;
                                    }
                    }
                    Logger.LOGGER.info("Tempo history retrieved successfully");
                }
                else
                    Logger.LOGGER.warn("Timeout or error when retrieving tempo history");
            }
            else
                Logger.LOGGER.warn("Timeout or error when retrieving tempo history");
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unable to retrieve Tempo history ("+e.toString()+")");
            e.printStackTrace();
        }

        Font baseFont=FREDOKA_ONE_FONT.deriveFont(20f);
        Font monthFont=baseFont.deriveFont(11f);
        Font dayFont=baseFont.deriveFont(11f).deriveFont(AffineTransform.getScaleInstance(.9d,1d));
        Font bottomFont=baseFont.deriveFont(12f);

        String[] monthStrings=new String[]{"S","O","N","D","J","F","M","A","M","J","J","A"};
        g2d.setFont(monthFont);
        for(int i=0;i<monthStrings.length;i++)
        {
            String monthString=monthStrings[i];
            int monthStringWidth=(int)Math.ceil(monthFont.getStringBounds(monthString,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(monthString,7-monthStringWidth/2,9*i+9);
        }

        int[] dayNumbers=new int[]{5,10,15,20,25,30};
        g2d.setFont(dayFont);
        for(int i=0;i<dayNumbers.length;i++)
        {
            String dayString=""+dayNumbers[i];
            int dayStringWidth=(int)Math.ceil(dayFont.getStringBounds(dayString,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(dayString,8+9*dayNumbers[i]-dayStringWidth/2,118);
        }

        g2d.setFont(bottomFont);
        String bottomString="BLEU : "+elapsedBlueDayCount+"~"+remainingBlueDayCount+"    BLANC : "+elapsedWhiteDayCount+"~"+remainingWhiteDayCount+"    ROUGE : "+elapsedRedDayCount+"~"+remainingRedDayCount;
        int bottomStringWidth=(int)Math.ceil(bottomFont.getStringBounds(bottomString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(bottomString,148-bottomStringWidth/2,128);
    }

    protected String getDebugImageFileName()
    {
        return "calendar.png";
    }

    public static void main(String[] args)
    {
        new TempoCalendarPage(null).potentiallyUpdate();
    }
}
