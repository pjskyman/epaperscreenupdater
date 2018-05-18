package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast4Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast4Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H +21h";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(25).second();
    }

    protected int getHourStartOffset()
    {
        return 21;
    }

    protected String getVerificationFileName()
    {
        return "weather6.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast4Page(null).potentiallyUpdate();
    }
}
