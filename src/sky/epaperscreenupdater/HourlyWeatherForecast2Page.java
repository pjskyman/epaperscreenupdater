package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast2Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast2Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H +7h";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(6).second();
    }

    protected int getHourStartOffset()
    {
        return 7;
    }

    protected String getVerificationFileName()
    {
        return "weather4.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast2Page(null).potentiallyUpdate();
    }
}
