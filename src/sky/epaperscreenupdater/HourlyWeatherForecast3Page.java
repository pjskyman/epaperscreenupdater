package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast3Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast3Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H +14h";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(20).second();
    }

    protected int getHourStartOffset()
    {
        return 14;
    }

    protected String getVerificationFileName()
    {
        return "weather5.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast3Page(null).potentiallyUpdate();
    }
}
