package sky.epaperscreenupdater;

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

    public synchronized void writeImage(BufferedImage image)
    {
        workingScreenContent.writeImage(image);
        validatedScreenContent=workingScreenContent;
        workingScreenContent=new ScreenContent(refreshType);
    }

    public ScreenContent getScreenContent()
    {
        return validatedScreenContent;
    }
}
