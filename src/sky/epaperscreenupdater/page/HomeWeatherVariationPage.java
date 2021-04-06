package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Date;
import java.util.Map;
import sky.housecommon.Database;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementType;
import sky.program.Duration;

public class HomeWeatherVariationPage extends AbstractSinglePage
{
    public HomeWeatherVariationPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Données météo (var./tot.)";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(4).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Map<String,Measure[]> lastMeasures=NetatmoUtils.getLastMeasures();
        Measure[] last70ee50000deaTemperatures=lastMeasures.get(NetatmoUtils._70ee50000dea_TEMPERATURE);
        double _70ee50000deaTemperatureVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last70ee50000deaTemperatures,1));
        Measure[] last70ee50000deaHumidities=lastMeasures.get(NetatmoUtils._70ee50000dea_HUMIDITY);
        double _70ee50000deaHumidityVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last70ee50000deaHumidities,1));
        Measure[] last70ee50000deaPressures=lastMeasures.get(NetatmoUtils._70ee50000dea_PRESSURE);
        double _70ee50000deaPressureVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last70ee50000deaPressures,2));
        Measure[] last70ee50000deaCarbonDioxydes=lastMeasures.get(NetatmoUtils._70ee50000dea_CARBON_DIOXYDE);
        double _70ee50000deaCarbonDioxydeVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last70ee50000deaCarbonDioxydes,1));
        Measure[] last70ee50000deaNoises=lastMeasures.get(NetatmoUtils._70ee50000dea_NOISE);
        double _70ee50000deaNoiseVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last70ee50000deaNoises,1));
        Measure[] last030000000216Temperatures=lastMeasures.get(NetatmoUtils._030000000216_TEMPERATURE);
        double _030000000216TemperatureVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last030000000216Temperatures,1));
        Measure[] last030000000216Humidities=lastMeasures.get(NetatmoUtils._030000000216_HUMIDITY);
        double _030000000216HumidityVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last030000000216Humidities,1));
        Measure[] last030000000216CarbonDioxydes=lastMeasures.get(NetatmoUtils._030000000216_CARBON_DIOXYDE);
        double _030000000216CarbonDioxydeVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last030000000216CarbonDioxydes,1));
        Measure[] last03000000076eTemperatures=lastMeasures.get(NetatmoUtils._03000000076e_TEMPERATURE);
        double _03000000076eTemperatureVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last03000000076eTemperatures,1));
        Measure[] last03000000076eHumidities=lastMeasures.get(NetatmoUtils._03000000076e_HUMIDITY);
        double _03000000076eHumidityVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last03000000076eHumidities,1));
        Measure[] last03000000076eCarbonDioxydes=lastMeasures.get(NetatmoUtils._03000000076e_CARBON_DIOXYDE);
        double _03000000076eCarbonDioxydeVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last03000000076eCarbonDioxydes,1));
        Measure[] last03000003fe8eTemperatures=lastMeasures.get(NetatmoUtils._03000003fe8e_TEMPERATURE);
        double _03000003fe8eTemperatureVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last03000003fe8eTemperatures,1));
        Measure[] last03000003fe8eHumidities=lastMeasures.get(NetatmoUtils._03000003fe8e_HUMIDITY);
        double _03000003fe8eHumidityVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last03000003fe8eHumidities,1));
        Measure[] last03000003fe8eCarbonDioxydes=lastMeasures.get(NetatmoUtils._03000003fe8e_CARBON_DIOXYDE);
        double _03000003fe8eCarbonDioxydeVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last03000003fe8eCarbonDioxydes,1));
        Measure[] last0200000010baTemperatures=lastMeasures.get(NetatmoUtils._0200000010ba_TEMPERATURE);
        double _0200000010baTemperatureVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last0200000010baTemperatures,1));
        Measure[] last0200000010baHumidities=lastMeasures.get(NetatmoUtils._0200000010ba_HUMIDITY);
        double _0200000010baHumidityVariation=NetatmoUtils.getHourlyVariation(NetatmoUtils.filterTimedWindowMeasures(last0200000010baHumidities,1));
        Measure[] array=lastMeasures.get(NetatmoUtils._05000004152c_TOTAL_RAIN);
        double _05000004152cTotalRain;
        if(array!=null&&array.length==1)
            _05000004152cTotalRain=array[0].getValue();
        else
            _05000004152cTotalRain=Double.NaN;
        array=lastMeasures.get(NetatmoUtils._06000000729a_MAX_GUST_STRENGTH);
        double _06000000729aMaxGustStrength;
        if(array!=null&&array.length==1)
            _06000000729aMaxGustStrength=array[0].getValue();
        else
            _06000000729aMaxGustStrength=Double.NaN;
        array=lastMeasures.get(NetatmoUtils._06000000729a_MAX_GUST_ANGLE);
        double _06000000729aMaxGustAngle;
        if(array!=null&&array.length==1)
            _06000000729aMaxGustAngle=array[0].getValue();
        else
            _06000000729aMaxGustAngle=Double.NaN;
        long now=System.currentTimeMillis();
        Measure[] lastToiletsTemperature=Database.getLastTemperaturesWhile(Duration.of(1).hourPlus(50).second()).stream()
                .filter(temperature->now-temperature.getTime()<Duration.of(1).hour())//un peu redondant désormais, non ?
                .map(temperature->new StandAloneMeasure(new Date(temperature.getTime()),MeasurementType.TEMPERATURE,temperature.getTemperature()))
                .toArray(Measure[]::new);
        double toiletsTemperatureVariation=NetatmoUtils.getHourlyVariation(lastToiletsTemperature);
        g2d.drawLine(64,68,62,68);
        g2d.drawLine(62,68,62,80);
        g2d.drawLine(62,80,98,80);
        g2d.drawLine(98,80,98,48);
        g2d.drawLine(98,48,62,48);
        g2d.drawLine(62,48,62,64);
        g2d.drawLine(62,64,68,64);
        g2d.drawLine(69,68,73,68);
        g2d.drawLine(79,64,89,64);
        g2d.drawLine(89,65,98,65);
        g2d.drawLine(62,60,68,60);
        g2d.drawLine(73,60,77,60);
        g2d.drawLine(77,60,77,53);
        g2d.drawLine(77,53,81,53);
        g2d.drawLine(81,53,81,60);
        g2d.drawLine(79,53,79,48);
        g2d.drawLine(77,55,81,55);
        g2d.drawLine(77,57,81,57);
        g2d.drawLine(77,59,81,59);
        g2d.drawLine(87,48,87,60);
        g2d.drawLine(87,60,86,60);
        g2d.drawLine(83,68,85,68);
        g2d.drawLine(85,68,85,66);
        g2d.drawLine(85,66,83,66);
        g2d.drawLine(83,66,83,68);
        g2d.drawLine(94,63,96,63);
        g2d.drawLine(96,63,96,61);
        g2d.drawLine(96,61,94,61);
        g2d.drawLine(94,61,94,63);
        g2d.drawLine(226,71,228,71);
        g2d.drawLine(228,71,228,69);
        g2d.drawLine(228,69,226,69);
        g2d.drawLine(226,69,226,71);
        g2d.drawLine(66,64,68,64);
        g2d.drawLine(68,64,68,62);
        g2d.drawLine(68,62,66,62);
        g2d.drawLine(66,62,66,64);
        g2d.drawLine(61,68,61,81);
        g2d.drawLine(61,81,99,81);
        g2d.drawLine(99,81,99,47);
        g2d.drawLine(99,47,61,47);
        g2d.drawLine(61,47,61,64);

        g2d.drawLine(206,48,198,48);
        g2d.drawLine(198,48,198,80);
        g2d.drawLine(198,80,218,80);
        g2d.drawLine(228,80,234,80);
        g2d.drawLine(234,80,234,48);
        g2d.drawLine(234,48,211,48);
        g2d.drawLine(198,64,200,64);
        g2d.drawLine(205,64,217,64);
        g2d.drawLine(217,64,217,73);
        g2d.drawLine(217,78,217,80);
        g2d.drawLine(198,75,202,75);
        g2d.drawLine(213,59,213,52);
        g2d.drawLine(217,52,217,64);
        g2d.drawLine(213,53,217,53);
        g2d.drawLine(213,55,217,55);
        g2d.drawLine(213,57,217,57);
        g2d.drawLine(213,59,217,59);
        g2d.drawLine(234,67,226,67);
        g2d.drawLine(234,64,226,64);
        g2d.drawLine(226,64,226,52);
        g2d.drawLine(226,52,228,52);
        g2d.drawLine(210,68,212,68);
        g2d.drawLine(212,68,212,66);
        g2d.drawLine(212,66,210,66);
        g2d.drawLine(210,66,210,68);
        g2d.drawLine(189,84,191,84);
        g2d.drawLine(191,84,191,82);
        g2d.drawLine(191,82,189,82);
        g2d.drawLine(189,82,189,84);
        g2d.drawLine(193,69,195,69);
        g2d.drawLine(195,69,195,67);
        g2d.drawLine(195,67,193,67);
        g2d.drawLine(193,67,193,69);
        g2d.drawLine(193,65,195,65);
        g2d.drawLine(195,65,195,63);
        g2d.drawLine(195,63,193,63);
        g2d.drawLine(193,63,193,65);
        g2d.drawLine(206,47,197,47);
        g2d.drawLine(197,47,197,81);
        g2d.drawLine(197,81,218,81);
        g2d.drawLine(228,81,235,81);
        g2d.drawLine(235,81,235,47);
        g2d.drawLine(235,47,211,47);

        Font baseFont=FREDOKA_ONE_FONT.deriveFont(20f);
        Font measureFont=baseFont.deriveFont(16f).deriveFont(AffineTransform.getScaleInstance(.7d,1d));
        g2d.setFont(measureFont);

        String _70ee50000deaString1=(Double.isNaN(_70ee50000deaTemperatureVariation)?"?":(_70ee50000deaTemperatureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(_70ee50000deaTemperatureVariation).replace(",","."))+"°C’  ";
        _70ee50000deaString1+=(Double.isNaN(_70ee50000deaHumidityVariation)?"?":(_70ee50000deaHumidityVariation>0d?"+":"")+INTEGER_FORMAT.format(_70ee50000deaHumidityVariation).replace(",","."))+"%’  ";
        _70ee50000deaString1+=(Double.isNaN(_70ee50000deaPressureVariation)?"?":(_70ee50000deaPressureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(_70ee50000deaPressureVariation).replace(",","."))+" hPa’";
        int _70ee50000deaString1Width=(int)Math.ceil(measureFont.getStringBounds(_70ee50000deaString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_70ee50000deaString1,74f-(float)_70ee50000deaString1Width/2f,110f);

        String _70ee50000deaString2=(Double.isNaN(_70ee50000deaCarbonDioxydeVariation)?"?":(_70ee50000deaCarbonDioxydeVariation>0d?"+":"")+INTEGER_FORMAT.format(_70ee50000deaCarbonDioxydeVariation).replace(",","."))+" ppm’  ";
        _70ee50000deaString2+=(Double.isNaN(_70ee50000deaNoiseVariation)?"?":(_70ee50000deaNoiseVariation>0d?"+":"")+INTEGER_FORMAT.format(_70ee50000deaNoiseVariation).replace(",","."))+" dB’";
        int _70ee50000deaString2Width=(int)Math.ceil(measureFont.getStringBounds(_70ee50000deaString2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_70ee50000deaString2,74f-(float)_70ee50000deaString2Width/2f,124f);

        int max70ee50000deaStringWidth=Math.max(_70ee50000deaString1Width,_70ee50000deaString2Width);
        g2d.drawLine(74-max70ee50000deaStringWidth/2-1,96,74+max70ee50000deaStringWidth/2+1,96);
        g2d.drawLine(83,68,64,96);
        g2d.drawString("Salon",74-max70ee50000deaStringWidth/2,95);

        String _030000000216String1=(Double.isNaN(_030000000216TemperatureVariation)?"?":(_030000000216TemperatureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(_030000000216TemperatureVariation).replace(",","."))+"°C’  ";
        _030000000216String1+=(Double.isNaN(_030000000216HumidityVariation)?"?":(_030000000216HumidityVariation>0d?"+":"")+INTEGER_FORMAT.format(_030000000216HumidityVariation).replace(",","."))+"%’";
        int _030000000216String1Width=(int)Math.ceil(measureFont.getStringBounds(_030000000216String1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_030000000216String1,37f-(float)_030000000216String1Width/2f,16f);

        String _030000000216String2=(Double.isNaN(_030000000216CarbonDioxydeVariation)?"?":(_030000000216CarbonDioxydeVariation>0d?"+":"")+INTEGER_FORMAT.format(_030000000216CarbonDioxydeVariation).replace(",","."))+" ppm’";
        int _030000000216String2Width=(int)Math.ceil(measureFont.getStringBounds(_030000000216String2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_030000000216String2,37f-(float)_030000000216String2Width/2f,30f);

        int max030000000216StringWidth=Math.max(_030000000216String1Width,_030000000216String2Width);
        g2d.drawLine(37-max030000000216StringWidth/2-1,31,37+max030000000216StringWidth/2+1,31);
        g2d.drawLine(58,31,86,41);
        g2d.drawLine(86,41,94,60);
        g2d.drawString("Filles",37-max030000000216StringWidth/2,45);

        String _03000000076eString1=(Double.isNaN(_03000000076eTemperatureVariation)?"?":(_03000000076eTemperatureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(_03000000076eTemperatureVariation).replace(",","."))+"°C’  ";
        _03000000076eString1+=(Double.isNaN(_03000000076eHumidityVariation)?"?":(_03000000076eHumidityVariation>0d?"+":"")+INTEGER_FORMAT.format(_03000000076eHumidityVariation).replace(",","."))+"%’";
        int _03000000076eString1Width=(int)Math.ceil(measureFont.getStringBounds(_03000000076eString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000000076eString1,111f-(float)_03000000076eString1Width/2f,16f);

        String _03000000076eString2=(Double.isNaN(_03000000076eCarbonDioxydeVariation)?"?":(_03000000076eCarbonDioxydeVariation>0d?"+":"")+INTEGER_FORMAT.format(_03000000076eCarbonDioxydeVariation).replace(",","."))+" ppm’";
        int _03000000076eString2Width=(int)Math.ceil(measureFont.getStringBounds(_03000000076eString2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000000076eString2,111f-(float)_03000000076eString2Width/2f,30f);

        int max03000000076eStringWidth=Math.max(_03000000076eString1Width,_03000000076eString2Width);
        g2d.drawLine(111-max03000000076eStringWidth/2-1,31,111+max03000000076eStringWidth/2+1,31);
        g2d.drawLine(89,31,118,57);
        g2d.drawLine(118,57,205,57);
        g2d.drawLine(205,57,226,69);
        String _03000000076eString="Garage";
        int _03000000076eStringWidth=(int)Math.ceil(measureFont.getStringBounds(_03000000076eString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000000076eString,111+max03000000076eStringWidth/2+1-_03000000076eStringWidth,45);

        String toiletsString=(Double.isNaN(toiletsTemperatureVariation)?"?":(toiletsTemperatureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(toiletsTemperatureVariation).replace(",","."))+"°C’";
        int toiletsStringWidth=(int)Math.ceil(measureFont.getStringBounds(toiletsString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(toiletsString,2,63);

        g2d.drawLine(1,64,2+toiletsStringWidth+1,64);
        g2d.drawLine(66,64,38,73);
        g2d.drawLine(38,73,25,64);
        g2d.drawString("WC",2,78);

        String _0200000010baString1=(Double.isNaN(_0200000010baTemperatureVariation)?"?":(_0200000010baTemperatureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(_0200000010baTemperatureVariation).replace(",","."))+"°C’  ";
        _0200000010baString1+=(Double.isNaN(_0200000010baHumidityVariation)?"?":(_0200000010baHumidityVariation>0d?"+":"")+INTEGER_FORMAT.format(_0200000010baHumidityVariation).replace(",","."))+"%’";
        int _0200000010baString1Width=(int)Math.ceil(measureFont.getStringBounds(_0200000010baString1,g2d.getFontRenderContext()).getWidth());
        int _0200000010baLeftOffset=Math.max(0,34-_0200000010baString1Width/2);
        g2d.drawString(_0200000010baString1,185f-(float)_0200000010baString1Width/2f-(float)_0200000010baLeftOffset,110f);

        g2d.drawLine(185-_0200000010baString1Width/2-1-_0200000010baLeftOffset,96,185+_0200000010baString1Width/2+1-_0200000010baLeftOffset,96);
        g2d.drawLine(190,84,195,96);
        g2d.drawString("Jardin",185-_0200000010baString1Width/2-_0200000010baLeftOffset,95);

        String _03000003fe8eString1=(Double.isNaN(_03000003fe8eTemperatureVariation)?"?":(_03000003fe8eTemperatureVariation>0d?"+":"")+DECIMAL_0_FORMAT.format(_03000003fe8eTemperatureVariation).replace(",","."))+"°C’  ";
        _03000003fe8eString1+=(Double.isNaN(_03000003fe8eHumidityVariation)?"?":(_03000003fe8eHumidityVariation>0d?"+":"")+INTEGER_FORMAT.format(_03000003fe8eHumidityVariation).replace(",","."))+"%’";
        int _03000003fe8eString1Width=(int)Math.ceil(measureFont.getStringBounds(_03000003fe8eString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000003fe8eString1,259f-(float)_03000003fe8eString1Width/2f,110f);

        String _03000003fe8eString2=(Double.isNaN(_03000003fe8eCarbonDioxydeVariation)?"?":(_03000003fe8eCarbonDioxydeVariation>0d?"+":"")+INTEGER_FORMAT.format(_03000003fe8eCarbonDioxydeVariation).replace(",","."))+" ppm’";
        int _03000003fe8eString2Width=(int)Math.ceil(measureFont.getStringBounds(_03000003fe8eString2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000003fe8eString2,259f-(float)_03000003fe8eString2Width/2f,124f);

        int max03000003fe8eStringWidth=Math.max(_03000003fe8eString1Width,_03000003fe8eString2Width);
        g2d.drawLine(259-max03000003fe8eStringWidth/2-1,96,259+max03000003fe8eStringWidth/2+1,96);
        g2d.drawLine(212,68,253,96);
        String _03000003fe8eString="PJ";
        int _03000003fe8eStringWidth=(int)Math.ceil(measureFont.getStringBounds(_03000003fe8eString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000003fe8eString,259+max03000003fe8eStringWidth/2+1-_03000003fe8eStringWidth,95);

        String _05000004152cString1=(Double.isNaN(_05000004152cTotalRain)?"?":DECIMAL_0_FORMAT.format(_05000004152cTotalRain).replace(",","."))+" mm";
        int _05000004152cString1Width=(int)Math.ceil(measureFont.getStringBounds(_05000004152cString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_05000004152cString1,180f-(float)_05000004152cString1Width/2f,30f);

        g2d.drawLine(180-_05000004152cString1Width/2-1,31,180+_05000004152cString1Width/2+1,31);
        g2d.drawLine(193,68,188,63);
        g2d.drawLine(188,63,188,31);
        g2d.drawString("Pluie",180-_05000004152cString1Width/2,45);

        String _06000000729aString="ˆ"+(Double.isNaN(_06000000729aMaxGustStrength)?"?":INTEGER_FORMAT.format(_06000000729aMaxGustStrength).replace(",","."))+" km/h ";
        _06000000729aString+=Double.isNaN(_06000000729aMaxGustAngle)?"":" "+WeatherUtils.convertWindAngle(_06000000729aMaxGustAngle);
        int _06000000729aStringWidth=(int)Math.ceil(measureFont.getStringBounds(_06000000729aString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_06000000729aString,254f-(float)_06000000729aStringWidth/2f,30f);

        g2d.drawLine(254-_06000000729aStringWidth/2-1,31,254+_06000000729aStringWidth/2+1,31);
        g2d.drawLine(194,63,194,42);
        g2d.drawLine(194,42,238,31);
        String _06000000729aStringBis="Vent";
        int _06000000729aStringBisWidth=(int)Math.ceil(measureFont.getStringBounds(_06000000729aStringBis,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_06000000729aStringBis,254+_06000000729aStringWidth/2/*+1*/-_06000000729aStringBisWidth,45);
    }

    protected String getDebugImageFileName()
    {
        return "meteo_variation.png";
    }

    public static void main(String[] args)
    {
        new HomeWeatherVariationPage(null).potentiallyUpdate();
    }
}
