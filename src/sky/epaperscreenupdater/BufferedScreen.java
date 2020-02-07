package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BufferedScreen
{
    private ScreenContent validatedScreenContent;
    private ScreenContent workingScreenContent;
    private final RefreshType refreshType;

    public BufferedScreen(RefreshType refreshType)
    {
        workingScreenContent=new ScreenContent(refreshType);
        validatedScreenContent=null;
        this.refreshType=refreshType;
    }

    public BufferedScreen initializeBlank()
    {
        BufferedImage blankImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2d=blankImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,296,128);
        g2d.dispose();
        writeImage(blankImage);
        return this;
    }

    public synchronized BufferedScreen writeImage(BufferedImage image)
    {
        workingScreenContent.writeImage(image);
        validatedScreenContent=workingScreenContent;
        workingScreenContent=new ScreenContent(refreshType);
        return this;
    }

    public ScreenContent getScreenContent()
    {
        return validatedScreenContent;
    }
}
