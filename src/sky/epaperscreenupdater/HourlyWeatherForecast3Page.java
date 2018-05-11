package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.program.Duration;

public class HourlyWeatherForecast3Page extends AbstractWeatherForecastPage
{
    private long lastRefreshTime;

    public HourlyWeatherForecast3Page()
    {
        lastRefreshTime=0L;
    }

    public int getSerial()
    {
        return 13;
    }

    public String getName()
    {
        return "Prévisions météo H/H 3";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(4).minutePlus(20).second())
        {
            lastRefreshTime=now;
            List<Hourly> hourlies=getLastHourlies();
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
                Font alternativeBaseFont=baseFont.deriveFont(AffineTransform.getScaleInstance(.75d,1d));
                g2d.setFont(baseFont);
                String string="Heure";
                int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,14);
                string="Icône";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,31);
                string="°C";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,43);
                string="%Hum";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,55);
                string="°CRosée";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,67);
                string="Vent";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,79);
                string="mmPluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,91);
                string="%Pluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,103);
                string="hPa";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,115);
                string="%Nuage";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,127);
                for(int i=14;i<Math.min(hourlies.size(),21);i++)
                {
                    int baseX=51+(i-14)*35;
                    Hourly hourly=hourlies.get(i);
                    GregorianCalendar calendar=new GregorianCalendar();
                    calendar.setTimeInMillis(hourly.getTime());
                    string=new SimpleDateFormat("HH:mm").format(new Date(hourly.getTime()));
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,9);
                    string=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.FRANCE);
                    string=string.substring(0,1).toUpperCase()+string.substring(1);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,19);
                    g2d.drawImage(Icons.getIcon(hourly.getIcon()),baseX+1,21,null);
                    string=TEMPERATURE_FORMAT.format(hourly.getTemperature());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,43);
                    string=WIND_FORMAT.format(hourly.getHumidity()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,55);
                    string=TEMPERATURE_FORMAT.format(hourly.getDewPoint());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,67);
                    g2d.setFont(alternativeBaseFont);
                    string=WIND_FORMAT.format(hourly.getWindGust())+"/"+HomeWeatherPage.convertWindAngle(hourly.getWindBearing());
                    stringWidth=(int)Math.ceil(alternativeBaseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,79);
                    g2d.setFont(baseFont);
                    string=TEMPERATURE_FORMAT.format(hourly.getPrecipIntensity());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,91);
                    string=WIND_FORMAT.format(hourly.getPrecipProbability()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,103);
                    string=WIND_FORMAT.format(hourly.getPressure());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,115);
                    string=WIND_FORMAT.format(hourly.getCloudCover()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,127);
                }
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("weather5.png")))
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
        new HourlyWeatherForecast3Page().potentiallyUpdate();
    }
}
