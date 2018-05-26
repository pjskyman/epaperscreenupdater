package sky.epaperscreenupdater;

public class SalleDeBainCurveMenuPage extends AbstractMenuPage
{
    public SalleDeBainCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new SalleDeBainTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new SalleDeBainHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new SalleDeBainCarbonDioxydeCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes salle de bain";
    }
}
