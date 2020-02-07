package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _030000000216TemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public _030000000216TemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température filles";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(18).second();
    }

    protected String getMeasureKind()
    {
        return _030000000216_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. chambre (°C)";
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
        return "courbect.png";
    }

    public static void main(String[] args)
    {
        new _030000000216TemperatureCurvePage(null).potentiallyUpdate();
    }
}
