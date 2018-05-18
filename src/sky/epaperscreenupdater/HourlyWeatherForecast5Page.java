package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast5Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast5Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H +28h";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(23).second();
    }

    protected int getHourStartOffset()
    {
        return 28;
    }

    protected String getVerificationFileName()
    {
        return "weather7.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast5Page(null).potentiallyUpdate();
    }
}
