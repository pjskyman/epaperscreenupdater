package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalleDeBainHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public SalleDeBainHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité salle de bain";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(30).second();
    }

    protected String getMeasureMapKey()
    {
        return SALLE_DE_BAIN_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. SdB (%)";
    }

    protected double getMinimalYRange()
    {
        return 10d;
    }

    protected double getMinimalY()
    {
        return 0d;
    }

    protected String getVerificationFileName()
    {
        return "courbesdbh.png";
    }

    public static void main(String[] args)
    {
        new SalleDeBainHumidityCurvePage(null).potentiallyUpdate();
    }
}
