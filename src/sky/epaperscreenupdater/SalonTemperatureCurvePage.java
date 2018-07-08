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
        return Duration.of(1).minutePlus(8).second();
    }

    protected String getMeasureMapKey()
    {
        return SALON_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. salon (°C)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y;
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
