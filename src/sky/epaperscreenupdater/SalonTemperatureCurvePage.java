package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalonTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public SalonTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température salon";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(8).second();
    }

    protected String getMeasureMapKey()
    {
        return SALON_TEMPERATURE;
    }

    protected String getVerificationFileName()
    {
        return "courbest.png";
    }

    public static void main(String[] args)
    {
        new SalonTemperatureCurvePage(null).potentiallyUpdate();
    }
}
