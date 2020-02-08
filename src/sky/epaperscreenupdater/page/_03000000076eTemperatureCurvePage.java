package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _03000000076eTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public _03000000076eTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température garage";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(27).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._03000000076e_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. SdB (°C)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y;
    }

    protected String getDebugImageFileName()
    {
        return "courbesdbt.png";
    }

    public static void main(String[] args)
    {
        new _03000000076eTemperatureCurvePage(null).potentiallyUpdate();
    }
}
