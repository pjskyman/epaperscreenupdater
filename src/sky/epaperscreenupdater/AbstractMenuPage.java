package sky.epaperscreenupdater;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMenuPage implements Page
{
    private final Page parentPage;
    protected final List<Page> subpages;
    private int currentlySelectedPageRank;

    public AbstractMenuPage(Page parentPage)
    {
        this.parentPage=parentPage;
        subpages=new ArrayList<>();
        currentlySelectedPageRank=-1;
    }

    public Page getParentPage()
    {
        return parentPage;
    }

    public int rankOf(Page subpage)
    {
        if(subpages.contains(subpage))
            return subpages.indexOf(subpage)+1;
        else
            return -1;
    }

    public int pageCount()
    {
        return subpages.size();
    }

    public Page potentiallyUpdate()
    {
        subpages.forEach(page->page.potentiallyUpdate());
        return this;
    }

    public Pixels getPixels()
    {
    }

    public boolean hasHighFrequency()
    {
        return false;
    }

    public Page clicked()
    {
        if(currentlySelectedPageRank==0)//on a sélectionné le menu lui-même, on veut donc revenir
        if(currentlySelectedPageRank==-1)
            currentlySelectedPageRank=1;
        else
    }

    public void rotated(RotaryEvent rotaryEvent)
    {
    }
}
