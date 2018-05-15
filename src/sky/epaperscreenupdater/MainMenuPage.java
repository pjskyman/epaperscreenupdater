package sky.epaperscreenupdater;

public class MainMenuPage extends AbstractMenuPage
{
    public MainMenuPage()
    {
        super(null);
        subpages.add(new InstantaneousConsumptionPage(this).potentiallyUpdate());
        subpages.add(new WeatherMenuPage(this).potentiallyUpdate());
        subpages.add(new ElectricityMenuPage(this).potentiallyUpdate());
        subpages.add(new ClockMenuPage(this).potentiallyUpdate());
        subpages.add(new RERCPage(this).potentiallyUpdate());
        subpages.add(new MoonPage(this).potentiallyUpdate());
        subpages.add(new AnniversaryPage(this).potentiallyUpdate());
        currentPageRank=1;
    }

    public String getName()
    {
        return "Menu principal";
    }

    @Override
    public void clicked(boolean initial)
    {
        if(currentlySelectedPageRank==-1)
            subpages.get(currentPageRank-1).clicked(false);
        else
        {
            currentPageRank=currentlySelectedPageRank;
            currentlySelectedPageRank=-1;
            subpages.get(currentPageRank-1).clicked(true);
        }
    }

    @Override
    public boolean rotated(RotationDirection rotationDirection)
    {
        if(subpages.get(currentPageRank-1).rotated(rotationDirection))
            return true;
        currentlySelectedPageRank=((currentlySelectedPageRank==-1?currentPageRank:currentlySelectedPageRank)-1+(rotationDirection==RotationDirection.CLOCKWISE?1:-1)+subpages.size())%subpages.size()+1;
        return true;
    }
}
