package sky.epaperscreenupdater;

import sky.program.Duration;

public class ChambreTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public ChambreTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température chambre";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(18).second();
    }

    protected String getMeasureMapKey()
    {
        return CHAMBRE_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. chambre (°C)";
    }

    protected String getVerificationFileName()
    {
        return "courbect.png";
    }

    public static void main(String[] args)
    {
        new ChambreTemperatureCurvePage(null).potentiallyUpdate();
    }
}
