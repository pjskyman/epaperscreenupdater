package sky.epaperscreenupdater;

public class ChambreCurveMenuPage extends AbstractMenuPage
{
    public ChambreCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new ChambreTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new ChambreHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new ChambreCarbonDioxydeCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes chambre";
    }
}
