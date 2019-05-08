package sky.epaperscreenupdater;

import sky.program.Duration;

public class _0200000010baHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public _0200000010baHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité jardin";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(5).second();
    }

    protected String getMeasureKind()
    {
        return _0200000010ba_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. jardin (%)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_HUMIDITY_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_HUMIDITY_MINIMAL_Y;
    }

    protected String getVerificationFileName()
    {
        return "courbejh.png";
    }

    public static void main(String[] args)
    {
        new _0200000010baHumidityCurvePage(null).potentiallyUpdate();
    }
}
