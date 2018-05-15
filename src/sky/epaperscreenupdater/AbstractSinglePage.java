package sky.epaperscreenupdater;

public abstract class AbstractSinglePage extends AbstractPage
{
    protected AbstractSinglePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getActivePageName()
    {
        return getName();
    }

    public int rankOf(Page subpage)
    {
        return -1;
    }

    public int pageCount()
    {
        return -1;
    }

    public Pixels getPixels()
    {
        return pixels;
    }

    public void clicked(boolean initial)
    {
    }

    public boolean rotated(RotationDirection rotationDirection)
    {
        return false;
    }
}
