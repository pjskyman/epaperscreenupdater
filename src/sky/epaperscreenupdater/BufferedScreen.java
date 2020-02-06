package sky.epaperscreenupdater;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class BufferedScreen
{
    private Pixel[][] validatedPixels;
    private Pixel[][] workingPixels;
    private Rectangle boundsToRefresh;
    private RefreshType refreshType;

    public BufferedScreen(RefreshType refreshType)
    {
        workingPixels=new Pixel[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()][EpaperScreenManager.getEpaperScreenSize().getBigHeight()];
        for(int j=0;j<EpaperScreenManager.getEpaperScreenSize().getLittleWidth();j++)
            for(int i=0;i<EpaperScreenManager.getEpaperScreenSize().getBigHeight();i++)
                workingPixels[j][i]=Pixel.WHITE;
        validatedPixels=null;
        boundsToRefresh=new Rectangle(0,0,EpaperScreenManager.getEpaperScreenSize().getLittleWidth(),EpaperScreenManager.getEpaperScreenSize().getBigHeight());
        this.refreshType=refreshType;
    }

    public synchronized void writeImage(BufferedImage image)
    {
        int[] sourcePixel=new int[4];
        WritableRaster sourceRaster=image.getRaster();
        for(int x=0;x<image.getWidth();x++)
            for(int y=0;y<image.getHeight();y++)
                workingPixels[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()-1-y][x]=(sourcePixel=sourceRaster.getPixel(x,y,sourcePixel))[1]==128?Pixel.TRANSPARENT:sourcePixel[1]>0?Pixel.WHITE:Pixel.BLACK;
        if(validatedPixels==null)
        {
            boundsToRefresh=new Rectangle(0,0,EpaperScreenManager.getEpaperScreenSize().getLittleWidth(),EpaperScreenManager.getEpaperScreenSize().getBigHeight());
            validatedPixels=workingPixels;
            working
        }
        else
        {
        }
    }
}
