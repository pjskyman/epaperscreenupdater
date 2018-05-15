package sky.epaperscreenupdater;

public abstract class AbstractPage implements Page
{
    protected Pixels pixels;
    protected final Page parentPage;

    protected AbstractPage(Page parentPage)
    {
        pixels=new Pixels(RefreshType.PARTIAL_REFRESH);
        this.parentPage=parentPage;
    }

    public Page getParentPage()
    {
        return parentPage;
    }
}
