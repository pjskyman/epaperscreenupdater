package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import sky.program.Duration;

public class DigitalClockPage extends AbstractSinglePage
{
    private long lastRefreshTime;

    public DigitalClockPage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Horloge numérique";
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
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(70f);
                g2d.setFont(baseFont);
                String clockString=SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM).format(new Date());
                int clockStringWidth=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getWidth());
                int clockStringHeight=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getHeight());
                g2d.drawString(clockString,148-clockStringWidth/2,45+clockStringHeight/2);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("clock.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH_IN_FAST_MODE).writeImage(sourceImage);
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
        new DigitalClockPage(null).potentiallyUpdate();
    }
}
