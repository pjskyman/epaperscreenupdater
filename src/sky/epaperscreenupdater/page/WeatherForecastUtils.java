package sky.epaperscreenupdater.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import sky.housecommon.Logger;
import sky.program.Duration;

public class WeatherForecastUtils
{
    private static long lastWeatherForecastVerificationTime=0L;
    private static List<Daily> lastDailies=new ArrayList<>();
    private static List<Hourly> lastHourlies=new ArrayList<>();
    private static final Object LOCK_OBJECT=new Object();

    private WeatherForecastUtils()
    {
    }

    public static List<Daily> getLastDailies()
    {
        synchronized(LOCK_OBJECT)
        {
            updateWeatherForecastData();
            return lastDailies;
        }
    }

    public static List<Hourly> getLastHourlies()
    {
        synchronized(LOCK_OBJECT)
        {
            updateWeatherForecastData();
            return lastHourlies;
        }
    }

    private static void updateWeatherForecastData()
    {
        long now=System.currentTimeMillis();
        if(now-lastWeatherForecastVerificationTime>Duration.of(10).minutePlus(5).secondPlus(300).millisecond())
        {
            lastWeatherForecastVerificationTime=now;
            boolean dailiesOk=false;
            boolean hourliesOk=false;
            try
            {
                StringBuilder stringBuilder=new StringBuilder();
                HttpURLConnection connection=null;
                try
                {
                    String apiKey="";
                    String latitude="";
                    String longitude="";
                    try(BufferedReader reader=new BufferedReader(new FileReader(new File("darksky.ini"))))
                    {
                        apiKey=reader.readLine();
                        latitude=reader.readLine();
                        longitude=reader.readLine();
                    }
                    catch(IOException e)
                    {
                        Logger.LOGGER.error("Unable to read Darksky access informations from the config file ("+e.toString()+")");
                        e.printStackTrace();
                    }
                    connection=(HttpURLConnection)new URL("https://api.darksky.net/forecast/"+apiKey+"/"+latitude+","+longitude+"?exclude=currently,minutely&lang=fr&units=ca").openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setAllowUserInteraction(false);
                    connection.setDoOutput(true);
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        stringBuilder.append(line);
                        stringBuilder.append("\n");
                    }
                    connection.disconnect();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(connection!=null)
                        connection.disconnect();
                }
                String string=stringBuilder.toString();
                if(!string.isEmpty())
                {
                    JsonObject response=new JsonParser().parse(string).getAsJsonObject();
                    JsonObject dailyObject=response.get("daily").getAsJsonObject();
                    JsonArray dailyDataArray=dailyObject.get("data").getAsJsonArray();
                    List<Daily> dailies=new ArrayList<>();
                    for(int i=0;i<dailyDataArray.size();i++)
                    {
                        JsonObject object=dailyDataArray.get(i).getAsJsonObject();
                        dailies.add(new Daily(object.has("time")?object.get("time").getAsLong()*1000L:0L,
                                object.has("summary")?object.get("summary").getAsString():"",
                                object.has("icon")?object.get("icon").getAsString():"",
                                object.has("sunriseTime")?object.get("sunriseTime").getAsLong()*1000L:0L,
                                object.has("sunsetTime")?object.get("sunsetTime").getAsLong()*1000L:0L,
                                object.has("moonPhase")?object.get("moonPhase").getAsDouble():0d,
                                object.has("precipIntensity")?object.get("precipIntensity").getAsDouble():0d,
                                object.has("precipIntensityMax")?object.get("precipIntensityMax").getAsDouble():0d,
                                object.has("precipIntensityMaxTime")?object.get("precipIntensityMaxTime").getAsLong()*1000L:0L,
                                object.has("precipProbability")?object.get("precipProbability").getAsDouble():0d,
                                object.has("precipType")?object.get("precipType").getAsString():"",
                                object.has("temperatureHigh")?object.get("temperatureHigh").getAsDouble():0d,
                                object.has("temperatureHighTime")?object.get("temperatureHighTime").getAsLong()*1000L:0L,
                                object.has("temperatureLow")?object.get("temperatureLow").getAsDouble():0d,
                                object.has("temperatureLowTime")?object.get("temperatureLowTime").getAsLong()*1000L:0L,
                                object.has("apparentTemperatureHigh")?object.get("apparentTemperatureHigh").getAsDouble():0d,
                                object.has("apparentTemperatureHighTime")?object.get("apparentTemperatureHighTime").getAsLong()*1000L:0L,
                                object.has("apparentTemperatureLow")?object.get("apparentTemperatureLow").getAsDouble():0d,
                                object.has("apparentTemperatureLowTime")?object.get("apparentTemperatureLowTime").getAsLong()*1000L:0L,
                                object.has("dewPoint")?object.get("dewPoint").getAsDouble():0d,
                                object.has("humidity")?object.get("humidity").getAsDouble():0d,
                                object.has("pressure")?object.get("pressure").getAsDouble():0d,
                                object.has("windSpeed")?object.get("windSpeed").getAsDouble():0d,
                                object.has("windGust")?object.get("windGust").getAsDouble():0d,
                                object.has("windGustTime")?object.get("windGustTime").getAsLong()*1000L:0L,
                                object.has("windBearing")?object.get("windBearing").getAsInt():0,
                                object.has("cloudCover")?object.get("cloudCover").getAsDouble():0d,
                                object.has("uvIndex")?object.get("uvIndex").getAsInt():0,
                                object.has("uvIndexTime")?object.get("uvIndexTime").getAsLong()*1000L:0L,
                                object.has("visibility")?object.get("visibility").getAsDouble():0d,
                                object.has("ozone")?object.get("ozone").getAsDouble():0d,
                                object.has("temperatureMin")?object.get("temperatureMin").getAsDouble():0d,
                                object.has("temperatureMinTime")?object.get("temperatureMinTime").getAsLong()*1000L:0L,
                                object.has("temperatureMax")?object.get("temperatureMax").getAsDouble():0d,
                                object.has("temperatureMaxTime")?object.get("temperatureMaxTime").getAsLong()*1000L:0L,
                                object.has("apparentTemperatureMin")?object.get("apparentTemperatureMin").getAsDouble():0d,
                                object.has("apparentTemperatureMinTime")?object.get("apparentTemperatureMinTime").getAsLong()*1000L:0L,
                                object.has("apparentTemperatureMax")?object.get("apparentTemperatureMax").getAsDouble():0d,
                                object.has("apparentTemperatureMaxTime")?object.get("apparentTemperatureMaxTime").getAsLong()*1000L:0L));
                    }
                    lastDailies=dailies;
                    dailiesOk=true;
                    JsonObject hourlyObject=response.get("hourly").getAsJsonObject();
                    JsonArray hourlyDataArray=hourlyObject.get("data").getAsJsonArray();
                    List<Hourly> hourlies=new ArrayList<>();
                    for(int i=0;i<hourlyDataArray.size();i++)
                    {
                        JsonObject object=hourlyDataArray.get(i).getAsJsonObject();
                        hourlies.add(new Hourly(object.has("time")?object.get("time").getAsLong()*1000L:0L,
                                object.has("summary")?object.get("summary").getAsString():"",
                                object.has("icon")?object.get("icon").getAsString():"",
                                object.has("precipIntensity")?object.get("precipIntensity").getAsDouble():0d,
                                object.has("precipProbability")?object.get("precipProbability").getAsDouble():0d,
                                object.has("precipType")?object.get("precipType").getAsString():"",
                                object.has("temperature")?object.get("temperature").getAsDouble():0d,
                                object.has("apparentTemperature")?object.get("apparentTemperature").getAsDouble():0d,
                                object.has("dewPoint")?object.get("dewPoint").getAsDouble():0d,
                                object.has("humidity")?object.get("humidity").getAsDouble():0d,
                                object.has("pressure")?object.get("pressure").getAsDouble():0d,
                                object.has("windSpeed")?object.get("windSpeed").getAsDouble():0d,
                                object.has("windGust")?object.get("windGust").getAsDouble():0d,
                                object.has("windBearing")?object.get("windBearing").getAsInt():0,
                                object.has("cloudCover")?object.get("cloudCover").getAsDouble():0d,
                                object.has("uvIndex")?object.get("uvIndex").getAsInt():0,
                                object.has("visibility")?object.get("visibility").getAsDouble():0d,
                                object.has("ozone")?object.get("ozone").getAsDouble():0d));
                    }
                    lastHourlies=hourlies;
                    hourliesOk=true;
                }
                else
                    Logger.LOGGER.warn("Timeout or error when getting weather forecast");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error(e.toString());
                e.printStackTrace();
            }
        }
    }
}
