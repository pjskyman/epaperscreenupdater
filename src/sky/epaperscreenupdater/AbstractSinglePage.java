package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public abstract class AbstractSinglePage extends AbstractPage
{
    private long lastRefreshTime;
    private static final boolean DEBUG_IMAGE_ENABLED=false;

    protected AbstractSinglePage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getActivePageName()
    {
        return getName();
    }

    public int rankOf(Page subpage)
    {
        return -1;
    }

    public int pageCount()
    {
        return -1;
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>=getMinimalRefreshDelay())
        {
            Logger.LOGGER.info("Page \""+getName()+"\" needs to be updated");
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                populateImage(g2d);
                g2d.dispose();
                if(DEBUG_IMAGE_ENABLED)
                    try(OutputStream outputStream=new FileOutputStream(new File(getDebugImageFileName())))
                    {
                        ImageIO.write(sourceImage,"png",outputStream);
                    }
                screen.writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error when updating page \""+getName()+"\"");
                e.printStackTrace();
            }
        }
        return this;
    }

    protected abstract long getMinimalRefreshDelay();

    protected abstract void populateImage(Graphics2D g2d) throws Exception;

    protected abstract String getDebugImageFileName();

    public boolean clicked(boolean initial)
    {
        return false;
    }

    public boolean rotated(RotationDirection rotationDirection)
    {
        return false;
    }
}
