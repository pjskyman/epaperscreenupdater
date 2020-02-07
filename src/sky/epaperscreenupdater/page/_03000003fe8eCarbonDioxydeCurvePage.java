package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _03000003fe8eCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public _03000003fe8eCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 PJ";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(40).second();
    }

    protected String getMeasureKind()
    {
        return _03000003fe8e_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 SS (ppm)";
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
        return "courbesscd.png";
    }

    public static void main(String[] args)
    {
        new _03000003fe8eCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
