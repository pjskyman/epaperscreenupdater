package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;

public class Screen
{
    private PixelMatrix validatedPixelMatrix;
    private PixelMatrix workingPixelMatrix;
    private final Object lockObject;

    public Screen()
    {
        workingPixelMatrix=new PixelMatrix();
        validatedPixelMatrix=null;
        lockObject=new Object();
    }

    public Screen initializeBlank()
    {
        synchronized(lockObject)
        {
            workingPixelMatrix.initializeBlank();
            validatedPixelMatrix=workingPixelMatrix;
            workingPixelMatrix=new PixelMatrix();
        }
        return this;
    }

    public Screen initializeTransparent()
    {
        synchronized(lockObject)
        {
            workingPixelMatrix.initializeTransparent();
            validatedPixelMatrix=workingPixelMatrix;
            workingPixelMatrix=new PixelMatrix();
        }
        return this;
    }

    public synchronized Screen setImage(BufferedImage image)
    {
        synchronized(lockObject)
        {
            workingPixelMatrix.setImage(image);
            validatedPixelMatrix=workingPixelMatrix;
            workingPixelMatrix=new PixelMatrix();
        }
        return this;
    }

    public PixelMatrix getPixelMatrix()
    {
        synchronized(lockObject)
        {
            return validatedPixelMatrix;
        }
    }
}
