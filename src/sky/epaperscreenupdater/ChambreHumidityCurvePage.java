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

    protected String getMeasureMapKey()
    {
        return CHAMBRE_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. chambre (%)";
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
