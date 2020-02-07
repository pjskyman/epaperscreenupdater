package sky.epaperscreenupdater.page;

public class _03000003fe8eCurveMenuPage extends AbstractMenuPage
{
    public _03000003fe8eCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new _03000003fe8eTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new _03000003fe8eHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new _03000003fe8eCarbonDioxydeCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes PJ";
    }
}
