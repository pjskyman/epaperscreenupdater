package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Pixels
{
    private final PixelState[][] pixelStates;
    private final RefreshType refreshType;

    public Pixels(RefreshType refreshType)
    {
        pixelStates=new PixelState[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()][EpaperScreenManager.getEpaperScreenSize().getBigHeight()];
        for(int j=0;j<EpaperScreenManager.getEpaperScreenSize().getLittleWidth();j++)
            for(int i=0;i<EpaperScreenManager.getEpaperScreenSize().getBigHeight();i++)
                pixelStates[j][i]=PixelState.WHITE;
        this.refreshType=refreshType;
    }

    public Pixels writeImage(BufferedImage image)
    {
        int[] sourcePixel=new int[4];
        WritableRaster sourceRaster=image.getRaster();
        for(int x=0;x<image.getWidth();x++)
            for(int y=0;y<image.getHeight();y++)
                pixelStates[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()-1-y][x]=(sourcePixel=sourceRaster.getPixel(x,y,sourcePixel))[1]==128?PixelState.TRANSPARENT:sourcePixel[1]>0?PixelState.WHITE:PixelState.BLACK;
        return this;
    }

    public Pixels incrustTransparentImage(Pixels image)
    {
        Pixels newPixels=new Pixels(refreshType.combine(image.refreshType));
        for(int j=0;j<EpaperScreenManager.getEpaperScreenSize().getLittleWidth();j++)
            for(int i=0;i<EpaperScreenManager.getEpaperScreenSize().getBigHeight();i++)
                if(image.pixelStates[j][i]!=PixelState.TRANSPARENT)
                    newPixels.pixelStates[j][i]=image.pixelStates[j][i];
                else
                    newPixels.pixelStates[j][i]=pixelStates[j][i];
        return newPixels;
    }

    public PixelState getPixelState(int i,int j)
    {
        return pixelStates[i][j];
    }

    public RefreshType getRefreshType()
    {
        return refreshType;
    }

    public boolean isIOk(int i)
    {
        return i<pixelStates.length;
    }
}
