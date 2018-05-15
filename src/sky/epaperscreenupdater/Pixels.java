package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Pixels
{
    private final Pixel[][] pixels;
    private final RefreshType refreshType;

    public Pixels(RefreshType refreshType)
    {
        pixels=new Pixel[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()][EpaperScreenManager.getEpaperScreenSize().getBigHeight()];
        for(int j=0;j<EpaperScreenManager.getEpaperScreenSize().getLittleWidth();j++)
            for(int i=0;i<EpaperScreenManager.getEpaperScreenSize().getBigHeight();i++)
                pixels[j][i]=Pixel.WHITE;
        this.refreshType=refreshType;
    }

    public Pixels writeImage(BufferedImage image)
    {
        int[] sourcePixel=new int[4];
        WritableRaster sourceRaster=image.getRaster();
        for(int x=0;x<image.getWidth();x++)
            for(int y=0;y<image.getHeight();y++)
                pixels[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()-1-y][x]=(sourcePixel=sourceRaster.getPixel(x,y,sourcePixel))[1]==128?Pixel.TRANSPARENT:sourcePixel[1]>0?Pixel.WHITE:Pixel.BLACK;
        return this;
    }

    public Pixels incrustTransparentImage(Pixels image)
    {
        Pixels newPixels=new Pixels(refreshType.combine(image.refreshType));
        for(int j=0;j<EpaperScreenManager.getEpaperScreenSize().getLittleWidth();j++)
            for(int i=0;i<EpaperScreenManager.getEpaperScreenSize().getBigHeight();i++)
                if(image.pixels[j][i]!=Pixel.TRANSPARENT)
                    newPixels.pixels[j][i]=image.pixels[j][i];
                else
                    newPixels.pixels[j][i]=pixels[j][i];
        return newPixels;
    }

    public Pixel getPixel(int i,int j)
    {
        return pixels[i][j];
    }

    public RefreshType getRefreshType()
    {
        return refreshType;
    }

    public boolean isIOk(int i)
    {
        return i<pixels.length;
    }
}
