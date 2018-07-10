package sky.epaperscreenupdater;

import sky.program.Duration;

public class SousSolHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public SousSolHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité sous-sol";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(35).second();
    }

    protected String getMeasureKind()
    {
        return SOUS_SOL_HUMIDITY;
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

    protected String getVerificationFileName()
    {
        return "courbessh.png";
    }

    public static void main(String[] args)
    {
        new SousSolHumidityCurvePage(null).potentiallyUpdate();
    }
}
