package sky.epaperscreenupdater.page;

public class AstronomyMenuPage extends AbstractMenuPage
{
    public AstronomyMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new MoonPage(this).potentiallyUpdate());
        subpages.add(new IridiumFlareForecastPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Astronomie";
    }
}
