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
        return Duration.of(9).minutePlus(32).second();
    }

    protected String getMeasureMapKey()
    {
        return SALLE_DE_BAIN_CARBON_DIOXYDE;
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
