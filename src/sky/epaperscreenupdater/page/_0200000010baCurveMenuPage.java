package sky.epaperscreenupdater.page;

public class _0200000010baCurveMenuPage extends AbstractMenuPage
{
    public _0200000010baCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new _0200000010baTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new _0200000010baHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new _05000004152cRainCurvePage(this).potentiallyUpdate());
        subpages.add(new _06000000729aWindCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes jardin";
    }
}
