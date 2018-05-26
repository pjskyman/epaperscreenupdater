package sky.epaperscreenupdater;

import sky.program.Duration;

public class AnemometreWindCurvePage extends AbstractNetatmoCurvePage
{
    public AnemometreWindCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe vent";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(43).second();
    }

    protected String getMeasureMapKey()
    {
        return ANEMOMETRE_GUST_STRENGTH;
    }

    protected String getVerificationFileName()
    {
        return "courbeaw.png";
    }

    public static void main(String[] args)
    {
        new AnemometreWindCurvePage(null).potentiallyUpdate();
    }
}
