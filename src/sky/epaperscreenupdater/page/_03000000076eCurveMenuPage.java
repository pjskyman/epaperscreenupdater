package sky.epaperscreenupdater.page;

public class _03000000076eCurveMenuPage extends AbstractMenuPage
{
    public _03000000076eCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new _03000000076eTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new _03000000076eHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new _03000000076eCarbonDioxydeCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes garage";
    }
}
