package sky.epaperscreenupdater;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import sky.netatmo.Device;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementScale;
import sky.netatmo.MeasurementType;
import sky.netatmo.Module;
import sky.netatmo.NetatmoException;
import sky.netatmo.Token;
import sky.program.Duration;

public abstract class AbstractNetatmoPage extends AbstractSinglePage
{
    private static Token token=null;
    private static long lastNetatmoVerificationTime=0L;
    private static Measure[] lastSalonTemperatures=null;
    private static Measure[] lastSalonHumidities=null;
    private static Measure[] lastSalonPressures=null;
    private static Measure[] lastSalonCarbonDioxydes=null;
    private static Measure[] lastSalonNoises=null;
    private static Measure[] salonWifi=null;
    private static Measure[] salonFirmware=null;
    private static Measure[] lastChambreTemperatures=null;
    private static Measure[] lastChambreHumidities=null;
    private static Measure[] lastChambreCarbonDioxydes=null;
    private static Measure[] chambreBattery=null;
    private static Measure[] chambreRadio=null;
    private static Measure[] lastSalleDeBainTemperatures=null;
    private static Measure[] lastSalleDeBainHumidities=null;
    private static Measure[] lastSalleDeBainCarbonDioxydes=null;
    private static Measure[] salleDeBainBattery=null;
    private static Measure[] salleDeBainRadio=null;
    private static Measure[] lastSousSolTemperatures=null;
    private static Measure[] lastSousSolHumidities=null;
    private static Measure[] lastSousSolCarbonDioxydes=null;
    private static Measure[] sousSolBattery=null;
    private static Measure[] sousSolRadio=null;
    private static Measure[] lastJardinTemperatures=null;
    private static Measure[] lastJardinHumidities=null;
    private static Measure[] jardinBattery=null;
    private static Measure[] jardinRadio=null;
    private static Measure[] lastPluviometreRains=null;
    private static Measure[] pluviometreTotalRain=null;
    private static Measure[] pluviometreBattery=null;
    private static Measure[] pluviometreRadio=null;
    private static Measure[] lastAnemometreWindStrengths=null;
    private static Measure[] lastAnemometreWindAngles=null;
    private static Measure[] lastAnemometreGustStrengths=null;
    private static Measure[] lastAnemometreGustAngles=null;
    private static Measure[] anemometreMaxGustStrength=null;
    private static Measure[] anemometreMaxGustAngle=null;
    private static Measure[] anemometreBattery=null;
    private static Measure[] anemometreRadio=null;
    protected static final String SALON_TEMPERATURE="salonTemperature";
    protected static final String SALON_HUMIDITY="salonHumidity";
    protected static final String SALON_PRESSURE="salonPressure";
    protected static final String SALON_CARBON_DIOXYDE="salonCarbonDioxyde";
    protected static final String SALON_NOISE="salonNoise";
    protected static final String SALON_WIFI="salonWifi";
    protected static final String SALON_FIRMWARE="salonFirmware";
    protected static final String CHAMBRE_TEMPERATURE="chambreTemperature";
    protected static final String CHAMBRE_HUMIDITY="chambreHumidity";
    protected static final String CHAMBRE_CARBON_DIOXYDE="chambreCarbonDioxyde";
    protected static final String CHAMBRE_BATTERY="chambreBattery";
    protected static final String CHAMBRE_RADIO="chambreRadio";
    protected static final String SALLE_DE_BAIN_TEMPERATURE="salleDeBainTemperature";
    protected static final String SALLE_DE_BAIN_HUMIDITY="salleDeBainHumidity";
    protected static final String SALLE_DE_BAIN_CARBON_DIOXYDE="salleDeBainCarbonDioxyde";
    protected static final String SALLE_DE_BAIN_BATTERY="salleDeBainBattery";
    protected static final String SALLE_DE_BAIN_RADIO="salleDeBainRadio";
    protected static final String SOUS_SOL_TEMPERATURE="sousSolTemperature";
    protected static final String SOUS_SOL_HUMIDITY="sousSolHumidity";
    protected static final String SOUS_SOL_CARBON_DIOXYDE="sousSolCarbonDioxyde";
    protected static final String SOUS_SOL_BATTERY="sousSolBattery";
    protected static final String SOUS_SOL_RADIO="sousSolRadio";
    protected static final String JARDIN_TEMPERATURE="jardinTemperature";
    protected static final String JARDIN_HUMIDITY="jardinHumidity";
    protected static final String JARDIN_BATTERY="jardinBattery";
    protected static final String JARDIN_RADIO="jardinRadio";
    protected static final String PLUVIOMETRE_RAIN="pluviometreRain";
    protected static final String PLUVIOMETRE_TOTAL_RAIN="pluviometreTotalRain";
    protected static final String PLUVIOMETRE_BATTERY="pluviometreBattery";
    protected static final String PLUVIOMETRE_RADIO="pluviometreRadio";
    protected static final String ANEMOMETRE_WIND_STRENGTH="anemometreWindStrength";
    protected static final String ANEMOMETRE_WIND_ANGLE="anemometreWindAngle";
    protected static final String ANEMOMETRE_GUST_STRENGTH="anemometreGustStrength";
    protected static final String ANEMOMETRE_GUST_ANGLE="anemometreGustAngle";
    protected static final String ANEMOMETRE_MAX_GUST_STRENGTH="anemometreMaxGustStrength";
    protected static final String ANEMOMETRE_MAX_GUST_ANGLE="anemometreMaxGustAngle";
    protected static final String ANEMOMETRE_BATTERY="anemometreBattery";
    protected static final String ANEMOMETRE_RADIO="anemometreRadio";
    private static final Comparator<Measure> MEASURE_COMPARATOR=(o1,o2)->Long.compare(o1.getDate().getTime(),o2.getDate().getTime());
    protected static final MeasureDatabase DATABASE=new MeasureDatabase();
    protected static final boolean NETATMO_ENABLED=true;

