package sky.epaperscreenupdater.page;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.SystemInfoProvider;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import sky.epaperscreenupdater.Logger;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class AboutPage extends AbstractSinglePage
{
    public AboutPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "À propos";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(9).secondMinus(200).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        try(InputStream inputStream=new FileInputStream(new File("piejamsun.png")))
        {
            g2d.drawImage(ImageIO.read(inputStream),12,12,null);
        }
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(13f);
        g2d.setFont(baseFont);
        g2d.drawString("EpaperScreenUpdater by PJ Skyman",55,24);
        g2d.drawString("A Pie Jam Sun production",55,44);
        g2d.drawString("©2018-2020",55,64);
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
