package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.program.Duration;

public class DailyWeatherForecast1Page extends AbstractWeatherForecastPage
{
    private long lastRefreshTime;

    public DailyWeatherForecast1Page(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Prévisions météo 1/2";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(4).minutePlus(9).second())
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
                Font alternativeBaseFont=baseFont.deriveFont(AffineTransform.getScaleInstance(.75d,1d));

                g2d.setFont(baseFont);

                String string="Date";
                int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,14);

                string="Icône";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,31);

                string="°CMin";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,43);

                string="°CMax";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,55);

                string="%Hum";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,67);

                string="°CRosée";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,79);

                string="Vent";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,91);

                string="mmPluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,103);

                string="%Pluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,115);

                string="hPa";
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

                    string=DECIMAL_0_FORMAT.format(daily.getTemperatureMin());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,43);

                    string=DECIMAL_0_FORMAT.format(daily.getTemperatureMax());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,55);

                    string=INTEGER_FORMAT.format(daily.getHumidity()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,67);

                    string=DECIMAL_0_FORMAT.format(daily.getDewPoint());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,79);

                    g2d.setFont(alternativeBaseFont);

                    string=INTEGER_FORMAT.format(daily.getWindGust())+"/"+HomeWeatherPage.convertWindAngle(daily.getWindBearing());
                    stringWidth=(int)Math.ceil(alternativeBaseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,91);

                    g2d.setFont(baseFont);

                    string=DECIMAL_0_FORMAT.format(daily.getPrecipIntensity()*24d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,103);

                    string=INTEGER_FORMAT.format(daily.getPrecipProbability()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,115);

                    string=INTEGER_FORMAT.format(daily.getPressure());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,127);
                }
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("weather1.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH).writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    public static void main(String[] args)
    {
        new DailyWeatherForecast1Page(null).potentiallyUpdate();
    }
}
