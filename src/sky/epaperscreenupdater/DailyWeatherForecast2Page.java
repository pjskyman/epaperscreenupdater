package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.program.Duration;

public class DailyWeatherForecast2Page extends AbstractWeatherForecastPage
{
    private long lastRefreshTime;

    public DailyWeatherForecast2Page()
    {
        lastRefreshTime=0L;
    }

    public int getSerial()
    {
        return 10;
    }

    public String getName()
    {
        return "Prévisions météo 2";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(4).minutePlus(13).second())
        {
            lastRefreshTime=now;
            List<Daily> dailies=getLastDailies();
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                for(int x=51;x<296;x+=35)
                    g2d.drawLine(x,0,x,127);
                for(int y=20;y<128;y+=12)
                    g2d.drawLine(0,y,295,y);
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(12f);
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
                    string=TIME_FORMAT.format(new Date(daily.getSunriseTime()));
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,43);
                    string=TIME_FORMAT.format(new Date(daily.getSunsetTime()));
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,55);
                    string=TEMPERATURE_FORMAT.format((double)(daily.getSunsetTime()-daily.getSunriseTime())/3.6e6d*(1d-daily.getCloudCover()));
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,67);
                    string=WIND_FORMAT.format(daily.getUvIndex());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,79);
                    string=""+daily.getMoonPhase();
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,91);
                    string=TEMPERATURE_FORMAT.format(daily.getVisibility());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,103);
                    string=WIND_FORMAT.format(daily.getOzone());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,115);
                    string="";
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,127);
                }
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("weather2.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels().writeImage(sourceImage);
                Logger.LOGGER.info("Page "+getSerial()+" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    public boolean hasHighFrequency()
    {
        return false;
    }

    public static void main(String[] args)
    {
        new DailyWeatherForecast2Page().potentiallyUpdate();
    }
}
