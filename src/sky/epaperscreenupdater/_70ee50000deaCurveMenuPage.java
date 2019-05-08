package sky.epaperscreenupdater;

public class _70ee50000deaCurveMenuPage extends AbstractMenuPage
{
    public _70ee50000deaCurveMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new _70ee50000deaTemperatureCurvePage(this).potentiallyUpdate());
        subpages.add(new _70ee50000deaHumidityCurvePage(this).potentiallyUpdate());
        subpages.add(new _70ee50000deaPressureCurvePage(this).potentiallyUpdate());
        subpages.add(new _70ee50000deaCarbonDioxydeCurvePage(this).potentiallyUpdate());
        subpages.add(new _70ee50000deaNoiseCurvePage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Courbes salon";
    }
}
