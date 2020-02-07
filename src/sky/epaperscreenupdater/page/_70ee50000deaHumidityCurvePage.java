package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _70ee50000deaHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public _70ee50000deaHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité salon";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(11).second();
    }

    protected String getMeasureKind()
    {
        return _70ee50000dea_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. salon (%)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_HUMIDITY_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_HUMIDITY_MINIMAL_Y;
    }

    protected String getDebugImageFileName()
    {
        return "courbesh.png";
    }

    public static void main(String[] args)
    {
        new _70ee50000deaHumidityCurvePage(null).potentiallyUpdate();
    }
}
