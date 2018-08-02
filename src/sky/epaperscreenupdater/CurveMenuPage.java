package sky.epaperscreenupdater;

public class CurveMenuPage extends AbstractMenuPage
{
    public CurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new SalonCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new ChambreCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new SalleDeBainCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new SousSolCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new JardinCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new TemperatureComparisonPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes Netatmo";
    }
}
