package sky.epaperscreenupdater;

import java.awt.image.BufferedImage;

public class Screen
{
    private PixelMatrix workingPixelMatrix;
    private PixelMatrix validatedPixelMatrix;
    private int modificationCounter;
    private final Object lockObject;

    public Screen()
    {
        workingPixelMatrix=new PixelMatrix();
        validatedPixelMatrix=null;
        modificationCounter=0;
        lockObject=new Object();
    }

    public Screen initializeBlank()
    {
        synchronized(lockObject)
        {
            workingPixelMatrix.initializeBlank();
            validatedPixelMatrix=null;
            validatedPixelMatrix=workingPixelMatrix;
            modificationCounter++;
            workingPixelMatrix=new PixelMatrix();
        }
        return this;
    }

    public Screen initializeTransparent()
    {
        synchronized(lockObject)
        {
            workingPixelMatrix.initializeTransparent();
            validatedPixelMatrix=null;
            validatedPixelMatrix=workingPixelMatrix;
            modificationCounter++;
            workingPixelMatrix=new PixelMatrix();
        }
        return this;
    }

    public synchronized Screen setImage(BufferedImage image)
    {
        synchronized(lockObject)
        {
            workingPixelMatrix.setImage(image);
            validatedPixelMatrix=null;
            validatedPixelMatrix=workingPixelMatrix;
            modificationCounter++;
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

    public int getModificationCounter()
    {
        return modificationCounter;
    }
}
