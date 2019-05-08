package sky.epaperscreenupdater;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import sky.program.Duration;

public class HourlyWeatherForecastPage extends AbstractWeatherForecastPage
{
    private final int rank;
    private final long refreshDelay;
    private long lastRefreshTime;

    protected HourlyWeatherForecastPage(Page parentPage,int rank,long refreshDelay)
    {
        super(parentPage);
        this.rank=rank;
        this.refreshDelay=refreshDelay;
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Prévisions météo H/H"+(rank>1?" +"+((rank-1)*7)+"h":"");
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>refreshDelay)
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
                Font baseFont=Main.FREDOKA_ONE_FONT.deriveFont(12f);

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

                Stroke largeStroke=new BasicStroke(2.5f);
                Stroke normalStroke=new BasicStroke();
                g2d.setStroke(normalStroke);

                for(int i=(rank-1)*7;i<Math.min(hourlies.size(),rank*7);i++)
                {
                    int baseX=51+(i-(rank-1)*7)*35;
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

                    string=DECIMAL_0_FORMAT.format(hourly.getTemperature());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,43);

                    string=INTEGER_FORMAT.format(hourly.getHumidity()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
                    g2d.drawString(string,baseX+18-stringWidth/2,55);
                    g2d.fillOval(baseX+18+stringWidth/2-12/2-4,55-12/2-5,12,12);
                    g2d.setColor(Color.WHITE);
                    g2d.fillArc(baseX+18+stringWidth/2-10/2-4,55-10/2-5,10,10,90,(int)(360d-hourly.getHumidity()*360d));
                    g2d.setColor(Color.BLACK);

                    string=DECIMAL_0_FORMAT.format(hourly.getDewPoint());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,67);

                    string=INTEGER_FORMAT.format(hourly.getWindGust());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
                    g2d.drawString(string,baseX+18-stringWidth/2,79);
                    g2d.setStroke(largeStroke);
                    g2d.drawLine(
                            baseX+18+stringWidth/2-4+(int)(Math.sin((double)hourly.getWindBearing()*Math.PI/180d)*6d),
                            79-5-(int)(Math.cos((double)hourly.getWindBearing()*Math.PI/180d)*6d),
                            baseX+18+stringWidth/2-4-(int)(Math.sin((double)hourly.getWindBearing()*Math.PI/180d)*4d),
                            79-5+(int)(Math.cos((double)hourly.getWindBearing()*Math.PI/180d)*4d)
                    );
                    g2d.setStroke(normalStroke);
                    Path2D path=new Path2D.Double();
                    path.moveTo(
                            (double)baseX+18d+(double)stringWidth/2d-4d-Math.sin((double)hourly.getWindBearing()*Math.PI/180d)*6d,
                            79d-5d+Math.cos((double)hourly.getWindBearing()*Math.PI/180d)*6d
                    );
                    path.lineTo(
                            (double)baseX+18d+(double)stringWidth/2d-4d-Math.sin((double)(hourly.getWindBearing()+90)*Math.PI/180d)*5d,
                            79d-5d+Math.cos((double)(hourly.getWindBearing()+90)*Math.PI/180d)*5d
                    );
                    path.lineTo(
                            (double)baseX+18d+(double)stringWidth/2d-4d-Math.sin((double)(hourly.getWindBearing()-90)*Math.PI/180d)*5d,
                            79d-5d+Math.cos((double)(hourly.getWindBearing()-90)*Math.PI/180d)*5d
                    );
                    path.closePath();
                    g2d.fill(path);

                    string=DECIMAL_0_FORMAT.format(hourly.getPrecipIntensity());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,91);

                    string=INTEGER_FORMAT.format(hourly.getPrecipProbability()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
                    g2d.drawString(string,baseX+18-stringWidth/2,103);
                    g2d.fillOval(baseX+18+stringWidth/2-12/2-4,103-12/2-5,12,12);
                    g2d.setColor(Color.WHITE);
                    g2d.fillArc(baseX+18+stringWidth/2-10/2-4,103-10/2-5,10,10,90,(int)(360d-hourly.getPrecipProbability()*360d));
                    g2d.setColor(Color.BLACK);

                    string=INTEGER_FORMAT.format(hourly.getPressure());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,115);

                    string=INTEGER_FORMAT.format(hourly.getCloudCover()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
                    g2d.drawString(string,baseX+18-stringWidth/2,127);
                    g2d.fillOval(baseX+18+stringWidth/2-12/2-4,127-12/2-5,12,12);
                    g2d.setColor(Color.WHITE);
                    g2d.fillArc(baseX+18+stringWidth/2-10/2-4,127-10/2-5,10,10,90,(int)(360d-hourly.getCloudCover()*360d));
                    g2d.setColor(Color.BLACK);
                }
                g2d.dispose();
                try(OutputStream outputStream=new FileOutputStream(new File("weather"+(rank+2)+".png")))
                {
                    ImageIO.write(sourceImage,"png",outputStream);
                }
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
        new HourlyWeatherForecastPage(null,1,Duration.of(4).minutePlus(17).second()).potentiallyUpdate();
    }
}
