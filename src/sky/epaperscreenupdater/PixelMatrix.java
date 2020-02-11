package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class PixelMatrix
{
    private final byte[] data;

    public PixelMatrix()
    {
        data=new byte[EpaperScreenManager.LITTLE_WIDTH*EpaperScreenManager.BIG_HEIGHT/4];
    }

    public void initializeBlank()
    {
        initialize(PixelState.WHITE);
    }

    public void initializeTransparent()
    {
        initialize(PixelState.TRANSPARENT);
    }

    private void initialize(PixelState pixelState)
    {
        int i;
        int j;
        int index;
        int address;
        int offset;
        byte b;
        for(int y=0;y<EpaperScreenManager.LITTLE_WIDTH;y++)
            for(int x=0;x<EpaperScreenManager.BIG_HEIGHT;x++)
            {
                index=y*EpaperScreenManager.BIG_HEIGHT+x;
                address=index/4;
                offset=index%4;
                if(offset==0)
                    b=pixelState.getByte();
                else
                    if(offset==1)
                        b=(byte)(pixelState.getByte()<<2);
                    else
                        if(offset==2)
                            b=(byte)(pixelState.getByte()<<4);
                        else
                            b=(byte)(pixelState.getByte()<<6);
                data[address]|=b;
            }
    }

    public void setImage(BufferedImage image)
    {
        if(image.getWidth()!=EpaperScreenManager.BIG_HEIGHT||image.getHeight()!=EpaperScreenManager.LITTLE_WIDTH)
            throw new IllegalArgumentException("Image has wrong dimensions");
        WritableRaster sourceRaster=image.getRaster();
        int i;
        int j;
        int[] sourcePixel=new int[4];
        int value;
        PixelState pixelState;
        int index;
        int address;
        int offset;
        byte b;
        for(int y=0;y<EpaperScreenManager.LITTLE_WIDTH;y++)
            for(int x=0;x<EpaperScreenManager.BIG_HEIGHT;x++)
            {
                value=sourceRaster.getPixel(x,y,sourcePixel)[1];//1=canal vert
                pixelState=value==128?PixelState.TRANSPARENT:value>0?PixelState.WHITE:PixelState.BLACK;
                index=y*EpaperScreenManager.BIG_HEIGHT+x;
                address=index/4;
                offset=index%4;
                if(offset==0)
                    b=pixelState.getByte();
                else
                    if(offset==1)
                        b=(byte)(pixelState.getByte()<<2);
                    else
                        if(offset==2)
                            b=(byte)(pixelState.getByte()<<4);
                        else
                            b=(byte)(pixelState.getByte()<<6);
                data[address]|=b;
            }
    }

    public PixelMatrix createMergedPixelMatrix(PixelMatrix pixelMatrix)
    {
        PixelMatrix newPixelMatrix=new PixelMatrix();
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
            b1=pixelMatrix.data[address];
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
            newPixelMatrix.data[address]=(byte)(i34<<6|i33<<4|i32<<2|i31);
        }
        return newPixelMatrix;
    }

    public boolean arePixelsEqual(PixelMatrix anotherPixelMatrix,int x,int y)
    {
        int index=y*EpaperScreenManager.BIG_HEIGHT+x;
        int address=index/4;
        int offset=index%4;
        byte b1=data[address];
        byte b2=anotherPixelMatrix.data[address];
        int value1;
        int value2;
        if(offset==0)
        {
            value1=b1&(byte)3;
            value2=b2&(byte)3;
        }
        else
            if(offset==1)
            {
                value1=(b1&(byte)12)>>2;
                value2=(b2&(byte)12)>>2;
            }
            else
                if(offset==3)
                {
                    value1=(b1&(byte)48)>>4;
                    value2=(b2&(byte)48)>>4;
                }
                else
                {
                    value1=(b1&(byte)192)>>6;
                    value2=(b2&(byte)192)>>6;
                }
        return value1==value2;
    }

    public PixelState getPixelState(int x,int y)
    {
        int index=y*EpaperScreenManager.BIG_HEIGHT+x;
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
        return value==0?PixelState.BLACK:value==1?PixelState.WHITE:PixelState.TRANSPARENT;
    }
}
