package sky.epaperscreenupdater.page;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.SystemInfoProvider;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import sky.housecommon.Logger;
import sky.program.Duration;

public class AboutPage extends AbstractSinglePage
{
    private final Image pieJamSunImage;

    public AboutPage(Page parentPage)
    {
        super(parentPage);
        Image temp;
        try(InputStream inputStream=new FileInputStream(new File("piejamsun.png")))
        {
            temp=ImageIO.read(inputStream);
        }
        catch(IOException e)
        {
            temp=new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB_PRE);
        }
        pieJamSunImage=temp;
    }

    public String getName()
    {
        return "À propos";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(9).secondMinus(200).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        g2d.drawImage(pieJamSunImage,12,12,null);
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(13f);
        g2d.setFont(baseFont);
        g2d.drawString("EpaperScreenUpdater by PJ Skyman",55,24);
        g2d.drawString("A Pie Jam Sun production",55,44);
        g2d.drawString("©2018-"+new GregorianCalendar().get(Calendar.YEAR),55,64);
        g2d.setFont(baseFont.deriveFont(12f));
        try
        {
            SystemInfoProvider systemInfoProvider=PlatformManager.getPlatform().getSystemInfoProvider();
            String systemInfo="CPU temp: "+DECIMAL_0_FORMAT.format((double)systemInfoProvider.getCpuTemperature())+" °C, RAM free: "+INTEGER_FORMAT.format((double)systemInfoProvider.getMemoryFree()/1_048_576d)+" MB";
            g2d.drawString(systemInfo,12,118);
        }
        catch(Exception e)
        {
            g2d.drawString("Info about system unavailable",12,118);
            Logger.LOGGER.error(e.toString());
            e.printStackTrace();
        }
    }

    protected String getDebugImageFileName()
    {
        return "about.png";
    }

    public static void main(String[] args)
    {
        new AboutPage(null).potentiallyUpdate();
    }
}
