package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _03000003fe8eHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public _03000003fe8eHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité PJ";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(35).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._03000003fe8e_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. SS (%)";
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
        return "courbessh.png";
    }

    public static void main(String[] args)
    {
        new _03000003fe8eHumidityCurvePage(null).potentiallyUpdate();
    }
}
