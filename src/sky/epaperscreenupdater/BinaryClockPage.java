package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import sky.program.Duration;

public class BinaryClockPage extends AbstractPage
{
    private long lastRefreshTime;

    public BinaryClockPage()
    {
        lastRefreshTime=0L;
    }

    public int getSerial()
    {
        return 7;
    }

    public String getName()
    {
        return "Horloge binaire";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(1).second())
        {
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Date date=new Date();
                String hour=new SimpleDateFormat("HH").format(date);
                String minute=new SimpleDateFormat("mm").format(date);
                String second=new SimpleDateFormat("ss").format(date);
                for(int i=1;i<=4;i++)
                    if((Integer.parseInt(hour.substring(0,1))/(1<<i-1))%2==1)
                        g2d.fillRect(53,129-32*i,30,30);
                for(int i=1;i<=4;i++)
                    if((Integer.parseInt(hour.substring(1,2))/(1<<i-1))%2==1)
                        g2d.fillRect(85,129-32*i,30,30);
                for(int i=1;i<=4;i++)
                    if((Integer.parseInt(minute.substring(0,1))/(1<<i-1))%2==1)
                        g2d.fillRect(117,129-32*i,30,30);
                for(int i=1;i<=4;i++)
                    if((Integer.parseInt(minute.substring(1,2))/(1<<i-1))%2==1)
                        g2d.fillRect(149,129-32*i,30,30);
                for(int i=1;i<=4;i++)
                    if((Integer.parseInt(second.substring(0,1))/(1<<i-1))%2==1)
                        g2d.fillRect(181,129-32*i,30,30);
                for(int i=1;i<=4;i++)
                    if((Integer.parseInt(second.substring(1,2))/(1<<i-1))%2==1)
                        g2d.fillRect(213,129-32*i,30,30);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("binaryclock.png")))
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
        new BinaryClockPage().potentiallyUpdate();
    }
}
