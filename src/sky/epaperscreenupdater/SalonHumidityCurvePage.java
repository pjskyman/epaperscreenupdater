package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalonHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public SalonHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité salon";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(11).second();
    }

    protected String getMeasureMapKey()
    {
        return SALON_HUMIDITY;
    }

    protected String getVerificationFileName()
    {
        return "courbesh.png";
    }

    public static void main(String[] args)
    {
        new SalonHumidityCurvePage(null).potentiallyUpdate();
    }
}
