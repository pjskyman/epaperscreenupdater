package sky.epaperscreenupdater;

import sky.program.Duration;

public class _030000000216HumidityCurvePage extends AbstractNetatmoCurvePage
{
    public _030000000216HumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité filles";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(20).second();
    }

    protected String getMeasureKind()
    {
        return _030000000216_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. chambre (%)";
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
        return "courbech.png";
    }

    public static void main(String[] args)
    {
        new _030000000216HumidityCurvePage(null).potentiallyUpdate();
    }
}
