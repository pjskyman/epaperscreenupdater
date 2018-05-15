package sky.epaperscreenupdater;

import java.util.ArrayList;
import java.util.List;

/**
 * Squelette de menu. Un menu est avant tout une page, mais contient une
 * arborescence avec des sous-pages. Cette arborescence est théoriquement de
 * profondeur infinie.
 * @author PJ Skyman
 */
public abstract class AbstractMenuPage extends AbstractPage
{
    /**
     * Liste des sous-pages contenues dans ce menu.
     */
    protected final List<Page> subpages;
    /**
     * Rang de la sous-page actuelle.
     * <li>Vaut -1 pour indiquer qu'on est pas encore rentré dans le menu.
     * <li>Vaut un rang valide de sous-page pour indiquer qu'on affiche une
     * sous-page réelle.
     */
    protected int currentPageRank;
    /**
     * Rang de la sous-page actuellement sélectionnée.
     * <li>Vaut -1 pour indiquer qu'on ne sélectionne rien.
     * <li>Vaut 0 pour indiquer qu'on désire sortir du menu.
     * <li>Vaut un rang valide de sous-page pour indiquer qu'on a réellement
     * sélectionné une sous-page.
     */
    protected int currentlySelectedPageRank;
    private int cachedCurrentPageRank;
    private int cachedCurrentlySelectedPageRank;
    private Pixels cachedPagePixels;
    private Pixels cachedSelectionIncrustPixels;
    private static final Pixels BLANK_PIXELS=new Pixels(RefreshType.PARTIAL_REFRESH);

    protected AbstractMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages=new ArrayList<>();
        currentPageRank=-1;
        currentlySelectedPageRank=-1;
        cachedCurrentPageRank=-2;
        cachedCurrentlySelectedPageRank=-2;
        cachedPagePixels=new Pixels(RefreshType.PARTIAL_REFRESH);
        cachedSelectionIncrustPixels=new IncrustGenerator(null).generateEmptyIncrust();
    }

    public String getActivePageName()
    {
        if(currentPageRank==-1)
            return getName();
        else
            return subpages.get(currentPageRank-1).getActivePageName();
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

    public synchronized Page potentiallyUpdate()
    {
        subpages.forEach(page->page.potentiallyUpdate());//avant tout le reste, comme ça on pourra récupérer leurs nouveaux pixels le cas échéant
        Pixels tempPagePixels=currentPageRank==-1?BLANK_PIXELS:subpages.get(currentPageRank-1).getPixels();
        if(currentPageRank!=cachedCurrentPageRank||currentlySelectedPageRank!=cachedCurrentlySelectedPageRank||tempPagePixels!=cachedPagePixels)
            try
            {
                cachedCurrentPageRank=currentPageRank;
                if(currentlySelectedPageRank!=cachedCurrentlySelectedPageRank)
                {
                    cachedSelectionIncrustPixels=currentlySelectedPageRank==-1?new IncrustGenerator(null).generateEmptyIncrust():
                                                 currentlySelectedPageRank==0?new IncrustGenerator(this).generateOutMessageIncrust():new IncrustGenerator(subpages.get(currentlySelectedPageRank-1)).generateStandardIncrust();
                    cachedCurrentlySelectedPageRank=currentlySelectedPageRank;
                }
                cachedPagePixels=tempPagePixels;
                pixels=cachedPagePixels.incrustTransparentImage(cachedSelectionIncrustPixels);
                Logger.LOGGER.info("Menu "+getName()+" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        return this;
    }

    public Pixels getPixels()
    {
        return pixels;
    }

    public void clicked(boolean initial)
    {
        if(currentPageRank==-1)
        {
            currentPageRank=1;
            currentlySelectedPageRank=1;
        }
        else
            if(currentlySelectedPageRank==-1)
                subpages.get(currentPageRank-1).clicked(false);
            else
                if(currentlySelectedPageRank==0)
                {
                    currentPageRank=-1;
                    currentlySelectedPageRank=-1;
                }
                else
                {
                    currentPageRank=currentlySelectedPageRank;
                    currentlySelectedPageRank=-1;
                    subpages.get(currentPageRank-1).clicked(true);
                }
    }

    public boolean rotated(RotationDirection rotationDirection)
    {
        if(currentPageRank!=-1&&subpages.get(currentPageRank-1).rotated(rotationDirection))
            return true;
        currentlySelectedPageRank=((currentlySelectedPageRank==-1?currentPageRank:currentlySelectedPageRank)+(rotationDirection==RotationDirection.CLOCKWISE?1:-1)+subpages.size()+1)%(subpages.size()+1);
        return true;
    }
}
