package sky.epaperscreenupdater;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public abstract class AbstractNetatmoPage extends AbstractPage
{
    private static Token token=null;
    private static long lastNetatmoVerificationTime=0L;
    private static Measure[] lastSalonTemperatures=null;
    private static Measure[] lastSalonHumidities=null;
    private static Measure[] lastSalonPressures=null;
    private static Measure[] lastSalonCarbonDioxydes=null;
    private static Measure[] lastSalonNoises=null;
    private static Measure[] lastChambreTemperatures=null;
    private static Measure[] lastChambreHumidities=null;
    private static Measure[] lastChambreCarbonDioxydes=null;
    private static Measure[] lastSalleDeBainTemperatures=null;
    private static Measure[] lastSalleDeBainHumidities=null;
    private static Measure[] lastSalleDeBainCarbonDioxydes=null;
    private static Measure[] lastSousSolTemperatures=null;
    private static Measure[] lastSousSolHumidities=null;
    private static Measure[] lastSousSolCarbonDioxydes=null;
    private static Measure[] lastJardinTemperatures=null;
    private static Measure[] lastJardinHumidities=null;
    private static Measure[] lastPluviometreRains=null;
    private static Measure[] pluviometreTotalRain=null;
    private static Measure[] lastAnemometreWindStrengths=null;
    private static Measure[] lastAnemometreWindAngles=null;
    private static Measure[] lastAnemometreGustStrengths=null;
    private static Measure[] lastAnemometreGustAngles=null;
    private static Measure[] anemometreMaxGustStrength=null;
    private static Measure[] anemometreMaxGustAngle=null;
    protected static final String SALON_TEMPERATURE="salonTemperature";
    protected static final String SALON_HUMIDITY="salonHumidity";
    protected static final String SALON_PRESSURE="salonPressure";
    protected static final String SALON_CARBON_DIOXYDE="salonCarbonDioxyde";
    protected static final String SALON_NOISE="salonNoise";
    protected static final String CHAMBRE_TEMPERATURE="chambreTemperature";
    protected static final String CHAMBRE_HUMIDITY="chambreHumidity";
    protected static final String CHAMBRE_CARBON_DIOXYDE="chambreCarbonDioxyde";
    protected static final String SALLE_DE_BAIN_TEMPERATURE="salleDeBainTemperature";
    protected static final String SALLE_DE_BAIN_HUMIDITY="salleDeBainHumidity";
    protected static final String SALLE_DE_BAIN_CARBON_DIOXYDE="salleDeBainCarbonDioxyde";
    protected static final String SOUS_SOL_TEMPERATURE="sousSolTemperature";
    protected static final String SOUS_SOL_HUMIDITY="sousSolHumidity";
    protected static final String SOUS_SOL_CARBON_DIOXYDE="sousSolCarbonDioxyde";
    protected static final String JARDIN_TEMPERATURE="jardinTemperature";
    protected static final String JARDIN_HUMIDITY="jardinHumidity";
    protected static final String PLUVIOMETRE_RAIN="pluviometreRain";
    protected static final String PLUVIOMETRE_TOTAL_RAIN="pluviometreTotalRain";
    protected static final String ANEMOMETRE_WIND_STRENGTH="anemometreWindStrength";
    protected static final String ANEMOMETRE_WIND_ANGLE="anemometreWindAngle";
    protected static final String ANEMOMETRE_GUST_STRENGTH="anemometreGustStrength";
    protected static final String ANEMOMETRE_GUST_ANGLE="anemometreGustAngle";
    protected static final String ANEMOMETRE_MAX_GUST_STRENGTH="anemometreMaxGustStrength";
    protected static final String ANEMOMETRE_MAX_GUST_ANGLE="anemometreMaxGustAngle";
    private static final Comparator<Measure> MEASURE_COMPARATOR=(o1,o2)->Long.compare(o1.getDate().getTime(),o2.getDate().getTime());
    protected static final boolean NETATMO_ENABLED=true;

    protected static synchronized Map<String,Measure[]> getLastMeasures()
    {
        long now=System.currentTimeMillis();
        if(NETATMO_ENABLED&&now-lastNetatmoVerificationTime>Duration.of(10).minute())
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
                    Module[] homeModules=homeDevice.getModules();
                    Module jardin=homeModules[0];//02:00:00:00:10:ba
                    Module chambre=homeModules[1];//03:00:00:00:02:16
                    Module salleDeBain=homeModules[2];//03:00:00:00:07:6e
                    Module anemometre=homeModules[3];//06:00:00:00:72:9a
                    JsonObject anemometreDashboardDataObject=anemometre.getAttributes().get("dashboard_data").getAsJsonObject();
                    anemometreMaxGustStrength=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,anemometreDashboardDataObject.get("max_wind_str").getAsDouble())};
                    anemometreMaxGustAngle=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,anemometreDashboardDataObject.get("max_wind_angle").getAsDouble())};
                    Module sousSol=homeModules[4];//03:00:00:03:fe:8e
                    Module pluviometre=homeModules[5];//05:00:00:04:15:2c
                    JsonObject pluviometreDashboardDataObject=pluviometre.getAttributes().get("dashboard_data").getAsJsonObject();
                    pluviometreTotalRain=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.RAIN,pluviometreDashboardDataObject.get("sum_rain_24").getAsDouble())};
                    Measure[] salonMeasures=salon.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2,MeasurementType.NOISE);
                    Measure[] salonPressureMeasures=salon.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(2).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.PRESSURE);
                    Measure[] jardinMeasures=jardin.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY);
                    Measure[] chambreMeasures=chambre.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                    Measure[] salleDeBainMeasures=salleDeBain.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                    Measure[] anemometreMeasures=anemometre.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.WIND_STRENGTH,MeasurementType.WIND_ANGLE,MeasurementType.GUST_STRENGTH,MeasurementType.GUST_ANGLE);
                    Measure[] sousSolMeasures=sousSol.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.TEMPERATURE,MeasurementType.HUMIDITY,MeasurementType.CO2);
                    Measure[] pluviometreMeasures=pluviometre.getMeasures(MeasurementScale.MAX,new Date(now-Duration.of(1).hour()),new Date(now+Duration.of(5).minute()),MeasurementType.RAIN);
                    lastSalonTemperatures=compileMeasures(salonMeasures,MeasurementType.TEMPERATURE);
                    lastSalonHumidities=compileMeasures(salonMeasures,MeasurementType.HUMIDITY);
                    lastSalonPressures=compileMeasures(salonPressureMeasures,MeasurementType.PRESSURE);
                    lastSalonCarbonDioxydes=compileMeasures(salonMeasures,MeasurementType.CO2);
                    lastSalonNoises=compileMeasures(salonMeasures,MeasurementType.NOISE);
                    lastChambreTemperatures=compileMeasures(chambreMeasures,MeasurementType.TEMPERATURE);
                    lastChambreHumidities=compileMeasures(chambreMeasures,MeasurementType.HUMIDITY);
                    lastChambreCarbonDioxydes=compileMeasures(chambreMeasures,MeasurementType.CO2);
                    lastSalleDeBainTemperatures=compileMeasures(salleDeBainMeasures,MeasurementType.TEMPERATURE);
                    lastSalleDeBainHumidities=compileMeasures(salleDeBainMeasures,MeasurementType.HUMIDITY);
                    lastSalleDeBainCarbonDioxydes=compileMeasures(salleDeBainMeasures,MeasurementType.CO2);
                    lastSousSolTemperatures=compileMeasures(sousSolMeasures,MeasurementType.TEMPERATURE);
                    lastSousSolHumidities=compileMeasures(sousSolMeasures,MeasurementType.HUMIDITY);
                    lastSousSolCarbonDioxydes=compileMeasures(sousSolMeasures,MeasurementType.CO2);
                    lastJardinTemperatures=compileMeasures(jardinMeasures,MeasurementType.TEMPERATURE);
                    lastJardinHumidities=compileMeasures(jardinMeasures,MeasurementType.HUMIDITY);
                    lastPluviometreRains=compileMeasures(pluviometreMeasures,MeasurementType.RAIN);
                    lastAnemometreWindStrengths=compileMeasures(anemometreMeasures,MeasurementType.WIND_STRENGTH);
                    lastAnemometreWindAngles=compileMeasures(anemometreMeasures,MeasurementType.WIND_ANGLE);
                    lastAnemometreGustStrengths=compileMeasures(anemometreMeasures,MeasurementType.GUST_STRENGTH);
                    lastAnemometreGustAngles=compileMeasures(anemometreMeasures,MeasurementType.GUST_ANGLE);
                    ok=true;
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
                lastChambreTemperatures=null;
                lastChambreHumidities=null;
                lastChambreCarbonDioxydes=null;
                lastSalleDeBainTemperatures=null;
                lastSalleDeBainHumidities=null;
                lastSalleDeBainCarbonDioxydes=null;
                lastSousSolTemperatures=null;
                lastSousSolHumidities=null;
                lastSousSolCarbonDioxydes=null;
                lastJardinTemperatures=null;
                lastJardinHumidities=null;
                lastPluviometreRains=null;
                lastAnemometreWindStrengths=null;
                lastAnemometreWindAngles=null;
                lastAnemometreGustStrengths=null;
                lastAnemometreGustAngles=null;
                pluviometreTotalRain=null;
                anemometreMaxGustStrength=null;
                anemometreMaxGustAngle=null;
            }
        }
        else
            if(!NETATMO_ENABLED)
            {
                lastSalonTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.1d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,19.4d)};
                lastSalonHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,65d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,60d)};
                lastSalonPressures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(2).hour()),MeasurementType.PRESSURE,1032.7d),new StandAloneMeasure(new Date(now),MeasurementType.PRESSURE,1030.2d)};
                lastSalonCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,4687d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,4235d)};
                lastSalonNoises=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.NOISE,60d),new StandAloneMeasure(new Date(now),MeasurementType.NOISE,57d)};
                lastChambreTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,22.5d)};
                lastChambreHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,68d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,62d)};
                lastChambreCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,3232d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,3125d)};
                lastSalleDeBainTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,22.3d)};
                lastSalleDeBainHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,43d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,40d)};
                lastSalleDeBainCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,3232d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,3148d)};
                lastSousSolTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,23.4d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,23.3d)};
                lastSousSolHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,67d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,61d)};
                lastSousSolCarbonDioxydes=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.CO2,1725d),new StandAloneMeasure(new Date(now),MeasurementType.CO2,1526d)};
                lastJardinTemperatures=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.TEMPERATURE,10.2d),new StandAloneMeasure(new Date(now),MeasurementType.TEMPERATURE,8.1d)};
                lastJardinHumidities=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.HUMIDITY,69d),new StandAloneMeasure(new Date(now),MeasurementType.HUMIDITY,66d)};
                lastPluviometreRains=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.RAIN,0d),new StandAloneMeasure(new Date(now),MeasurementType.RAIN,0d)};
                lastAnemometreWindStrengths=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.WIND_STRENGTH,34d),new StandAloneMeasure(new Date(now),MeasurementType.WIND_STRENGTH,34d)};
                lastAnemometreWindAngles=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.WIND_ANGLE,220d),new StandAloneMeasure(new Date(now),MeasurementType.WIND_ANGLE,220d)};
                lastAnemometreGustStrengths=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.GUST_STRENGTH,43d),new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,43d)};
                lastAnemometreGustAngles=new Measure[]{new StandAloneMeasure(new Date(now-Duration.of(1).hour()),MeasurementType.GUST_ANGLE,200d),new StandAloneMeasure(new Date(now),MeasurementType.GUST_ANGLE,200d)};
                pluviometreTotalRain=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.RAIN,12.4d)};
                anemometreMaxGustStrength=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_STRENGTH,42d)};
                anemometreMaxGustAngle=new Measure[]{new StandAloneMeasure(new Date(now),MeasurementType.GUST_ANGLE,238d)};
            }
        Map<String,Measure[]> lastMeasures=new HashMap<>();
        lastMeasures.put(SALON_TEMPERATURE,lastSalonTemperatures);
        lastMeasures.put(SALON_HUMIDITY,lastSalonHumidities);
        lastMeasures.put(SALON_PRESSURE,lastSalonPressures);
        lastMeasures.put(SALON_CARBON_DIOXYDE,lastSalonCarbonDioxydes);
        lastMeasures.put(SALON_NOISE,lastSalonNoises);
        lastMeasures.put(CHAMBRE_TEMPERATURE,lastChambreTemperatures);
        lastMeasures.put(CHAMBRE_HUMIDITY,lastChambreHumidities);
        lastMeasures.put(CHAMBRE_CARBON_DIOXYDE,lastChambreCarbonDioxydes);
        lastMeasures.put(SALLE_DE_BAIN_TEMPERATURE,lastSalleDeBainTemperatures);
        lastMeasures.put(SALLE_DE_BAIN_HUMIDITY,lastSalleDeBainHumidities);
        lastMeasures.put(SALLE_DE_BAIN_CARBON_DIOXYDE,lastSalleDeBainCarbonDioxydes);
        lastMeasures.put(SOUS_SOL_TEMPERATURE,lastSousSolTemperatures);
        lastMeasures.put(SOUS_SOL_HUMIDITY,lastSousSolHumidities);
        lastMeasures.put(SOUS_SOL_CARBON_DIOXYDE,lastSousSolCarbonDioxydes);
        lastMeasures.put(JARDIN_TEMPERATURE,lastJardinTemperatures);
        lastMeasures.put(JARDIN_HUMIDITY,lastJardinHumidities);
        lastMeasures.put(PLUVIOMETRE_RAIN,lastPluviometreRains);
        lastMeasures.put(ANEMOMETRE_WIND_STRENGTH,lastAnemometreWindStrengths);
        lastMeasures.put(ANEMOMETRE_WIND_ANGLE,lastAnemometreWindAngles);
        lastMeasures.put(ANEMOMETRE_GUST_STRENGTH,lastAnemometreGustStrengths);
        lastMeasures.put(ANEMOMETRE_GUST_ANGLE,lastAnemometreGustAngles);
        lastMeasures.put(PLUVIOMETRE_TOTAL_RAIN,pluviometreTotalRain);
        lastMeasures.put(ANEMOMETRE_MAX_GUST_STRENGTH,anemometreMaxGustStrength);
        lastMeasures.put(ANEMOMETRE_MAX_GUST_ANGLE,anemometreMaxGustAngle);
        return lastMeasures;
    }

    private static Measure[] compileMeasures(Measure[] rawMeasures,MeasurementType measurementType)
    {
        return Arrays.stream(rawMeasures)
                .filter(t->t.getMeasurementType()==measurementType)
                .sorted(MEASURE_COMPARATOR)
                .toArray(size->new Measure[size]);
    }
}
