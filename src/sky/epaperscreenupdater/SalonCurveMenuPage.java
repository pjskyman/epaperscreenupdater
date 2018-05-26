package sky.epaperscreenupdater;

public class SalonCurveMenuPage extends AbstractMenuPage
{
    public SalonCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new SalonTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new SalonHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new SalonPressureCurvePage(this).potentiallyUpdate());
        subpages.add(new SalonCarbonDioxydeCurvePage(this).potentiallyUpdate());
        subpages.add(new SalonNoiseCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes salon";
    }
}
