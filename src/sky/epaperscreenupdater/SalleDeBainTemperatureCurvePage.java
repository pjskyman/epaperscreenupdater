package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalleDeBainTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public SalleDeBainTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température salle de bain";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(9).minutePlus(27).second();
    }

    protected String getMeasureMapKey()
    {
        return SALLE_DE_BAIN_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. SdB (°C)";
    }

    protected String getVerificationFileName()
    {
        return "courbesdbt.png";
    }

    public static void main(String[] args)
    {
        new SalleDeBainTemperatureCurvePage(null).potentiallyUpdate();
    }
}
