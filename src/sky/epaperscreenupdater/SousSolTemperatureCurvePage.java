package sky.epaperscreenupdater;

import sky.program.Duration;

public class SousSolTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public SousSolTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température sous-sol";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(34).second();
    }

    protected String getMeasureMapKey()
    {
        return SOUS_SOL_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. SS (°C)";
    }

    protected String getVerificationFileName()
    {
        return "courbesst.png";
    }

    public static void main(String[] args)
    {
        new SousSolTemperatureCurvePage(null).potentiallyUpdate();
    }
}
