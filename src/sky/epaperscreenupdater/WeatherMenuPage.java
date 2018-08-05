package sky.epaperscreenupdater;

public class WeatherMenuPage extends AbstractMenuPage
{
    public WeatherMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new HomeWeatherPage(this).potentiallyUpdate());
        subpages.add(new HomeWeatherVariationPage(this).potentiallyUpdate());
        subpages.add(new NetatmoModuleStatePage(this).potentiallyUpdate());
        subpages.add(new CurveMenuPage(this).potentiallyUpdate());
        subpages.add(new DailyWeatherForecast1Page(this).potentiallyUpdate());
        subpages.add(new DailyWeatherForecast2Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast1Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast2Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast3Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast4Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast5Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast6Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecast7Page(this).potentiallyUpdate());
        subpages.add(new CO2WeightPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Météo";
    }
}
