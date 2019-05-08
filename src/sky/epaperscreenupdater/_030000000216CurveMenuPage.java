package sky.epaperscreenupdater;

public class _030000000216CurveMenuPage extends AbstractMenuPage
{
    public _030000000216CurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new _030000000216TemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new _030000000216HumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new _030000000216CarbonDioxydeCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes filles";
    }
}
