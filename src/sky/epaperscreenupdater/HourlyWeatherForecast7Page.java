package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast7Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast7Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H +42h";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(37).second();
    }

    protected int getHourStartOffset()
    {
        return 42;
    }

    protected String getVerificationFileName()
    {
        return "weather9.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast7Page(null).potentiallyUpdate();
    }
}
