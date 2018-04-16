package sky.epaperscreenupdater;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AnalogClockPage extends AbstractPage
{
    private long lastRefreshTime;

    public AnalogClockPage()
    {
        lastRefreshTime=0L;
    }

    public int getSerial()
    {
        return 6;
    }

    public String getName()
    {
        return "Horloge analogique";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Time.get(1).second())
        {
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(148-64,0,127,127);
                g2d.fillOval(148-5,64-5,9,9);
                GregorianCalendar calendar=new GregorianCalendar();
                double second=(double)calendar.get(Calendar.SECOND);
                double minute=((double)calendar.get(Calendar.MINUTE)+second/60d);
                double hour=((double)calendar.get(Calendar.HOUR_OF_DAY)+minute/60d);
                g2d.setStroke(new BasicStroke(3f));
                g2d.draw(new Line2D.Double(148d-Math.sin(hour*Math.PI/6d)*14d,64d+Math.cos(hour*Math.PI/6d)*14d,148d+Math.sin(hour*Math.PI/6d)*39d,64d-Math.cos(hour*Math.PI/6d)*39d));
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(new Line2D.Double(148d-Math.sin(minute*Math.PI/30d)*14d,64d+Math.cos(minute*Math.PI/30d)*14d,148d+Math.sin(minute*Math.PI/30d)*56d,64d-Math.cos(minute*Math.PI/30d)*56d));
                g2d.setStroke(new BasicStroke(1f));
                g2d.draw(new Line2D.Double(148d-Math.sin(second*Math.PI/30d)*22d,64d+Math.cos(second*Math.PI/30d)*22d,148d+Math.sin(second*Math.PI/30d)*58d,64d-Math.cos(second*Math.PI/30d)*58d));
                for(int i=0;i<60;i++)
                    g2d.draw(new Line2D.Double(148d+Math.sin((double)i*Math.PI/30d)*63d,64d-Math.cos((double)i*Math.PI/30d)*63d,148d+Math.sin((double)i*Math.PI/30d)*(i%5==0?52d:59d),64d-Math.cos((double)i*Math.PI/30d)*(i%5==0?52d:59d)));
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("analogclock.png")))
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

    public Pixels getPixels()
    {
        return pixels;
    }

    public boolean hasHighFrequency()
    {
        return true;
    }

    public static void main(String[] args)
    {
        new AnalogClockPage().potentiallyUpdate();
    }
}
