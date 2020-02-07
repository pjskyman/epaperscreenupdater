package sky.epaperscreenupdater.page;

import sky.program.Duration;

public class WeatherMenuPage extends AbstractMenuPage
{
    public WeatherMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new CurveMenuPage(this).potentiallyUpdate());
        subpages.add(new HomeWeatherPage(this).potentiallyUpdate());
        subpages.add(new HomeWeatherVariationPage(this).potentiallyUpdate());
        subpages.add(new NetatmoModuleStatePage(this).potentiallyUpdate());
        subpages.add(new DailyWeatherForecast1Page(this).potentiallyUpdate());
        subpages.add(new DailyWeatherForecast2Page(this).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,1,Duration.of(4).minutePlus(17).second()).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,2,Duration.of(4).minutePlus(6).second()).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,3,Duration.of(4).minutePlus(20).second()).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,4,Duration.of(4).minutePlus(25).second()).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,5,Duration.of(4).minutePlus(23).second()).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,6,Duration.of(4).minutePlus(35).second()).potentiallyUpdate());
        subpages.add(new HourlyWeatherForecastPage(this,7,Duration.of(4).minutePlus(37).second()).potentiallyUpdate());
        subpages.add(new CarbonDioxydeWeightPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Météo";
    }
}
