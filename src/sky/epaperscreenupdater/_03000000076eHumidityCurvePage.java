package sky.epaperscreenupdater;

import sky.program.Duration;

public class _03000000076eHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public _03000000076eHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité garage";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(30).second();
    }

    protected String getMeasureKind()
    {
        return _03000000076e_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. SdB (%)";
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
        return "courbesdbh.png";
    }

    public static void main(String[] args)
    {
        new _03000000076eHumidityCurvePage(null).potentiallyUpdate();
    }
}
