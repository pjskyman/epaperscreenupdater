package sky.epaperscreenupdater;

import sky.program.Duration;

public class ChambreCarbonDioxydeCurvePage extends AbstractNetatmoCurvePage
{
    public ChambreCarbonDioxydeCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe CO2 chambre";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(21).second();
    }

    protected String getMeasureMapKey()
    {
        return CHAMBRE_CARBON_DIOXYDE;
    }

    protected String getVerificationFileName()
    {
        return "courbeccd.png";
    }

    public static void main(String[] args)
    {
        new ChambreCarbonDioxydeCurvePage(null).potentiallyUpdate();
    }
}
