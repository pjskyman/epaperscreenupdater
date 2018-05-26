package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast6Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast6Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H +35h";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(35).second();
    }

    protected int getHourStartOffset()
    {
        return 35;
    }

    protected String getVerificationFileName()
    {
        return "weather8.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast6Page(null).potentiallyUpdate();
    }
}
