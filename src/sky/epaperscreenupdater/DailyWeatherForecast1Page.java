package sky.epaperscreenupdater;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.program.Duration;

public class DailyWeatherForecast1Page extends AbstractSinglePage
{
    private long lastRefreshTime;

    public DailyWeatherForecast1Page(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Prévisions météo 1/2";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(15).minutePlus(9).second())
        {
            lastRefreshTime=now;
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
                JsonObject response=new JsonParser().parse(stringBuilder.toString()).getAsJsonObject();
                JsonObject dailyObject=response.get("daily").getAsJsonObject();
                JsonArray dataArray=dailyObject.get("data").getAsJsonArray();
                List<Daily> dailies=new ArrayList<>();
                for(int i=0;i<dataArray.size();i++)
                {
                    JsonObject object=dataArray.get(i).getAsJsonObject();
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

                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                for(int x=51;x<296;x+=35)
                    g2d.drawLine(x,0,x,127);
                for(int y=20;y<128;y+=12)
                    g2d.drawLine(0,y,295,y);
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(12f);
                Font alternativeBaseFont=baseFont.deriveFont(AffineTransform.getScaleInstance(.75d,1d));
                g2d.setFont(baseFont);
                String string="Date";
                int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,14);
                string="Icône";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,31);
                string="°CMin";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,43);
                string="°CMax";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,55);
                string="%Hum";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,67);
                string="°CRosée";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,79);
                string="Vent";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,91);
                string="mmPluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,103);
                string="%Pluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,115);
                string="hPa";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,127);
                for(int i=0;i<dailies.size();i++)
                {
                    int baseX=51+i*35;
                    Daily daily=dailies.get(i);
                    GregorianCalendar calendar=new GregorianCalendar();
                    calendar.setTimeInMillis(daily.getTime());
                    string=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.FRANCE);
                    string=string.substring(0,1).toUpperCase()+string.substring(1);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,9);
                    string=SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(new Date(daily.getTime())).substring(0,2);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,19);
                    g2d.drawImage(Icons.getIcon(daily.getIcon()),baseX+1,21,null);
                    string=DECIMAL_0_FORMAT.format(daily.getTemperatureLow());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,43);
                    string=DECIMAL_0_FORMAT.format(daily.getTemperatureHigh());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,55);
                    string=INTEGER_FORMAT.format(daily.getHumidity()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,67);
                    string=DECIMAL_0_FORMAT.format(daily.getDewPoint());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,79);
                    g2d.setFont(alternativeBaseFont);
                    string=INTEGER_FORMAT.format(daily.getWindGust())+"/"+HomeWeatherPage.convertWindAngle(daily.getWindBearing());
                    stringWidth=(int)Math.ceil(alternativeBaseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,91);
                    g2d.setFont(baseFont);
                    string=DECIMAL_0_FORMAT.format(daily.getPrecipIntensity()*24d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,103);
                    string=INTEGER_FORMAT.format(daily.getPrecipProbability()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,115);
                    string=INTEGER_FORMAT.format(daily.getPressure());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,127);
                }
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("weather1.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH).writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    public static void main(String[] args)
    {
        new DailyWeatherForecast1Page(null).potentiallyUpdate();
    }
}
