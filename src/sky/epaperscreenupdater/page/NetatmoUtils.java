package sky.epaperscreenupdater.page;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import sky.housecommon.Logger;
import sky.netatmo.Device;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementScale;
import sky.netatmo.MeasurementType;
import sky.netatmo.Module;
import sky.netatmo.NetatmoException;
import sky.netatmo.Token;
import sky.program.Duration;

public class NetatmoUtils
{
    private static Token token=null;
    private static long lastNetatmoVerificationTime=0L;
    private static Measure[] last70ee50000deaTemperatures=null;
    private static Measure[] last70ee50000deaHumidities=null;
    private static Measure[] last70ee50000deaPressures=null;
    private static Measure[] last70ee50000deaCarbonDioxydes=null;
    private static Measure[] last70ee50000deaNoises=null;
    private static Measure[] _70ee50000deaWifi=null;
    private static Measure[] _70ee50000deaFirmware=null;
    private static Measure[] last030000000216Temperatures=null;
    private static Measure[] last030000000216Humidities=null;
    private static Measure[] last030000000216CarbonDioxydes=null;
    private static Measure[] _030000000216Battery=null;
    private static Measure[] _030000000216Radio=null;
    private static Measure[] last03000000076eTemperatures=null;
    private static Measure[] last03000000076eHumidities=null;
    private static Measure[] last03000000076eCarbonDioxydes=null;
    private static Measure[] _03000000076eBattery=null;
    private static Measure[] _03000000076eRadio=null;
    private static Measure[] last03000003fe8eTemperatures=null;
    private static Measure[] last03000003fe8eHumidities=null;
    private static Measure[] last03000003fe8eCarbonDioxydes=null;
    private static Measure[] _03000003fe8eBattery=null;
    private static Measure[] _03000003fe8eRadio=null;
    private static Measure[] last0200000010baTemperatures=null;
    private static Measure[] last0200000010baHumidities=null;
    private static Measure[] _0200000010baBattery=null;
    private static Measure[] _0200000010baRadio=null;
    private static Measure[] last05000004152cRains=null;
    private static Measure[] _05000004152cTotalRain=null;
    private static Measure[] _05000004152cBattery=null;
    private static Measure[] _05000004152cRadio=null;
    private static Measure[] last06000000729aWindStrengths=null;
    private static Measure[] last06000000729aWindAngles=null;
    private static Measure[] last06000000729aGustStrengths=null;
    private static Measure[] last06000000729aGustAngles=null;
    private static Measure[] _06000000729aMaxGustStrength=null;
    private static Measure[] _06000000729aMaxGustAngle=null;
    private static Measure[] _06000000729aBattery=null;
    private static Measure[] _06000000729aRadio=null;
    public static final String _70ee50000dea_TEMPERATURE="70ee50000deaTemperature";
    public static final String _70ee50000dea_HUMIDITY="70ee50000deaHumidity";
    public static final String _70ee50000dea_PRESSURE="70ee50000deaPressure";
    public static final String _70ee50000dea_CARBON_DIOXYDE="70ee50000deaCarbonDioxyde";
    public static final String _70ee50000dea_NOISE="70ee50000deaNoise";
    public static final String _70ee50000dea_WIFI="70ee50000deaWifi";
    public static final String _70ee50000dea_FIRMWARE="70ee50000deaFirmware";
    public static final String _030000000216_TEMPERATURE="030000000216Temperature";
    public static final String _030000000216_HUMIDITY="030000000216Humidity";
    public static final String _030000000216_CARBON_DIOXYDE="030000000216CarbonDioxyde";
    public static final String _030000000216_BATTERY="030000000216Battery";
    public static final String _030000000216_RADIO="030000000216Radio";
    public static final String _03000000076e_TEMPERATURE="03000000076eTemperature";
    public static final String _03000000076e_HUMIDITY="03000000076eHumidity";
    public static final String _03000000076e_CARBON_DIOXYDE="03000000076eCarbonDioxyde";
    public static final String _03000000076e_BATTERY="03000000076eBattery";
    public static final String _03000000076e_RADIO="03000000076eRadio";
    public static final String _03000003fe8e_TEMPERATURE="03000003fe8eTemperature";
    public static final String _03000003fe8e_HUMIDITY="03000003fe8eHumidity";
    public static final String _03000003fe8e_CARBON_DIOXYDE="03000003fe8eCarbonDioxyde";
    public static final String _03000003fe8e_BATTERY="03000003fe8eBattery";
    public static final String _03000003fe8e_RADIO="03000003fe8eRadio";
    public static final String _0200000010ba_TEMPERATURE="0200000010baTemperature";
    public static final String _0200000010ba_HUMIDITY="0200000010baHumidity";
    public static final String _0200000010ba_BATTERY="0200000010baBattery";
    public static final String _0200000010ba_RADIO="0200000010baRadio";
    public static final String _05000004152c_RAIN="05000004152cRain";
    public static final String _05000004152c_TOTAL_RAIN="05000004152cTotalRain";
    public static final String _05000004152c_BATTERY="05000004152cBattery";
    public static final String _05000004152c_RADIO="05000004152cRadio";
    public static final String _06000000729a_WIND_STRENGTH="06000000729aWindStrength";
    public static final String _06000000729a_WIND_ANGLE="06000000729aWindAngle";
    public static final String _06000000729a_GUST_STRENGTH="06000000729aGustStrength";
    public static final String _06000000729a_GUST_ANGLE="06000000729aGustAngle";
    public static final String _06000000729a_MAX_GUST_STRENGTH="06000000729aMaxGustStrength";
    public static final String _06000000729a_MAX_GUST_ANGLE="06000000729aMaxGustAngle";
    public static final String _06000000729a_BATTERY="06000000729aBattery";
    public static final String _06000000729a_RADIO="06000000729aRadio";
    private static final Comparator<Measure> CHRONOLOGICAL_SORTER=(o1,o2)->Long.compare(o1.getDate().getTime(),o2.getDate().getTime());
    private static final MeasureDatabase DATABASE=new MeasureDatabase();
    private static final long MEASURE_DELAY=1_200_000L;
    private static final boolean NETATMO_ENABLED=true;

