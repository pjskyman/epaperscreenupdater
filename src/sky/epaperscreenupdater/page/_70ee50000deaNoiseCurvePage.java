package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class _70ee50000deaNoiseCurvePage extends AbstractNetatmoCurvePage
{
    public _70ee50000deaNoiseCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe bruit salon";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(17).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._70ee50000dea_NOISE;
    }

    protected String getOrdinateLabelText()
    {
        return "Bruit salon (dB)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_NOISE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_NOISE_MINIMAL_Y;
    }

    protected String getDebugImageFileName()
    {
        return "courbesn.png";
    }

    public static void main(String[] args)
    {
        new _70ee50000deaNoiseCurvePage(null).potentiallyUpdate();
    }
}
