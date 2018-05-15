package sky.epaperscreenupdater;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.SystemInfoProvider;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import sky.program.Duration;

public class AboutPage extends AbstractSinglePage
{
    private long lastRefreshTime;

    public AboutPage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "À propos";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(9).secondMinus(200).millisecond())
        {
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                try(InputStream inputStream=new FileInputStream(new File("piejamsun.png")))
                {
                    g2d.drawImage(ImageIO.read(inputStream),12,12,null);
                }
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(13f);
                g2d.setFont(baseFont);
                g2d.drawString("EpaperScreenUpdater by PJ Skyman",55,24);
                g2d.drawString("A Pie Jam Sun production",55,44);
                g2d.drawString("© 2018",55,64);
                g2d.setFont(baseFont.deriveFont(12f));
                try
                {
                    SystemInfoProvider systemInfoProvider=PlatformManager.getPlatform().getSystemInfoProvider();
                    String systemInfo="CPU@"+DECIMAL_0_FORMAT.format((double)systemInfoProvider.getClockFrequencyArm()/1e9d)+" GHz: "+DECIMAL_0_FORMAT.format((double)systemInfoProvider.getCpuTemperature())+"°C@"+DECIMAL_00_FORMAT.format((double)systemInfoProvider.getCpuVoltage())+" V, RAM free: "+INTEGER_FORMAT.format((double)systemInfoProvider.getMemoryFree()/1_048_576d)+" Mo";
                    g2d.drawString(systemInfo,12,118);
                }
                catch(Exception e)
                {
                    g2d.drawString("Infos about system unavailable",12,118);
                    Logger.LOGGER.error(e.toString());
                }
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("about.png")))
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
        new AboutPage(null).potentiallyUpdate();
    }
}
