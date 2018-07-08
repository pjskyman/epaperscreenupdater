package sky.epaperscreenupdater;

import sky.program.Duration;

public class SalonPressureCurvePage extends AbstractNetatmoCurvePage
{
    public SalonPressureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe pression salon";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(12).second();
    }

    protected String getMeasureMapKey()
    {
        return SALON_PRESSURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Press. salon (hPa)";
    }

    protected double getMinimalYRange()
    {
        return 2d;
    }

    protected double getMinimalY()
    {
        return 0d;
    }

    protected String getVerificationFileName()
    {
        return "courbesp.png";
    }

    public static void main(String[] args)
    {
        new SalonPressureCurvePage(null).potentiallyUpdate();
    }
}
