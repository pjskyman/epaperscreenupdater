package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ScreenContent
{
    private final byte[] data;
    private final RefreshType refreshType;

    public ScreenContent(RefreshType refreshType)
    {
        data=new byte[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()*EpaperScreenManager.getEpaperScreenSize().getBigHeight()/4];
        this.refreshType=refreshType;
    }

    public void writeImage(BufferedImage image)
    {
        WritableRaster sourceRaster=image.getRaster();
        int i;
        int j;
        int[] sourcePixel=new int[4];
        int value;
        Pixel pixel;
        int index;
        int address;
        int offset;
        byte b;
        for(int x=0;x<image.getWidth();x++)
            for(int y=0;y<image.getHeight();y++)
            {
                j=EpaperScreenManager.getEpaperScreenSize().getLittleWidth()-1-y;
                i=x;
                value=sourceRaster.getPixel(x,y,sourcePixel)[1];//1=canal vert
                pixel=value==128?Pixel.TRANSPARENT:value>0?Pixel.WHITE:Pixel.BLACK;
                index=j*EpaperScreenManager.getEpaperScreenSize().getLittleWidth()+i;
                address=index/4;
                offset=index%4;
                if(offset==0)
                    b=pixel.getByte();
                else
                    if(offset==1)
                        b=(byte)(pixel.getByte()<<2);
                    else
                        if(offset==2)
                            b=(byte)(pixel.getByte()<<4);
                        else
                            b=(byte)(pixel.getByte()<<6);
                data[address]|=b;
            }
    }

    public ScreenContent incrustTransparentImage(ScreenContent image)
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

    public RefreshType getRefreshType()
    {
        return refreshType;
    }

    public boolean isIOk(int i)
    {
        return i<EpaperScreenManager.getEpaperScreenSize().getLittleWidth();
    }

    public Pixel getPixel(int i,int j)
    {
        int index=i*EpaperScreenManager.getEpaperScreenSize().getLittleWidth()+j;
        int address=index/4;
        int offset=index%4;
        byte b=data[address];
        int value;
        if(offset==0)
            value=b&(byte)3;
        else
            if(offset==1)
                value=(b&(byte)12)>>2;
            else
                if(offset==3)
                    value=(b&(byte)48)>>4;
                else
                    value=(b&(byte)192)>>6;
        return value==0?Pixel.BLACK:value==1?Pixel.WHITE:Pixel.TRANSPARENT;
    }
}
