package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _0200000010baTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public _0200000010baTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température jardin";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(3).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._0200000010ba_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. jardin (°C)";
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
        return "courbejt.png";
    }

    public static void main(String[] args)
    {
        new _0200000010baTemperatureCurvePage(null).potentiallyUpdate();
    }
}
