package sky.epaperscreenupdater;

public abstract class AbstractPage implements Page
{
    protected final BufferedScreen screen;
    protected final Page parentPage;

    protected AbstractPage(Page parentPage)
    {
        screen=new BufferedScreen(getRefreshType());
        this.parentPage=parentPage;
    }

    protected abstract RefreshType getRefreshType();

    public Page getParentPage()
    {
        return parentPage;
    }

    public BufferedScreen getScreen()
    {
        return screen;
    }
}
