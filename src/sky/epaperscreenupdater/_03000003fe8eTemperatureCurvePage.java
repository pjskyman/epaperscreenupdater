package sky.epaperscreenupdater;

import sky.program.Duration;

public class _03000003fe8eTemperatureCurvePage extends AbstractNetatmoCurvePage
{
    public _03000003fe8eTemperatureCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe température PJ";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(34).second();
    }

    protected String getMeasureKind()
    {
        return _03000003fe8e_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Temp. SS (°C)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y;
    }

    protected String getVerificationFileName()
    {
        return "courbesst.png";
    }

    public static void main(String[] args)
    {
        new _03000003fe8eTemperatureCurvePage(null).potentiallyUpdate();
    }
}
