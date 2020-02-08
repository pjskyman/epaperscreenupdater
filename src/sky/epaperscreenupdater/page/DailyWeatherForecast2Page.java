package sky.epaperscreenupdater.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class DailyWeatherForecast2Page extends AbstractSinglePage
{
    public DailyWeatherForecast2Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo 2/2";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(4).minutePlus(13).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        List<Daily> dailies=WeatherForecastUtils.getLastDailies();
        for(int x=51;x<296;x+=35)
            g2d.drawLine(x,0,x,127);
        for(int y=20;y<128;y+=12)
            g2d.drawLine(0,y,295,y);
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(12f);

        g2d.setFont(baseFont);

        String string="Date";
        int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,14);

        string="Icône";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,31);

        string="hSol“";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,43);

        string="hSol„";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,55);

        string="£hSol";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,67);

        string="UVi";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,79);

        string="PhLune";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,91);

        string="kmVisib";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,103);

        string="Ozone";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,115);

        string="";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,127);

        for(int i=0;i<dailies.size();i++)
        {
            int baseX=51+i*35;
            Daily daily=dailies.get(i);

            GregorianCalendar calendar=new GregorianCalendar();
            calendar.setTimeInMillis(daily.getTime());
            string=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.FRANCE);
            string=string.substring(0,1).toUpperCase()+string.substring(1);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,9);

            string=SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(new Date(daily.getTime())).substring(0,2);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,19);

            g2d.drawImage(Icons.getIcon(daily.getIcon()),baseX+1,21,null);

            string=SHORT_TIME_FORMAT.format(new Date(daily.getSunriseTime()));
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,43);

            string=SHORT_TIME_FORMAT.format(new Date(daily.getSunsetTime()));
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,55);

            string=DECIMAL_0_FORMAT.format((double)(daily.getSunsetTime()-daily.getSunriseTime())/3.6e6d*(1d-daily.getCloudCover()));
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,67);

            string=INTEGER_FORMAT.format(daily.getUvIndex());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,79);

            g2d.fillOval(baseX+18-12/2-4,91-12/2-5,12,12);
            g2d.setColor(Color.WHITE);
//                    g2d.fillArc(baseX+18-10/2-4,91-10/2-5,10,10,90,(int)(360d-daily.getMoonPhase()*360d));
            Path2D path=new Path2D.Double();
            path.moveTo((double)baseX+18d,82d);
            if(daily.getMoonPhase()<=.5d)
            {
                for(int degree=0;degree<=180;degree+=10)
                    path.lineTo(
                            (double)baseX+18d-4d+5d*Math.sin((double)degree*Math.PI/180d),
                            91d-5d-5d*Math.cos((double)degree*Math.PI/180d)
                    );
                for(int degree=180;degree>=0;degree-=10)
                    path.lineTo(
                            (double)baseX+18d-4d+5d*Math.sin((double)degree*Math.PI/180d)*Math.cos(daily.getMoonPhase()*Math.PI/.5d),
                            91d-5d-5d*Math.cos((double)degree*Math.PI/180d)
                    );
            }
            else
            {
                for(int degree=0;degree<=180;degree+=10)
                    path.lineTo(
                            (double)baseX+18d-4d-5d*Math.sin((double)degree*Math.PI/180d)*Math.cos((1d-daily.getMoonPhase())*Math.PI/.5d),
                            91d-5d-5d*Math.cos((double)degree*Math.PI/180d)
                    );
                for(int degree=180;degree>=0;degree-=10)
                    path.lineTo(
                            (double)baseX+18d-4d-5d*Math.sin((double)degree*Math.PI/180d),
                            91d-5d-5d*Math.cos((double)degree*Math.PI/180d)
                    );
            }
            g2d.fill(new Area(path));
            g2d.setColor(Color.BLACK);

            string=DECIMAL_0_FORMAT.format(daily.getVisibility());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,103);

            string=INTEGER_FORMAT.format(daily.getOzone());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,115);

            string="";
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,127);
        }
    }

    protected String getDebugImageFileName()
    {
        return "weather2.png";
    }

    public static void main(String[] args)
    {
        new DailyWeatherForecast2Page(null).potentiallyUpdate();
    }
}
