package sky.epaperscreenupdater;

public class SousSolCurveMenuPage extends AbstractMenuPage
{
    public SousSolCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new SousSolTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new SousSolHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new SousSolCarbonDioxydeCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes sous-sol";
    }
}
