package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class AnniversaryPage extends AbstractSinglePage
{
    public AnniversaryPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Anniversaires";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).hourPlus(1).minutePlus(20).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Properties properties=new Properties();
        try(InputStream inputStream=new FileInputStream(new File("anniversary.ini")))
        {
            properties.load(new InputStreamReader(inputStream,Charset.forName("utf8")));
        }
        List<Entry<Birthday,String>> anniversaries=new ArrayList<>();
        properties.stringPropertyNames().forEach(key->
        {
            String anniversaryDate=(String)properties.get(key);
            GregorianCalendar calendar=new GregorianCalendar();
            int year=calendar.get(Calendar.YEAR);
            int month=Integer.parseInt(anniversaryDate.substring(2,4));
            int day=Integer.parseInt(anniversaryDate.substring(0,2));
            calendar.clear();
            calendar.set(year,month-1,day,23,59,59);
            if(calendar.getTimeInMillis()<System.currentTimeMillis())
                calendar.set(Calendar.YEAR,year+1);
            anniversaries.add(new SimpleEntry<>(new Birthday(calendar,Integer.parseInt(anniversaryDate.substring(4,8))),key));
        });
        anniversaries.sort((o1,o2)->o1.getKey().getCalendar().compareTo(o2.getKey().getCalendar()));
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(14f);
        g2d.setFont(baseFont);
        g2d.drawString("Prochains anniversaires :",5,14);
        GregorianCalendar nowCalendar=new GregorianCalendar();
        for(int i=0;i<anniversaries.size();i++)
        {
            GregorianCalendar calendar=anniversaries.get(i).getKey().getCalendar();
            String string=""+calendar.get(Calendar.DAY_OF_MONTH);
            if(string.equals("1"))
                string+="er";
            string+=" "+calendar.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.FRANCE);
            string+=" : "+anniversaries.get(i).getValue();
            string+=" ("+(calendar.get(Calendar.YEAR)-anniversaries.get(i).getKey().getBirthYear())+")";
            if(calendar.get(Calendar.DAY_OF_MONTH)==nowCalendar.get(Calendar.DAY_OF_MONTH)&&calendar.get(Calendar.MONTH)==nowCalendar.get(Calendar.MONTH))
                string="!!! "+string+" !!!";
            g2d.drawString(string,5,14*(i+2));
        }
    }

    protected String getDebugImageFileName()
    {
        return "anniversary.png";
    }

    public static void main(String[] args)
    {
        new AnniversaryPage(null).potentiallyUpdate();
    }
}