    protected AbstractNetatmoPage(Page parentPage)
    {
        super(parentPage);
    }

    protected static synchronized Map<String,Measure[]> getLastMeasures()
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
                        Device salon=homeDevice;//70:ee:50:00:0d:ea
                        JsonObject salonObject=salon.getAttributes();
                        salonWifi=new Measure[]{new StandAloneMeasure(new Date(now),null,salonObject.get("wifi_status").getAsDouble())};
                        salonFirmware=new Measure[]{new StandAloneMeasure(new Date(now),null,salonObject.get("firmware").getAsDouble())};
                        Module[] homeModules=homeDevice.getModules();
                        Module jardin=homeModules[0];//02:00:00:00:10:ba
                        JsonObject jardinObject=jardin.getAttributes();
                        jardinBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,jardinObject.get("battery_vp").getAsDouble())};
                        jardinRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,jardinObject.get("rf_status").getAsDouble())};
                        Module chambre=homeModules[1];//03:00:00:00:02:16
                        JsonObject chambreObject=chambre.getAttributes();
                        chambreBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,chambreObject.get("battery_vp").getAsDouble())};
                        chambreRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,chambreObject.get("rf_status").getAsDouble())};
                        Module salleDeBain=homeModules[2];//03:00:00:00:07:6e
                        JsonObject salleDeBainObject=salleDeBain.getAttributes();
                        salleDeBainBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,salleDeBainObject.get("battery_vp").getAsDouble())};
                        salleDeBainRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,salleDeBainObject.get("rf_status").getAsDouble())};
                        Module anemometre=homeModules[3];//06:00:00:00:72:9a
                        JsonObject anemometreObject=anemometre.getAttributes();
                        JsonObject anemometreDashboardDataObject=anemometreObject.get("dashboard_data").getAsJsonObject();
                        anemometreMaxGustStrength=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,anemometreDashboardDataObject.get("max_wind_str").getAsDouble())};
                        anemometreMaxGustAngle=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_ANGLE,anemometreDashboardDataObject.get("max_wind_angle").getAsDouble())};
                        anemometreBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,anemometreObject.get("battery_vp").getAsDouble())};
                        anemometreRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,anemometreObject.get("rf_status").getAsDouble())};
                        Module sousSol=homeModules[4];//03:00:00:03:fe:8e
                        JsonObject sousSolObject=sousSol.getAttributes();
                        sousSolBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,sousSolObject.get("battery_vp").getAsDouble())};
                        sousSolRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,sousSolObject.get("rf_status").getAsDouble())};
                        Module pluviometre=homeModules[5];//05:00:00:04:15:2c
                        JsonObject pluviometreObject=pluviometre.getAttributes();
                        JsonObject pluviometreDashboardDataObject=pluviometreObject.get("dashboard_data").getAsJsonObject();
                        pluviometreTotalRain=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.RAIN,pluviometreDashboardDataObject.get("sum_rain_24").getAsDouble())};
                        pluviometreBattery=new Measure[]{new StandAloneMeasure(new Date(now),null,pluviometreObject.get("battery_vp").getAsDouble())};
                        pluviometreRadio=new Measure[]{new StandAloneMeasure(new Date(now),null,pluviometreObject.get("rf_status").getAsDouble())};
                        Measure[] salonMeasures=salon.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2,MeasurementType.NOISE);
                        Measure[] salonPressureMeasures=salon.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hourPlus(15).minute()),new Date(now+Duration.of(5).minute()),MeasurementType.PRESSURE);
                        Measure[] jardinMeasures=jardin.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY);
                        Measure[] chambreMeasures=chambre.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                        Measure[] salleDeBainMeasures=salleDeBain.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                        Measure[] anemometreMeasures=anemometre.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.WIND_STRENGTH,MeasurementType.WIND_ANGLE,MeasurementType.GUST_STRENGTH,MeasurementType.GUST_ANGLE);
                        Measure[] sousSolMeasures=sousSol.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                        Measure[] pluviometreMeasures=pluviometre.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(3).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.RAIN);
                        lastSalonTemperatures=compileMeasures(salonMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(SALON_TEMPERATURE,lastSalonTemperatures);
                        lastSalonHumidities=compileMeasures(salonMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(SALON_HUMIDITY,lastSalonHumidities);
                        lastSalonPressures=compileMeasures(salonPressureMeasures,MeasurementType.PRESSURE);
                        DATABASE.addMeasures(SALON_PRESSURE,lastSalonPressures);
                        lastSalonCarbonDioxydes=compileMeasures(salonMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(SALON_CARBON_DIOXYDE,lastSalonCarbonDioxydes);
                        lastSalonNoises=compileMeasures(salonMeasures,MeasurementType.NOISE);
                        DATABASE.addMeasures(SALON_NOISE,lastSalonNoises);
                        lastChambreTemperatures=compileMeasures(chambreMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(CHAMBRE_TEMPERATURE,lastChambreTemperatures);
                        lastChambreHumidities=compileMeasures(chambreMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(CHAMBRE_HUMIDITY,lastChambreHumidities);
                        lastChambreCarbonDioxydes=compileMeasures(chambreMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(CHAMBRE_CARBON_DIOXYDE,lastChambreCarbonDioxydes);
                        lastSalleDeBainTemperatures=compileMeasures(salleDeBainMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(SALLE_DE_BAIN_TEMPERATURE,lastSalleDeBainTemperatures);
                        lastSalleDeBainHumidities=compileMeasures(salleDeBainMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(SALLE_DE_BAIN_HUMIDITY,lastSalleDeBainHumidities);
                        lastSalleDeBainCarbonDioxydes=compileMeasures(salleDeBainMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(SALLE_DE_BAIN_CARBON_DIOXYDE,lastSalleDeBainCarbonDioxydes);
                        lastSousSolTemperatures=compileMeasures(sousSolMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(SOUS_SOL_TEMPERATURE,lastSousSolTemperatures);
                        lastSousSolHumidities=compileMeasures(sousSolMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(SOUS_SOL_HUMIDITY,lastSousSolHumidities);
                        lastSousSolCarbonDioxydes=compileMeasures(sousSolMeasures,MeasurementType.CO2);
                        DATABASE.addMeasures(SOUS_SOL_CARBON_DIOXYDE,lastSousSolCarbonDioxydes);
                        lastJardinTemperatures=compileMeasures(jardinMeasures,MeasurementType.TEMPERATURE);
                        DATABASE.addMeasures(JARDIN_TEMPERATURE,lastJardinTemperatures);
                        lastJardinHumidities=compileMeasures(jardinMeasures,MeasurementType.HUMIDITY);
                        DATABASE.addMeasures(JARDIN_HUMIDITY,lastJardinHumidities);
                        lastPluviometreRains=compileMeasures(pluviometreMeasures,MeasurementType.RAIN);
                        DATABASE.addMeasures(PLUVIOMETRE_RAIN,lastPluviometreRains);
                        lastAnemometreWindStrengths=compileMeasures(anemometreMeasures,MeasurementType.WIND_STRENGTH);
                        DATABASE.addMeasures(ANEMOMETRE_WIND_STRENGTH,lastAnemometreWindStrengths);
                        lastAnemometreWindAngles=compileMeasures(anemometreMeasures,MeasurementType.WIND_ANGLE);
                        DATABASE.addMeasures(ANEMOMETRE_WIND_ANGLE,lastAnemometreWindAngles);
                        lastAnemometreGustStrengths=compileMeasures(anemometreMeasures,MeasurementType.GUST_STRENGTH);
                        DATABASE.addMeasures(ANEMOMETRE_GUST_STRENGTH,lastAnemometreGustStrengths);
                        lastAnemometreGustAngles=compileMeasures(anemometreMeasures,MeasurementType.GUST_ANGLE);
                        DATABASE.addMeasures(ANEMOMETRE_GUST_ANGLE,lastAnemometreGustAngles);
                        DATABASE.clean();
                        ok=true;
                        if(lastSalonTemperatures!=null&&lastSalonTemperatures.length>0)
                        {
                            long lastMeasureTime=lastSalonTemperatures[lastSalonTemperatures.length-1].getDate().getTime();
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
                }
                if(!ok)
                {
                    lastSalonTemperatures=null;
                    lastSalonHumidities=null;
                    lastSalonPressures=null;
                    lastSalonCarbonDioxydes=null;
                    lastSalonNoises=null;
                    salonWifi=null;
                    salonFirmware=null;
                    lastChambreTemperatures=null;
                    lastChambreHumidities=null;
                    lastChambreCarbonDioxydes=null;
                    chambreBattery=null;
                    chambreRadio=null;
                    lastSalleDeBainTemperatures=null;
                    lastSalleDeBainHumidities=null;
                    lastSalleDeBainCarbonDioxydes=null;
                    salleDeBainBattery=null;
                    salleDeBainRadio=null;
                    lastSousSolTemperatures=null;
                    lastSousSolHumidities=null;
                    lastSousSolCarbonDioxydes=null;
                    sousSolBattery=null;
                    sousSolRadio=null;
                    lastJardinTemperatures=null;
                    lastJardinHumidities=null;
                    jardinBattery=null;
                    jardinRadio=null;
                    lastPluviometreRains=null;
                    pluviometreTotalRain=null;
                    pluviometreBattery=null;
                    pluviometreRadio=null;
                    lastAnemometreWindStrengths=null;
                    lastAnemometreWindAngles=null;
                    lastAnemometreGustStrengths=null;
                    lastAnemometreGustAngles=null;
                    anemometreMaxGustStrength=null;
                    anemometreMaxGustAngle=null;
                    anemometreBattery=null;
                    anemometreRadio=null;
                }
            }
        }
        else
        {
            lastSalonTemperatures=new Measure[]
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
            lastSalonHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,65d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,60d)};
            lastSalonPressures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(2).hour()),MeasurementType.PRESSURE,1032.7d),new StandAloneMeasure(new Date(now),MeasurementType.PRESSURE,1030.2d)};
            lastSalonCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,4687d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,4235d)};
            lastSalonNoises=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.NOISE,60d),new StandAloneMeasure(new Date(now),MeasurementType.NOISE,57d)};
            salonWifi=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,60d)};
            salonFirmware=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,135d)};
            lastChambreTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,22.5d)};
            lastChambreHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,68d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,62d)};
            lastChambreCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,3232d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,3125d)};
            chambreBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5105d)};
            chambreRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,74d)};
            lastSalleDeBainTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,22.3d)};
            lastSalleDeBainHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,43d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,40d)};
            lastSalleDeBainCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,3232d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,3148d)};
            salleDeBainBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5323d)};
            salleDeBainRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,59d)};
            lastSousSolTemperatures=new Measure[]
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
            lastSousSolHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,67d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,61d)};
            lastSousSolCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,1725d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,1526d)};
            sousSolBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5331d)};
            sousSolRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,64d)};
            lastJardinTemperatures=new Measure[]
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
            for(Measure measure:lastJardinTemperatures)
                DATABASE.addMeasures(JARDIN_TEMPERATURE,new Measure[]{new StandAloneMeasure(new Date(measure.getDate().getTime()-Duration.of(1).day()),MeasurementType.TEMPERATURE,measure.getValue()/1.5d+2d)});
            lastJardinHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,69d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,66d)};
            jardinBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5259d)};
            jardinRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,79d)};
            lastPluviometreRains=new Measure[]
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
            pluviometreTotalRain=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.RAIN,12.4d)};
            pluviometreBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,5668d)};
            pluviometreRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,71d)};
            lastAnemometreWindStrengths=new Measure[]
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
            lastAnemometreWindAngles=new Measure[]
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
            lastAnemometreGustStrengths=new Measure[]
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
            lastAnemometreGustAngles=new Measure[]
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
            anemometreMaxGustStrength=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,42d)};
            anemometreMaxGustAngle=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_ANGLE,238d)};
            anemometreBattery=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,4819d)};
            anemometreRadio=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).minute()),null,95d)};
        }
        Map<String,Measure[]> lastMeasures=new HashMap<>();
        lastMeasures.put(SALON_TEMPERATURE,lastSalonTemperatures);
        lastMeasures.put(SALON_HUMIDITY,lastSalonHumidities);
        lastMeasures.put(SALON_PRESSURE,lastSalonPressures);
        lastMeasures.put(SALON_CARBON_DIOXYDE,lastSalonCarbonDioxydes);
        lastMeasures.put(SALON_NOISE,lastSalonNoises);
        lastMeasures.put(SALON_WIFI,salonWifi);
        lastMeasures.put(SALON_FIRMWARE,salonFirmware);
        lastMeasures.put(CHAMBRE_TEMPERATURE,lastChambreTemperatures);
        lastMeasures.put(CHAMBRE_HUMIDITY,lastChambreHumidities);
        lastMeasures.put(CHAMBRE_CARBON_DIOXYDE,lastChambreCarbonDioxydes);
        lastMeasures.put(CHAMBRE_BATTERY,chambreBattery);
        lastMeasures.put(CHAMBRE_RADIO,chambreRadio);
        lastMeasures.put(SALLE_DE_BAIN_TEMPERATURE,lastSalleDeBainTemperatures);
        lastMeasures.put(SALLE_DE_BAIN_HUMIDITY,lastSalleDeBainHumidities);
        lastMeasures.put(SALLE_DE_BAIN_CARBON_DIOXYDE,lastSalleDeBainCarbonDioxydes);
        lastMeasures.put(SALLE_DE_BAIN_BATTERY,salleDeBainBattery);
        lastMeasures.put(SALLE_DE_BAIN_RADIO,salleDeBainRadio);
        lastMeasures.put(SOUS_SOL_TEMPERATURE,lastSousSolTemperatures);
        lastMeasures.put(SOUS_SOL_HUMIDITY,lastSousSolHumidities);
        lastMeasures.put(SOUS_SOL_CARBON_DIOXYDE,lastSousSolCarbonDioxydes);
        lastMeasures.put(SOUS_SOL_BATTERY,sousSolBattery);
        lastMeasures.put(SOUS_SOL_RADIO,sousSolRadio);
        lastMeasures.put(JARDIN_TEMPERATURE,lastJardinTemperatures);
        lastMeasures.put(JARDIN_HUMIDITY,lastJardinHumidities);
        lastMeasures.put(JARDIN_BATTERY,jardinBattery);
        lastMeasures.put(JARDIN_RADIO,jardinRadio);
        lastMeasures.put(PLUVIOMETRE_RAIN,lastPluviometreRains);
        lastMeasures.put(PLUVIOMETRE_TOTAL_RAIN,pluviometreTotalRain);
        lastMeasures.put(PLUVIOMETRE_BATTERY,pluviometreBattery);
        lastMeasures.put(PLUVIOMETRE_RADIO,pluviometreRadio);
        lastMeasures.put(ANEMOMETRE_WIND_STRENGTH,lastAnemometreWindStrengths);
        lastMeasures.put(ANEMOMETRE_WIND_ANGLE,lastAnemometreWindAngles);
        lastMeasures.put(ANEMOMETRE_GUST_STRENGTH,lastAnemometreGustStrengths);
        lastMeasures.put(ANEMOMETRE_GUST_ANGLE,lastAnemometreGustAngles);
        lastMeasures.put(ANEMOMETRE_MAX_GUST_STRENGTH,anemometreMaxGustStrength);
        lastMeasures.put(ANEMOMETRE_MAX_GUST_ANGLE,anemometreMaxGustAngle);
        lastMeasures.put(ANEMOMETRE_BATTERY,anemometreBattery);
        lastMeasures.put(ANEMOMETRE_RADIO,anemometreRadio);
        return lastMeasures;
    }

    private static Measure[] compileMeasures(Measure[] rawMeasures,MeasurementType measurementType)
    {
        return Arrays.stream(rawMeasures)
                .filter(t->t.getMeasurementType()==measurementType)
                .sorted(MEASURE_COMPARATOR)
                .toArray(Measure[]::new);
    }
}
