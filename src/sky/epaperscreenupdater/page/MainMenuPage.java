package sky.epaperscreenupdater.page;

import sky.epaperscreenupdater.RotationDirection;

public class MainMenuPage extends AbstractMenuPage
{
    public MainMenuPage()
    {
        super(null);
        subpages.add(new InstantaneousConsumptionPage(this).potentiallyUpdate());
        subpages.add(new WeatherMenuPage(this).potentiallyUpdate());
        subpages.add(new ElectricityMenuPage(this).potentiallyUpdate());
//        subpages.add(new ClockMenuPage(this).potentiallyUpdate());
        subpages.add(new RERCPage(this).potentiallyUpdate());
//        subpages.add(new AstronomyMenuPage(this).potentiallyUpdate());
        subpages.add(new MoonPage(this).potentiallyUpdate());//il arrive là car le menu ci-dessus se retrouve avec un seul élément
        subpages.add(new AnniversaryPage(this).potentiallyUpdate());
        subpages.add(new AboutPage(this).potentiallyUpdate());
        currentPageRank=1;
    }

    public String getName()
    {
        return "Menu principal";
    }

    @Override
    public boolean clicked(boolean initial)
    {
        synchronized(lockObject)
        {
            if(currentlySelectedPageRank==-1)
            {
                if(subpages.get(currentPageRank-1).clicked(false))
                    currentlySelectedPageRank=currentPageRank;
                return false;
            }
            else
            {
                currentPageRank=currentlySelectedPageRank;
                currentlySelectedPageRank=-1;
                subpages.get(currentPageRank-1).clicked(true);
                return false;
            }
        }
    }

    @Override
    public boolean rotated(RotationDirection rotationDirection)
    {
        synchronized(lockObject)
        {
            if(subpages.get(currentPageRank-1).rotated(rotationDirection))
                return true;
            currentlySelectedPageRank=((currentlySelectedPageRank==-1?currentPageRank:currentlySelectedPageRank)-1+(rotationDirection==RotationDirection.CLOCKWISE?1:-1)+subpages.size())%subpages.size()+1;
            return true;
        }
    }
}
