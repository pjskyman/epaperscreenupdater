package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Date;
import java.util.List;
import java.util.Map;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementType;
import sky.program.Duration;

public class HomeWeatherPage extends AbstractSinglePage
{
    public HomeWeatherPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Données météo (actuelles)";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(6).secondPlus(300).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Map<String,Measure[]> lastMeasures=NetatmoUtils.getLastMeasures();
        Measure last70ee50000deaTemperature=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._70ee50000dea_TEMPERATURE);
        Measure last70ee50000deaHumidity=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._70ee50000dea_HUMIDITY);
        Measure last70ee50000deaPressure=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._70ee50000dea_PRESSURE);
        Measure last70ee50000deaCarbonDioxyde=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._70ee50000dea_CARBON_DIOXYDE);
        Measure last70ee50000deaNoise=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._70ee50000dea_NOISE);
        Measure last030000000216Temperature=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._030000000216_TEMPERATURE);
        Measure last030000000216Humidity=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._030000000216_HUMIDITY);
        Measure last030000000216CarbonDioxyde=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._030000000216_CARBON_DIOXYDE);
        Measure last03000000076eTemperature=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._03000000076e_TEMPERATURE);
        Measure last03000000076eHumidity=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._03000000076e_HUMIDITY);
        Measure last03000000076eCarbonDioxyde=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._03000000076e_CARBON_DIOXYDE);
        Measure last03000003fe8eTemperature=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._03000003fe8e_TEMPERATURE);
        Measure last03000003fe8eHumidity=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._03000003fe8e_HUMIDITY);
        Measure last03000003fe8eCarbonDioxyde=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._03000003fe8e_CARBON_DIOXYDE);
        Measure last0200000010baTemperature=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._0200000010ba_TEMPERATURE);
        Measure last0200000010baHumidity=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._0200000010ba_HUMIDITY);
        Measure last05000004152cRain=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._05000004152c_RAIN);
        Measure last06000000729aWindStrength=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._06000000729a_WIND_STRENGTH);
        Measure last06000000729aWindAngle=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._06000000729a_WIND_ANGLE);
        Measure last06000000729aGustStrength=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._06000000729a_GUST_STRENGTH);
        Measure last06000000729aGustAngle=NetatmoUtils.getLastMeasure(lastMeasures,NetatmoUtils._06000000729a_GUST_ANGLE);
        List<Temperature> temperatures=WeatherUtils.loadTemperatures(1);
        Measure lastToiletsTemperature=temperatures.isEmpty()?null:new StandAloneMeasure(new Date(temperatures.get(0).getTime()),MeasurementType.TEMPERATURE,temperatures.get(0).getTemperature());
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

        String _70ee50000deaString1=(last70ee50000deaTemperature!=null?""+last70ee50000deaTemperature.getValue():"?")+"°C  ";
        _70ee50000deaString1+=(last70ee50000deaHumidity!=null?""+(int)last70ee50000deaHumidity.getValue():"?")+"%  ";
        _70ee50000deaString1+=(last70ee50000deaPressure!=null?""+last70ee50000deaPressure.getValue():"?")+" hPa";
        int _70ee50000deaString1Width=(int)Math.ceil(measureFont.getStringBounds(_70ee50000deaString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_70ee50000deaString1,74f-(float)_70ee50000deaString1Width/2f,110f);

        String _70ee50000deaString2=(last70ee50000deaCarbonDioxyde!=null?""+(int)last70ee50000deaCarbonDioxyde.getValue():"?")+" ppm  ";
        _70ee50000deaString2+=(last70ee50000deaNoise!=null?""+(int)last70ee50000deaNoise.getValue():"?")+" dB";
        int _70ee50000deaString2Width=(int)Math.ceil(measureFont.getStringBounds(_70ee50000deaString2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_70ee50000deaString2,74f-(float)_70ee50000deaString2Width/2f,124f);

        int max70ee50000deaStringWidth=Math.max(_70ee50000deaString1Width,_70ee50000deaString2Width);
        g2d.drawLine(74-max70ee50000deaStringWidth/2-1,96,74+max70ee50000deaStringWidth/2+1,96);
        g2d.drawLine(83,68,64,96);
        g2d.drawString("Salon",74-max70ee50000deaStringWidth/2,95);

        String _030000000216String1=(last030000000216Temperature!=null?""+last030000000216Temperature.getValue():"?")+"°C  ";
        _030000000216String1+=(last030000000216Humidity!=null?""+(int)last030000000216Humidity.getValue():"?")+"%";
        int _030000000216String1Width=(int)Math.ceil(measureFont.getStringBounds(_030000000216String1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_030000000216String1,37f-(float)_030000000216String1Width/2f,16f);

        String _030000000216String2=(last030000000216CarbonDioxyde!=null?""+(int)last030000000216CarbonDioxyde.getValue():"?")+" ppm";
        int _030000000216String2Width=(int)Math.ceil(measureFont.getStringBounds(_030000000216String2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_030000000216String2,37f-(float)_030000000216String2Width/2f,30f);

        int max030000000216StringWidth=Math.max(_030000000216String1Width,_030000000216String2Width);
        g2d.drawLine(37-max030000000216StringWidth/2-1,31,37+max030000000216StringWidth/2+1,31);
        g2d.drawLine(58,31,86,41);
        g2d.drawLine(86,41,94,60);
        g2d.drawString("Filles",37-max030000000216StringWidth/2,45);

        String _03000000076eString1=(last03000000076eTemperature!=null?""+last03000000076eTemperature.getValue():"?")+"°C  ";
        _03000000076eString1+=(last03000000076eHumidity!=null?""+(int)last03000000076eHumidity.getValue():"?")+"%";
        int _03000000076eString1Width=(int)Math.ceil(measureFont.getStringBounds(_03000000076eString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000000076eString1,111f-(float)_03000000076eString1Width/2f,16f);

        String _03000000076eString2=(last03000000076eCarbonDioxyde!=null?""+(int)last03000000076eCarbonDioxyde.getValue():"?")+" ppm";
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

        String toiletsString=(lastToiletsTemperature!=null?DECIMAL_0_FORMAT.format(lastToiletsTemperature.getValue()).replace(",","."):"?")+"°C";
        int toiletsStringWidth=(int)Math.ceil(measureFont.getStringBounds(toiletsString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(toiletsString,2,63);

        g2d.drawLine(1,64,2+toiletsStringWidth+1,64);
        g2d.drawLine(66,64,38,73);
        g2d.drawLine(38,73,25,64);
        g2d.drawString("WC",2,78);

        String _0200000010baString1=(last0200000010baTemperature!=null?""+last0200000010baTemperature.getValue():"?")+"°C  ";
        _0200000010baString1+=(last0200000010baHumidity!=null?""+(int)last0200000010baHumidity.getValue():"?")+"%";
        int _0200000010baString1Width=(int)Math.ceil(measureFont.getStringBounds(_0200000010baString1,g2d.getFontRenderContext()).getWidth());
        int _0200000010baLeftOffset=Math.max(0,34-_0200000010baString1Width/2);
        g2d.drawString(_0200000010baString1,185f-(float)_0200000010baString1Width/2f-(float)_0200000010baLeftOffset,110f);

        g2d.drawLine(185-_0200000010baString1Width/2-1-_0200000010baLeftOffset,96,185+_0200000010baString1Width/2+1-_0200000010baLeftOffset,96);
        g2d.drawLine(190,84,195,96);
        g2d.drawString("Jardin",185-_0200000010baString1Width/2-_0200000010baLeftOffset,95);

        String _03000003fe8eString1=(last03000003fe8eTemperature!=null?""+last03000003fe8eTemperature.getValue():"?")+"°C  ";
        _03000003fe8eString1+=(last03000003fe8eHumidity!=null?""+(int)last03000003fe8eHumidity.getValue():"?")+"%";
        int _03000003fe8eString1Width=(int)Math.ceil(measureFont.getStringBounds(_03000003fe8eString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000003fe8eString1,259f-(float)_03000003fe8eString1Width/2f,110f);

        String _03000003fe8eString2=(last03000003fe8eCarbonDioxyde!=null?""+(int)last03000003fe8eCarbonDioxyde.getValue():"?")+" ppm";
        int _03000003fe8eString2Width=(int)Math.ceil(measureFont.getStringBounds(_03000003fe8eString2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000003fe8eString2,259f-(float)_03000003fe8eString2Width/2f,124f);

        int max03000003fe8eStringWidth=Math.max(_03000003fe8eString1Width,_03000003fe8eString2Width);
        g2d.drawLine(259-max03000003fe8eStringWidth/2-1,96,259+max03000003fe8eStringWidth/2+1,96);
        g2d.drawLine(212,68,253,96);
        String _03000003fe8eString="PJ";
        int _03000003fe8eStringWidth=(int)Math.ceil(measureFont.getStringBounds(_03000003fe8eString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_03000003fe8eString,259+max03000003fe8eStringWidth/2+1-_03000003fe8eStringWidth,95);

        String _05000004152cString1=(last05000004152cRain!=null?DECIMAL_0_FORMAT.format(last05000004152cRain.getValue()).replace(",","."):"?")+" mm/h";
        int _05000004152cString1Width=(int)Math.ceil(measureFont.getStringBounds(_05000004152cString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_05000004152cString1,180f-(float)_05000004152cString1Width/2f,30f);

        g2d.drawLine(180-_05000004152cString1Width/2-1,31,180+_05000004152cString1Width/2+1,31);
        g2d.drawLine(193,68,188,63);
        g2d.drawLine(188,63,188,31);
        g2d.drawString("Pluie",180-_05000004152cString1Width/2,45);

        String _06000000729aString1="~"+(last06000000729aWindStrength!=null?""+(int)last06000000729aWindStrength.getValue():"?")+" km/h ";
        _06000000729aString1+=last06000000729aWindAngle!=null?WeatherUtils.convertWindAngle(last06000000729aWindAngle.getValue()):"?";
        int _06000000729aString1Width=(int)Math.ceil(measureFont.getStringBounds(_06000000729aString1,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_06000000729aString1,254f-(float)_06000000729aString1Width/2f,16f);

        String _06000000729aString2="ˆ"+(last06000000729aGustStrength!=null?""+(int)last06000000729aGustStrength.getValue():"?")+" km/h ";
        _06000000729aString2+=last06000000729aGustAngle!=null?WeatherUtils.convertWindAngle(last06000000729aGustAngle.getValue()):"?";
        int _06000000729aString2Width=(int)Math.ceil(measureFont.getStringBounds(_06000000729aString2,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_06000000729aString2,254f-(float)_06000000729aString2Width/2f,30f);

        int max06000000729aStringWidth=Math.max(_06000000729aString1Width,_06000000729aString2Width);
        g2d.drawLine(254-max06000000729aStringWidth/2-1,31,254+max06000000729aStringWidth/2+1,31);
        g2d.drawLine(194,63,194,42);
        g2d.drawLine(194,42,238,31);
        String _06000000729aString="Vent";
        int _06000000729aStringWidth=(int)Math.ceil(measureFont.getStringBounds(_06000000729aString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(_06000000729aString,254+max06000000729aStringWidth/2/*+1*/-_06000000729aStringWidth,45);
    }

    protected String getDebugImageFileName()
    {
        return "meteo.png";
    }

    public static void main(String[] args)
    {
        new HomeWeatherPage(null).potentiallyUpdate();
    }
}
