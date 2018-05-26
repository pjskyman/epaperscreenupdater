package sky.epaperscreenupdater;

import sky.program.Duration;

public class PluviometreRainCurvePage extends AbstractNetatmoCurvePage
{
    public PluviometreRainCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe pluie";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(41).second();
    }

    protected String getMeasureMapKey()
    {
        return PLUVIOMETRE_RAIN;
    }

    protected String getVerificationFileName()
    {
        return "courbepr.png";
    }

    public static void main(String[] args)
    {
        new PluviometreRainCurvePage(null).potentiallyUpdate();
    }
}
