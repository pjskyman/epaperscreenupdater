package sky.epaperscreenupdater;

import sky.program.Duration;

public class ChambreHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public ChambreHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité chambre";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(20).second();
    }

    protected String getMeasureKind()
    {
        return CHAMBRE_HUMIDITY;
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

    protected String getVerificationFileName()
    {
        return "courbech.png";
    }

    public static void main(String[] args)
    {
        new ChambreHumidityCurvePage(null).potentiallyUpdate();
    }
}
