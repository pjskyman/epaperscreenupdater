package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _030000000216CarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public _030000000216CarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 filles";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(21).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._030000000216_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 chambre (ppm)";
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
        return "courbeccd.png";
    }

    public static void main(String[] args)
    {
        new _030000000216CarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
