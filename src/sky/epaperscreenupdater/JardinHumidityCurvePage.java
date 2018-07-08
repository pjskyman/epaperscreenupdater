package sky.epaperscreenupdater;

import sky.program.Duration;

public class JardinHumidityCurvePage extends AbstractNetatmoCurvePage
{
    public JardinHumidityCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe humidité jardin";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(5).second();
    }

    protected String getMeasureMapKey()
    {
        return JARDIN_HUMIDITY;
    }

    protected String getOrdinateLabelText()
    {
        return "Hum. jardin (%)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_HUMIDITY_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_HUMIDITY_MINIMAL_Y;
    }

    protected String getVerificationFileName()
    {
        return "courbejh.png";
    }

    public static void main(String[] args)
    {
        new JardinHumidityCurvePage(null).potentiallyUpdate();
    }
}
