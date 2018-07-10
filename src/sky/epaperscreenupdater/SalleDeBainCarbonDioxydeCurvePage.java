package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalleDeBainCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public SalleDeBainCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 salle de bain";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(32).second();
    }

    protected String getMeasureKind()
    {
        return SALLE_DE_BAIN_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 SdB (ppm)";
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
        return "courbesdbcd.png";
    }

    public static void main(String[] args)
    {
        new SalleDeBainCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
