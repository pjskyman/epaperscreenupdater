package sky.epaperscreenupdater;

import sky.program.Duration;

public class HourlyWeatherForecast1Page extends AbstractHourlyWeatherForecastPage
{
    public HourlyWeatherForecast1Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo H/H";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(4).minutePlus(17).second();
    }

    protected int getHourStartOffset()
    {
        return 0;
    }

    protected String getVerificationFileName()
    {
        return "weather3.png";
    }

    public static void main(String[] args)
    {
        new HourlyWeatherForecast1Page(null).potentiallyUpdate();
    }
}
