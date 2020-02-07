package sky.epaperscreenupdater;

import sky.program.Duration;

public class _03000000076eCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public _03000000076eCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 garage";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(32).second();
    }

    protected String getMeasureKind()
    {
        return _03000000076e_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 SdB (ppm)";
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
        return "courbesdbcd.png";
    }

    public static void main(String[] args)
    {
        new _03000000076eCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
