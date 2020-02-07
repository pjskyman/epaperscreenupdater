package sky.epaperscreenupdater;

import sky.program.Duration;

public class _70ee50000deaTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public _70ee50000deaTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température salon";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(8).second();
    }

    protected String getMeasureKind()
    {
        return _70ee50000dea_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. salon (°C)";
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
        return "courbest.png";
    }

    public static void main(String[] args)
    {
        new _70ee50000deaTemperatureCurvePage(null).potentiallyUpdate();
    }
}
