package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalonNoiseCurvePage extends AbstractNetatmoCurvePage
{
    public SalonNoiseCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe bruit salon";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(17).second();
    }

    protected String getMeasureMapKey()
    {
        return SALON_NOISE;
    }

    protected String getOrdinateLabelText()
    {
        return "Bruit salon (dB)";
    }

    protected String getVerificationFileName()
    {
        return "courbesn.png";
    }

    public static void main(String[] args)
    {
        new SalonNoiseCurvePage(null).potentiallyUpdate();
    }
}
