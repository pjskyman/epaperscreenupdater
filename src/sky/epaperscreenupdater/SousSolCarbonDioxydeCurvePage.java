package sky.epaperscreenupdater;

import sky.program.Duration;

public class SousSolCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public SousSolCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 sous-sol";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(40).second();
    }

    protected String getMeasureMapKey()
    {
        return SOUS_SOL_CARBON_DIOXYDE;
    }

    protected String getOrdinateLabelText()
    {
        return "CO2 SS (ppm)";
    }

    protected double getMinimalYRange()
    {
        return 500d;
    }

    protected double getMinimalY()
    {
        return 0d;
    }

    protected String getVerificationFileName()
    {
        return "courbesscd.png";
    }

    public static void main(String[] args)
    {
        new SousSolCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
