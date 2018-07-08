package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalonCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public SalonCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 salon";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(15).second();
    }

    protected String getMeasureMapKey()
    {
        return SALON_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 salon (ppm)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_CARBON_DIOXYDE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_CARBON_DIOXYDE_MINIMAL_Y;
    }

    protected String getVerificationFileName()
    {
        return "courbescd.png";
    }

    public static void main(String[] args)
    {
        new SalonCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
