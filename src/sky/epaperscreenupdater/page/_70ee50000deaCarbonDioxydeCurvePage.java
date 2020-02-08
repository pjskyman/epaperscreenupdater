package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _70ee50000deaCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public _70ee50000deaCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 salon";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(15).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._70ee50000dea_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 salon (ppm)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_CARBON_DIOXYDE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_CARBON_DIOXYDE_MINIMAL_Y;
    }

    protected String getDebugImageFileName()
    {
        return "courbescd.png";
    }

    public static void main(String[] args)
    {
        new _70ee50000deaCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
