package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import sky.epaperscreenupdater.page.AbstractPage;

public class Test
{
    public static void main(String[] args)
    {
        try
        {
            for(int i=0;i<=10;i++)
            {
                EpaperScreenManager.display(getScreen(i),i==0?RefreshType.TOTAL_REFRESH:RefreshType.PARTIAL_REFRESH);
                if(i<10)
                    Thread.sleep(2000L);
            }
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
        Random random=new Random();
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
            g2d.drawString("EpaperScreenUpdater by PJ Skyman",random.nextInt(150),20+random.nextInt(10));
            g2d.drawString("A Pie Jam Sun production",random.nextInt(150),40+random.nextInt(10));
            g2d.drawString("©2018-2020",random.nextInt(150),60+random.nextInt(10));
            g2d.drawString(""+count,random.nextInt(150),80+random.nextInt(10));
            g2d.drawLine(random.nextInt(296),random.nextInt(128),random.nextInt(296),random.nextInt(128));
            g2d.drawLine(random.nextInt(296),random.nextInt(128),random.nextInt(296),random.nextInt(128));
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
