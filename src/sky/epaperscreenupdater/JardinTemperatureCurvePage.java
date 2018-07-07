package sky.epaperscreenupdater;

import sky.program.Duration;

public class JardinTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public JardinTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température jardin";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(3).second();
    }

    protected String getMeasureMapKey()
    {
        return JARDIN_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. jardin (°C)";
    }

    protected double getMinimalYRange()
    {
        return 5d;
    }

    protected String getVerificationFileName()
    {
        return "courbejt.png";
    }

    public static void main(String[] args)
    {
        new JardinTemperatureCurvePage(null).potentiallyUpdate();
    }
}
