package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import sky.epaperscreenupdater.page.AbstractPage;

public class Test
{
    public static void main(String[] args)
    {
        try
        {
            EpaperScreenManager.display(getScreen(0),RefreshType.TOTAL_REFRESH);
            Thread.sleep(2000L);
            EpaperScreenManager.display(getScreen(1),RefreshType.PARTIAL_REFRESH);
            Thread.sleep(2000L);
            EpaperScreenManager.display(getScreen(2),RefreshType.PARTIAL_REFRESH);
        }
        catch(InterruptedException e)
        {
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unknown error");
            e.printStackTrace();
        }
    }

    private static Screen getScreen(int count)
    {
        Screen screen=new Screen();
        Graphics2D g2d=null;
        try
        {
            BufferedImage image=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
            g2d=image.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,296,128);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(0,0,1,0);
            g2d.drawLine(0,0,0,1);
            Font baseFont=AbstractPage.FREDOKA_ONE_FONT.deriveFont(13f);
            g2d.setFont(baseFont);
            g2d.drawString("EpaperScreenUpdater by PJ Skyman",55,24);
            g2d.drawString("A Pie Jam Sun production",55,44);
            g2d.drawString("©2018-2020",55,64);
            g2d.drawString(""+count,55,84);
            g2d.dispose();
            screen.setImage(image);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return screen;
    }
}
