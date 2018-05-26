package sky.epaperscreenupdater;

public class JardinCurveMenuPage extends AbstractMenuPage
{
    public JardinCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new JardinTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new JardinHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new PluviometreRainCurvePage(this).potentiallyUpdate());
        subpages.add(new AnemometreWindCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes jardin";
    }
}
