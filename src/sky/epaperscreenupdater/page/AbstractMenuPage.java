package sky.epaperscreenupdater.page;

import java.util.ArrayList;
import java.util.List;
import sky.epaperscreenupdater.BufferedScreen;
import sky.epaperscreenupdater.IncrustGenerator;
import sky.epaperscreenupdater.Logger;
import sky.epaperscreenupdater.RefreshType;
import sky.epaperscreenupdater.RotationDirection;

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
    private BufferedScreen cachedPageScreen;
    private BufferedScreen cachedSelectionIncrustScreen;
    private static final BufferedScreen BLANK_PIXELS=new BufferedScreen(RefreshType.PARTIAL_REFRESH).initializeBlank();

    protected AbstractMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages=new ArrayList<>();
        currentPageRank=-1;
        currentlySelectedPageRank=-1;
        cachedCurrentPageRank=-2;
        cachedCurrentlySelectedPageRank=-2;
        cachedPageScreen=new BufferedScreen(RefreshType.PARTIAL_REFRESH);
        cachedSelectionIncrustScreen=new IncrustGenerator(null).generateEmptyIncrust();
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

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    public synchronized Page potentiallyUpdate()
    {
        subpages.forEach(Page::potentiallyUpdate);//avant tout le reste, comme ça on pourra récupérer leurs nouveaux pixels le cas échéant
        BufferedScreen tempPageScreen=currentPageRank==-1?BLANK_PIXELS:subpages.get(currentPageRank-1).getScreen();
        if(currentPageRank!=cachedCurrentPageRank||currentlySelectedPageRank!=cachedCurrentlySelectedPageRank||tempPageScreen!=cachedPageScreen)
        {
            Logger.LOGGER.info("Menu \""+getName()+"\" needs to be updated");
            try
            {
                cachedCurrentPageRank=currentPageRank;
                if(currentlySelectedPageRank!=cachedCurrentlySelectedPageRank)
                {
                    cachedSelectionIncrustScreen=currentlySelectedPageRank==-1?new IncrustGenerator(null).generateEmptyIncrust():
                                                 currentlySelectedPageRank==0?new IncrustGenerator(this).generateOutMessageIncrust():new IncrustGenerator(subpages.get(currentlySelectedPageRank-1)).generateStandardIncrust();
                    cachedCurrentlySelectedPageRank=currentlySelectedPageRank;
                }
                cachedPageScreen=tempPageScreen;
                screen.
                screen=cachedPageScreen.incrustTransparentImage(cachedSelectionIncrustScreen);
                Logger.LOGGER.info("Menu \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error when updating menu \""+getName()+"\"");
                e.printStackTrace();
            }
        }
        return this;
    }

    public boolean clicked(boolean initial)
    {
        if(currentPageRank==-1)
        {
            currentPageRank=1;
            currentlySelectedPageRank=subpages.get(0).pageCount()==-1?-1:1;
            return false;
        }
        else
            if(currentlySelectedPageRank==-1)
            {
                if(subpages.get(currentPageRank-1).clicked(false))
                    currentlySelectedPageRank=currentPageRank;
                return false;
            }
            else
                if(currentlySelectedPageRank==0)
                {
                    currentPageRank=-1;
                    currentlySelectedPageRank=-1;
                    return true;
                }
                else
                {
                    currentPageRank=currentlySelectedPageRank;
                    currentlySelectedPageRank=-1;
                    subpages.get(currentPageRank-1).clicked(true);
                    return false;
                }
    }

    public boolean rotated(RotationDirection rotationDirection)
    {
        if(currentPageRank==-1)
            return false;
        if(currentPageRank!=-1&&subpages.get(currentPageRank-1).rotated(rotationDirection))
            return true;
        currentlySelectedPageRank=((currentlySelectedPageRank==-1?currentPageRank:currentlySelectedPageRank)+(rotationDirection==RotationDirection.CLOCKWISE?1:-1)+subpages.size()+1)%(subpages.size()+1);
        return true;
    }
}