    private NetatmoUtils()
    {
    }

    public static MeasureDatabase getMeasureDatabase()
    {
        return DATABASE;
    }

    public static synchronized Map<String,Measure[]> getLastMeasures()
    {
        long now=System.currentTimeMillis();
        if(NETATMO_ENABLED)
        {
            if(now-lastNetatmoVerificationTime>Duration.of(10).minute())
            {
                lastNetatmoVerificationTime=now;
                boolean ok=false;
                try
                {
                    if(token==null)
                    {
                        String clientId="";
                        String clientSecret="";
                        String userName="";
                        String password="";
                        try(BufferedReader reader=new BufferedReader(new FileReader(new File("netatmo.ini"))))
                        {
                            clientId=reader.readLine();
                            clientSecret=reader.readLine();
                            userName=reader.readLine();
                            password=reader.readLine();
                        }
                        catch(IOException e)
                        {
                            Logger.LOGGER.error("Unable to read Netatmo access informations from the config file ("+e.toString()+")");
                            e.printStackTrace();
                        }
                        token=Token.getToken(clientId,clientSecret,userName,password);
                        if(token!=null)
                            Logger.LOGGER.info("Initial token successfully acquired");
                        else
                            Logger.LOGGER.info("Unable to acquire initial token");
                    }
                    if(token!=null&&token.isExpired())
                    {
                        token=token.renewExpiredToken();
                        if(token!=null)
                            Logger.LOGGER.info("Token successfully renewed");
                        else
                            Logger.LOGGER.info("Unable to renew token");
                    }
                    if(token!=null)
                    {
                        Device homeDevice=token.getUser().getDevices()[0];
                        Device _70ee50000dea=homeDevice;
                        JsonObject _70ee50000deaObject=_70ee50000dea.getAttributes();
                        _70ee50000deaWifi=new Measure[]{new StandAloneMeasure(new Date(now),null,_70ee50000deaObject.get("wifi_status").getAsDouble())};
                        _70ee50000deaFirmware=new Measure[]{new StandAloneMeasure(new Date(now),null,_70ee50000deaObject.get("firmware").getAsDouble())};
                        Module[] homeModules=homeDevice.getModules();
                        Module _0200000010ba=homeModules[0];
                        JsonObject _0200000010baObject=_0200000010ba.getAttributes();
                        _0200000010baBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,_0200000010baObject.get("battery_vp").getAsDouble())};
                        _0200000010baRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,_0200000010baObject.get("rf_status").getAsDouble())};
                        Module _030000000216=homeModules[1];
                        JsonObject _030000000216Object=_030000000216.getAttributes();
                        _030000000216Battery=new Measure[]{new StandAloneMeasure(new Date(now),null,_030000000216Object.get("battery_vp").getAsDouble())};
                        _030000000216Radio=new Measure[]{new StandAloneMeasure(new Date(now),null,_030000000216Object.get("rf_status").getAsDouble())};
                        Module _03000000076e=homeModules[2];
                        JsonObject _03000000076eObject=_03000000076e.getAttributes();
                        _03000000076eBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,_03000000076eObject.get("battery_vp").getAsDouble())};
                        _03000000076eRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,_03000000076eObject.get("rf_status").getAsDouble())};
                        Module _06000000729a=homeModules[3];
                        JsonObject _06000000729aObject=_06000000729a.getAttributes();
                        JsonObject _06000000729aDashboardDataObject=_06000000729aObject.get("dashboard_data").getAsJsonObject();
                        _06000000729aMaxGustStrength=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,_06000000729aDashboardDataObject.get("max_wind_str").getAsDouble())};
                        _06000000729aMaxGustAngle=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_ANGLE,_06000000729aDashboardDataObject.get("max_wind_angle").getAsDouble())};
                        _06000000729aBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,_06000000729aObject.get("battery_vp").getAsDouble())};
                        _06000000729aRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,_06000000729aObject.get("rf_status").getAsDouble())};
                        Module _03000003fe8e=homeModules[4];
                        JsonObject _03000003fe8eObject=_03000003fe8e.getAttributes();
                        _03000003fe8eBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,_03000003fe8eObject.get("battery_vp").getAsDouble())};
                        _03000003fe8eRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,_03000003fe8eObject.get("rf_status").getAsDouble())};
                        Module _05000004152c=homeModules[5];
                        JsonObject _05000004152cObject=_05000004152c.getAttributes();
                        JsonObject _05000004152cDashboardDataObject=_05000004152cObject.get("dashboard_data").getAsJsonObject();
                        _05000004152cTotalRain=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.RAIN,_05000004152cDashboardDataObject.get("sum_rain_24").getAsDouble())};
                        _05000004152cBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,_05000004152cObject.get("battery_vp").getAsDouble())};
                        _05000004152cRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,_05000004152cObject.get("rf_status").getAsDouble())};
                        Measure[] _70ee50000deaMeasures=_70ee50000dea.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2,MeasurementType.NOISE);
                        Measure[] _70ee50000deaPressureMeasures=_70ee50000dea.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hourPlus(15).minute()),new Date(now+Duration.of(5).minute()),MeasurementType.PRESSURE);
                        Measure[] _0200000010baMeasures=_0200000010ba.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY);
                        Measure[] _030000000216Measures=_030000000216.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                        Measure[] _03000000076eMeasures=_03000000076e.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                        Measure[] _06000000729aMeasures=_06000000729a.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.WIND_STRENGTH,MeasurementType.WIND_ANGLE,MeasurementType.GUST_STRENGTH,MeasurementType.GUST_ANGLE);
                        Measure[] _03000003fe8eMeasures=_03000003fe8e.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                        Measure[] _05000004152cMeasures=_05000004152c.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.RAIN);
                        last70ee50000deaTemperatures=filterMeasures(_70ee50000deaMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(_70ee50000dea_TEMPERATURE,last70ee50000deaTemperatures);
                        last70ee50000deaHumidities=filterMeasures(_70ee50000deaMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(_70ee50000dea_HUMIDITY,last70ee50000deaHumidities);
                        last70ee50000deaPressures=filterMeasures(_70ee50000deaPressureMeasures,MeasurementType.PRESSURE);
                        DATABASE.addMeasures(_70ee50000dea_PRESSURE,last70ee50000deaPressures);
                        last70ee50000deaCarbonDioxydes=filterMeasures(_70ee50000deaMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(_70ee50000dea_CARBON_DIOXYDE,last70ee50000deaCarbonDioxydes);
                        last70ee50000deaNoises=filterMeasures(_70ee50000deaMeasures,MeasurementType.NOISE);
                        DATABASE.addMeasures(_70ee50000dea_NOISE,last70ee50000deaNoises);
                        last030000000216Temperatures=filterMeasures(_030000000216Measures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(_030000000216_TEMPERATURE,last030000000216Temperatures);
                        last030000000216Humidities=filterMeasures(_030000000216Measures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(_030000000216_HUMIDITY,last030000000216Humidities);
                        last030000000216CarbonDioxydes=filterMeasures(_030000000216Measures,MeasurementType.CO2);
                        DATABASE.addMeasures(_030000000216_CARBON_DIOXYDE,last030000000216CarbonDioxydes);
                        last03000000076eTemperatures=filterMeasures(_03000000076eMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(_03000000076e_TEMPERATURE,last03000000076eTemperatures);
                        last03000000076eHumidities=filterMeasures(_03000000076eMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(_03000000076e_HUMIDITY,last03000000076eHumidities);
                        last03000000076eCarbonDioxydes=filterMeasures(_03000000076eMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(_03000000076e_CARBON_DIOXYDE,last03000000076eCarbonDioxydes);
                        last03000003fe8eTemperatures=filterMeasures(_03000003fe8eMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(_03000003fe8e_TEMPERATURE,last03000003fe8eTemperatures);
                        last03000003fe8eHumidities=filterMeasures(_03000003fe8eMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(_03000003fe8e_HUMIDITY,last03000003fe8eHumidities);
                        last03000003fe8eCarbonDioxydes=filterMeasures(_03000003fe8eMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(_03000003fe8e_CARBON_DIOXYDE,last03000003fe8eCarbonDioxydes);
                        last0200000010baTemperatures=filterMeasures(_0200000010baMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(_0200000010ba_TEMPERATURE,last0200000010baTemperatures);
                        last0200000010baHumidities=filterMeasures(_0200000010baMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(_0200000010ba_HUMIDITY,last0200000010baHumidities);
                        last05000004152cRains=filterMeasures(_05000004152cMeasures,MeasurementType.RAIN);
                        DATABASE.addMeasures(_05000004152c_RAIN,last05000004152cRains);
                        last06000000729aWindStrengths=filterMeasures(_06000000729aMeasures,MeasurementType.WIND_STRENGTH);
                        DATABASE.addMeasures(_06000000729a_WIND_STRENGTH,last06000000729aWindStrengths);
                        last06000000729aWindAngles=filterMeasures(_06000000729aMeasures,MeasurementType.WIND_ANGLE);
                        DATABASE.addMeasures(_06000000729a_WIND_ANGLE,last06000000729aWindAngles);
                        last06000000729aGustStrengths=filterMeasures(_06000000729aMeasures,MeasurementType.GUST_STRENGTH);
                        DATABASE.addMeasures(_06000000729a_GUST_STRENGTH,last06000000729aGustStrengths);
                        last06000000729aGustAngles=filterMeasures(_06000000729aMeasures,MeasurementType.GUST_ANGLE);
                        DATABASE.addMeasures(_06000000729a_GUST_ANGLE,last06000000729aGustAngles);
                        DATABASE.clean();
                        ok=true;
                        if(last70ee50000deaTemperatures!=null&&last70ee50000deaTemperatures.length>0)
                        {
                            long lastMeasureTime=last70ee50000deaTemperatures[last70ee50000deaTemperatures.length-1].getDate().getTime();
                            long nextMeasureTime=lastMeasureTime+Duration.of(11).minute();//1 minute de rab' pour laisser le temps à Netatmo de publier sa mesure
                            long nextNetatmoVerificationTime=lastNetatmoVerificationTime+Duration.of(10).minute();
                            if(nextNetatmoVerificationTime-nextMeasureTime<Duration.of(10).minute())
                            {
                                lastNetatmoVerificationTime=lastMeasureTime+Duration.of(1).minute();
                                Logger.LOGGER.info("Anticipation of the next Netatmo update from "+DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date(nextNetatmoVerificationTime))+" to "+DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date(nextMeasureTime))+" because last measure was taken at "+DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date(lastMeasureTime)));
                            }
                        }
                    }
                }
                catch(NetatmoException e)
                {
                    Logger.LOGGER.error(e.toString());
                    e.printStackTrace();
                }
                if(!ok)
                {
                    last70ee50000deaTemperatures=null;
                    last70ee50000deaHumidities=null;
                    last70ee50000deaPressures=null;
                    last70ee50000deaCarbonDioxydes=null;
                    last70ee50000deaNoises=null;
                    _70ee50000deaWifi=null;
                    _70ee50000deaFirmware=null;
                    last030000000216Temperatures=null;
                    last030000000216Humidities=null;
                    last030000000216CarbonDioxydes=null;
                    _030000000216Battery=null;
                    _030000000216Radio=null;
                    last03000000076eTemperatures=null;
                    last03000000076eHumidities=null;
                    last03000000076eCarbonDioxydes=null;
                    _03000000076eBattery=null;
                    _03000000076eRadio=null;
                    last03000003fe8eTemperatures=null;
                    last03000003fe8eHumidities=null;
                    last03000003fe8eCarbonDioxydes=null;
                    _03000003fe8eBattery=null;
                    _03000003fe8eRadio=null;
                    last0200000010baTemperatures=null;
                    last0200000010baHumidities=null;
                    _0200000010baBattery=null;
                    _0200000010baRadio=null;
                    last05000004152cRains=null;
                    _05000004152cTotalRain=null;
                    _05000004152cBattery=null;
                    _05000004152cRadio=null;
                    last06000000729aWindStrengths=null;
                    last06000000729aWindAngles=null;
                    last06000000729aGustStrengths=null;
                    last06000000729aGustAngles=null;
                    _06000000729aMaxGustStrength=null;
                    _06000000729aMaxGustAngle=null;
                    _06000000729aBattery=null;
                    _06000000729aRadio=null;
                }
            }
        }
        else
        {
            last70ee50000deaTemperatures=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.TEMPERATURE,26.2d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.TEMPERATURE,23.2d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.TEMPERATURE,28.2d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.TEMPERATURE,20.2d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.TEMPERATURE,24.7d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.TEMPERATURE,26.8d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.TEMPERATURE,27.2d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.TEMPERATURE,29.5d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.TEMPERATURE,23.7d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.TEMPERATURE,24.3d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.TEMPERATURE,25.7d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.TEMPERATURE,27.1d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.TEMPERATURE,28.2d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.TEMPERATURE,25.8d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.TEMPERATURE,14.2d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.TEMPERATURE,13.2d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.TEMPERATURE,22.7d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.TEMPERATURE,26.8d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.TEMPERATURE,27.2d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.TEMPERATURE,29.5d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.TEMPERATURE,20.7d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.TEMPERATURE,21.3d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.TEMPERATURE,22.7d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.TEMPERATURE,23.1d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.TEMPERATURE,25.2d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.TEMPERATURE,25.8d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.TEMPERATURE,27d),
            };
            last70ee50000deaHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,65d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,60d)};
            last70ee50000deaPressures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(2).hour()),MeasurementType.PRESSURE,1032.7d),new StandAloneMeasure(new Date(now),MeasurementType.PRESSURE,1030.2d)};
            last70ee50000deaCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,4687d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,4235d)};
            last70ee50000deaNoises=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.NOISE,60d),new StandAloneMeasure(new Date(now),MeasurementType.NOISE,57d)};
            _70ee50000deaWifi=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,60d)};
            _70ee50000deaFirmware=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,135d)};
            last030000000216Temperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,22.5d)};
            last030000000216Humidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,68d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,62d)};
            last030000000216CarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,3232d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,3125d)};
            _030000000216Battery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5105d)};
            _030000000216Radio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,74d)};
            last03000000076eTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,22.3d)};
            last03000000076eHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,43d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,40d)};
            last03000000076eCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,3232d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,3148d)};
            _03000000076eBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5323d)};
            _03000000076eRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,59d)};
            last03000003fe8eTemperatures=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.TEMPERATURE,26.2d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.TEMPERATURE,25.2d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.TEMPERATURE,23.2d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.TEMPERATURE,22.2d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.TEMPERATURE,24.7d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.TEMPERATURE,26.8d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.TEMPERATURE,28.2d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.TEMPERATURE,29.5d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.TEMPERATURE,27.7d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.TEMPERATURE,26.3d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.TEMPERATURE,24.7d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.TEMPERATURE,23.1d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.TEMPERATURE,26.2d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.TEMPERATURE,27.8d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.TEMPERATURE,10.2d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.TEMPERATURE,11.2d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.TEMPERATURE,22.7d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.TEMPERATURE,24.8d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.TEMPERATURE,26.2d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.TEMPERATURE,28.5d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.TEMPERATURE,20.7d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.TEMPERATURE,26.3d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.TEMPERATURE,25.7d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.TEMPERATURE,23.1d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.TEMPERATURE,22.2d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.TEMPERATURE,28.8d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.TEMPERATURE,29d),
            };
            last03000003fe8eHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,67d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,61d)};
            last03000003fe8eCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,1725d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,1526d)};
            _03000003fe8eBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5331d)};
            _03000003fe8eRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,64d)};
            last0200000010baTemperatures=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.TEMPERATURE,27.2d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.TEMPERATURE,24.2d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.TEMPERATURE,26.2d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.TEMPERATURE,25.2d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.TEMPERATURE,20.7d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.TEMPERATURE,22.8d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.TEMPERATURE,23.2d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.TEMPERATURE,23.5d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.TEMPERATURE,23.7d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.TEMPERATURE,24.3d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.TEMPERATURE,24.7d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.TEMPERATURE,26.1d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.TEMPERATURE,25.2d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.TEMPERATURE,27.8d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.TEMPERATURE,19.2d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.TEMPERATURE,19.2d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.TEMPERATURE,20.7d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.TEMPERATURE,22.8d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.TEMPERATURE,23.2d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.TEMPERATURE,23.5d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.TEMPERATURE,23.7d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.TEMPERATURE,24.3d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.TEMPERATURE,24.7d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.TEMPERATURE,26.1d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.TEMPERATURE,25.2d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.TEMPERATURE,27.8d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.TEMPERATURE,24d),
            };
            for(Measure measure:last0200000010baTemperatures)
                DATABASE.addMeasures(_0200000010ba_TEMPERATURE,new Measure[]{new StandAloneMeasure(new Date(measure.getDate().getTime()-Duration.of(1).day()),MeasurementType.TEMPERATURE,measure.getValue()/1.5d+2d)});
            last0200000010baHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,69d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,66d)};
            _0200000010baBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5259d)};
            _0200000010baRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,79d)};
            last05000004152cRains=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.RAIN,.2d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.RAIN,.1d),
                new StandAloneMeasure(new Date(now-Duration.of(169).minute()),MeasurementType.RAIN,.1d),
                new StandAloneMeasure(new Date(now-Duration.of(158).minute()),MeasurementType.RAIN,.3d),
                new StandAloneMeasure(new Date(now-Duration.of(148).minute()),MeasurementType.RAIN,.2d),
                new StandAloneMeasure(new Date(now-Duration.of(137).minute()),MeasurementType.RAIN,.3d),
                new StandAloneMeasure(new Date(now-Duration.of(127).minute()),MeasurementType.RAIN,.2d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.RAIN,.1d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.RAIN,.3d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.RAIN,.2d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.RAIN,0d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.RAIN,0d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.RAIN,.1d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.RAIN,.2d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.RAIN,.7d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.RAIN,.6d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.RAIN,.5d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.RAIN,.3d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.RAIN,.7d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.RAIN,.4d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.RAIN,.5d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.RAIN,.6d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.RAIN,.7d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.RAIN,1.1d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.RAIN,.7d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.RAIN,.7d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.RAIN,.3d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.RAIN,.2d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.RAIN,.4d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.RAIN,.7d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.RAIN,.5d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.RAIN,.1d),
            };
            _05000004152cTotalRain=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.RAIN,12.4d)};
            _05000004152cBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5668d)};
            _05000004152cRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,71d)};
            last06000000729aWindStrengths=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.WIND_STRENGTH,8d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.WIND_STRENGTH,9d),
                new StandAloneMeasure(new Date(now-Duration.of(169).minute()),MeasurementType.WIND_STRENGTH,11d),
                new StandAloneMeasure(new Date(now-Duration.of(158).minute()),MeasurementType.WIND_STRENGTH,1d),
                new StandAloneMeasure(new Date(now-Duration.of(148).minute()),MeasurementType.WIND_STRENGTH,4d),
                new StandAloneMeasure(new Date(now-Duration.of(137).minute()),MeasurementType.WIND_STRENGTH,5d),
                new StandAloneMeasure(new Date(now-Duration.of(127).minute()),MeasurementType.WIND_STRENGTH,8d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.WIND_STRENGTH,8d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.WIND_STRENGTH,7d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.WIND_STRENGTH,6d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.WIND_STRENGTH,4d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.WIND_STRENGTH,2d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.WIND_STRENGTH,1d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.WIND_STRENGTH,2d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.WIND_STRENGTH,3d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.WIND_STRENGTH,4d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.WIND_STRENGTH,6d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.WIND_STRENGTH,8d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.WIND_STRENGTH,9d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.WIND_STRENGTH,11d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.WIND_STRENGTH,1d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.WIND_STRENGTH,4d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.WIND_STRENGTH,4d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.WIND_STRENGTH,8d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.WIND_STRENGTH,1d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.WIND_STRENGTH,1d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.WIND_STRENGTH,2d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.WIND_STRENGTH,3d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.WIND_STRENGTH,3d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.WIND_STRENGTH,2d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.WIND_STRENGTH,1d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.WIND_STRENGTH,8d),
            };
            last06000000729aWindAngles=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(169).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(158).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(148).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(137).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(127).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.WIND_ANGLE,220d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.WIND_ANGLE,220d),
            };
            last06000000729aGustStrengths=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.GUST_STRENGTH,15d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.GUST_STRENGTH,12d),
                new StandAloneMeasure(new Date(now-Duration.of(169).minute()),MeasurementType.GUST_STRENGTH,13d),
                new StandAloneMeasure(new Date(now-Duration.of(158).minute()),MeasurementType.GUST_STRENGTH,14d),
                new StandAloneMeasure(new Date(now-Duration.of(148).minute()),MeasurementType.GUST_STRENGTH,15d),
                new StandAloneMeasure(new Date(now-Duration.of(137).minute()),MeasurementType.GUST_STRENGTH,16d),
                new StandAloneMeasure(new Date(now-Duration.of(127).minute()),MeasurementType.GUST_STRENGTH,14d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.GUST_STRENGTH,11d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.GUST_STRENGTH,12d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.GUST_STRENGTH,13d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.GUST_STRENGTH,15d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.GUST_STRENGTH,16d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.GUST_STRENGTH,20d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.GUST_STRENGTH,22d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.GUST_STRENGTH,25d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.GUST_STRENGTH,12d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.GUST_STRENGTH,13d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.GUST_STRENGTH,14d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.GUST_STRENGTH,15d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.GUST_STRENGTH,16d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.GUST_STRENGTH,18d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.GUST_STRENGTH,19d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.GUST_STRENGTH,20d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.GUST_STRENGTH,18d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.GUST_STRENGTH,18d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.GUST_STRENGTH,16d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.GUST_STRENGTH,16d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.GUST_STRENGTH,14d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.GUST_STRENGTH,13d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.GUST_STRENGTH,11d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.GUST_STRENGTH,8d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.GUST_STRENGTH,12d),
            };
            last06000000729aGustAngles=new Measure[]
            {
                new StandAloneMeasure(new Date(now-Duration.of(181).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(179).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(169).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(158).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(148).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(137).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(127).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(121).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(116).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(111).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(106).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(101).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(96).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(91).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(86).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(81).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(76).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(71).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(66).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(61).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(56).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(51).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(46).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(41).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(36).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(31).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(26).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(21).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(16).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(11).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(6).minute()),MeasurementType.GUST_ANGLE,200d),
                new StandAloneMeasure(new Date(now-Duration.of(1).minute()),MeasurementType.GUST_ANGLE,200d),
            };
            _06000000729aMaxGustStrength=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,42d)};
            _06000000729aMaxGustAngle=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_ANGLE,238d)};
            _06000000729aBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,4819d)};
            _06000000729aRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,95d)};
        }
        Map<String,Measure[]> lastMeasures=new HashMap<>();
        lastMeasures.put(_70ee50000dea_TEMPERATURE,last70ee50000deaTemperatures);
        lastMeasures.put(_70ee50000dea_HUMIDITY,last70ee50000deaHumidities);
        lastMeasures.put(_70ee50000dea_PRESSURE,last70ee50000deaPressures);
        lastMeasures.put(_70ee50000dea_CARBON_DIOXYDE,last70ee50000deaCarbonDioxydes);
        lastMeasures.put(_70ee50000dea_NOISE,last70ee50000deaNoises);
        lastMeasures.put(_70ee50000dea_WIFI,_70ee50000deaWifi);
        lastMeasures.put(_70ee50000dea_FIRMWARE,_70ee50000deaFirmware);
        lastMeasures.put(_030000000216_TEMPERATURE,last030000000216Temperatures);
        lastMeasures.put(_030000000216_HUMIDITY,last030000000216Humidities);
        lastMeasures.put(_030000000216_CARBON_DIOXYDE,last030000000216CarbonDioxydes);
        lastMeasures.put(_030000000216_BATTERY,_030000000216Battery);
        lastMeasures.put(_030000000216_RADIO,_030000000216Radio);
        lastMeasures.put(_03000000076e_TEMPERATURE,last03000000076eTemperatures);
        lastMeasures.put(_03000000076e_HUMIDITY,last03000000076eHumidities);
        lastMeasures.put(_03000000076e_CARBON_DIOXYDE,last03000000076eCarbonDioxydes);
        lastMeasures.put(_03000000076e_BATTERY,_03000000076eBattery);
        lastMeasures.put(_03000000076e_RADIO,_03000000076eRadio);
        lastMeasures.put(_03000003fe8e_TEMPERATURE,last03000003fe8eTemperatures);
        lastMeasures.put(_03000003fe8e_HUMIDITY,last03000003fe8eHumidities);
        lastMeasures.put(_03000003fe8e_CARBON_DIOXYDE,last03000003fe8eCarbonDioxydes);
        lastMeasures.put(_03000003fe8e_BATTERY,_03000003fe8eBattery);
        lastMeasures.put(_03000003fe8e_RADIO,_03000003fe8eRadio);
        lastMeasures.put(_0200000010ba_TEMPERATURE,last0200000010baTemperatures);
        lastMeasures.put(_0200000010ba_HUMIDITY,last0200000010baHumidities);
        lastMeasures.put(_0200000010ba_BATTERY,_0200000010baBattery);
        lastMeasures.put(_0200000010ba_RADIO,_0200000010baRadio);
        lastMeasures.put(_05000004152c_RAIN,last05000004152cRains);
        lastMeasures.put(_05000004152c_TOTAL_RAIN,_05000004152cTotalRain);
        lastMeasures.put(_05000004152c_BATTERY,_05000004152cBattery);
        lastMeasures.put(_05000004152c_RADIO,_05000004152cRadio);
        lastMeasures.put(_06000000729a_WIND_STRENGTH,last06000000729aWindStrengths);
        lastMeasures.put(_06000000729a_WIND_ANGLE,last06000000729aWindAngles);
        lastMeasures.put(_06000000729a_GUST_STRENGTH,last06000000729aGustStrengths);
        lastMeasures.put(_06000000729a_GUST_ANGLE,last06000000729aGustAngles);
        lastMeasures.put(_06000000729a_MAX_GUST_STRENGTH,_06000000729aMaxGustStrength);
        lastMeasures.put(_06000000729a_MAX_GUST_ANGLE,_06000000729aMaxGustAngle);
        lastMeasures.put(_06000000729a_BATTERY,_06000000729aBattery);
        lastMeasures.put(_06000000729a_RADIO,_06000000729aRadio);
        return lastMeasures;
    }

    public static Measure getLastMeasure(Map<String,Measure[]> lastMeasures,String measureKind)
    {
        Measure[] array=lastMeasures.get(measureKind);
        return array!=null&&array.length>=1?array[array.length-1]:null;
    }

    public static Measure[] filterMeasures(Measure[] measures,MeasurementType measurementType)
    {
        return Arrays.stream(measures)
                .filter(t->t.getMeasurementType()==measurementType)
                .sorted(CHRONOLOGICAL_SORTER)
                .toArray(Measure[]::new);
    }

    public static Measure[] filterTimedWindowMeasures(Measure[] measures,int hour)
    {
        if(measures==null)
            return null;
        return Arrays.stream(measures)
                .filter(measure->measures[measures.length-1].getDate().getTime()-measure.getDate().getTime()<Duration.of(hour).hour())
                .toArray(Measure[]::new);
    }

    public static double getHourlyVariation(Measure[] lastMeasures)
    {
        if(lastMeasures!=null&&lastMeasures.length>=2)
            return (lastMeasures[lastMeasures.length-1].getValue()-lastMeasures[0].getValue())/((double)(lastMeasures[lastMeasures.length-1].getDate().getTime()-lastMeasures[0].getDate().getTime())/3_600_000d);
        else
            return Double.NaN;
    }

    public static Measure estimate(Measure[] measures)
    {
        if(measures==null||measures.length<1)
            return null;
        try
        {
            long now=System.currentTimeMillis();
            Measure[] filteredMeasures=Arrays.stream(measures)
                    .filter(measure->measures[measures.length-1].getDate().getTime()-measure.getDate().getTime()<MEASURE_DELAY)
                    .toArray(Measure[]::new);
            List<WeightedObservedPoint> points=new ArrayList<>(filteredMeasures.length);
            for(int index=0;index<filteredMeasures.length;index++)
                points.add(new WeightedObservedPoint(index==filteredMeasures.length-1?1_000d:1d,(double)filteredMeasures[index].getDate().getTime(),filteredMeasures[index].getValue()));
            while(points.size()>4)
                points.remove(0);
            List<WeightedObservedPoint> correctedPoints=new ArrayList<>(points.size());
            points.forEach(point->correctedPoints.add(new WeightedObservedPoint(point.getWeight(),(point.getX()-points.get(0).getX())/60_000d,point.getY())));
            int degree=Math.min(1,correctedPoints.size()-1);
            double[] result=PolynomialCurveFitter.create(degree)
                    .withMaxIterations(1_000)
                    .fit(correctedPoints);
            double time=((double)now-points.get(0).getX())/60_000d;
            double value=0d;
            for(int index=0;index<result.length;index++)
                value+=result[index]*Math.pow(time,(double)index);
            return new StandAloneMeasure(new Date(now),measures[measures.length-1].getMeasurementType(),value);
        }
        catch(Exception e)
        {
            return new StandAloneMeasure(new Date(),measures[measures.length-1].getMeasurementType(),measures[measures.length-1].getValue());
        }
    }
}
