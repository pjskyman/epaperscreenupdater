package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ScreenContent
{
    private final byte[] data;
    private final RefreshType refreshType;
    private final Object lockObject;

    public ScreenContent(RefreshType refreshType)
    {
        data=new byte[EpaperScreenManager.getEpaperScreenSize().getLittleWidth()*EpaperScreenManager.getEpaperScreenSize().getBigHeight()/4];
        this.refreshType=refreshType;
        lockObject=new Object();
    }

    public void writeImage(BufferedImage image)
    {
        synchronized(lockObject)
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
    }

    public ScreenContent createMergedScreenContent(ScreenContent screenContent)
    {
        synchronized(lockObject)
        {
            ScreenContent newScreenContent=new ScreenContent(refreshType.combine(screenContent.refreshType));
            byte b1;
            byte b2;
            int i11;
            int i21;
            int i31;
            int i12;
            int i22;
            int i32;
            int i13;
            int i23;
            int i33;
            int i14;
            int i24;
            int i34;
            for(int address=0;address<data.length;address++)
            {
                b1=screenContent.data[address];
                b2=data[address];
                i11=b1&(byte)3;
                i21=b2&(byte)3;
                i12=(b1&(byte)12)>>2;
                i22=(b2&(byte)12)>>2;
                i13=(b1&(byte)48)>>4;
                i23=(b2&(byte)48)>>4;
                i14=(b1&(byte)192)>>6;
                i24=(b2&(byte)192)>>6;
                if(i11<2)
                    i31=i11;
                else
                    i31=i21;
                if(i12<2)
                    i32=i12;
                else
                    i32=i22;
                if(i13<2)
                    i33=i13;
                else
                    i33=i23;
                if(i14<2)
                    i34=i14;
                else
                    i34=i24;
                newScreenContent.data[address]=(byte)(i34<<6|i33<<4|i32<<2|i31);
            }
            return newScreenContent;
        }
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
        synchronized(lockObject)
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
}
