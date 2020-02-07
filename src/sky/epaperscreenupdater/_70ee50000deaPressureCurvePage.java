package sky.epaperscreenupdater;

import sky.program.Duration;

public class _70ee50000deaPressureCurvePage extends AbstractNetatmoCurvePage
{
    public _70ee50000deaPressureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe pression salon";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(12).second();
    }

    protected String getMeasureKind()
    {
        return _70ee50000dea_PRESSURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Press. salon (hPa)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_PRESSURE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_PRESSURE_MINIMAL_Y;
    }

    protected String getDebugImageFileName()
    {
        return "courbesp.png";
    }

    public static void main(String[] args)
    {
        new _70ee50000deaPressureCurvePage(null).potentiallyUpdate();
    }
}
