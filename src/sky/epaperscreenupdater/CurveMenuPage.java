package sky.epaperscreenupdater;

public class CurveMenuPage extends AbstractMenuPage
{
    public CurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new _70ee50000deaCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new _030000000216CurveMenuPage(this).potentiallyUpdate());
        subpages.add(new _03000000076eCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new _03000003fe8eCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new _0200000010baCurveMenuPage(this).potentiallyUpdate());
        subpages.add(new TemperatureComparisonPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes Netatmo";
    }
}
