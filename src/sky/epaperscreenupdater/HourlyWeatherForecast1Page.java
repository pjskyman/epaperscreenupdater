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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.program.Duration;

public class HourlyWeatherForecast1Page extends AbstractSinglePage
{
    private long lastRefreshTime;

    public HourlyWeatherForecast1Page(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Prévisions météo H/H";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(15).minutePlus(17).second())
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
                JsonObject hourlyObject=response.get("hourly").getAsJsonObject();
                JsonArray dataArray=hourlyObject.get("data").getAsJsonArray();
                List<Hourly> hourlies=new ArrayList<>();
                for(int i=0;i<dataArray.size();i++)
                {
                    JsonObject object=dataArray.get(i).getAsJsonObject();
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
                String string="Heure";
                int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,14);
                string="Icône";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,31);
                string="°C";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,43);
                string="%Hum";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,55);
                string="°CRosée";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,67);
                string="Vent";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,79);
                string="mmPluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,91);
                string="%Pluie";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,103);
                string="hPa";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,115);
                string="%Nuage";
                stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,26-stringWidth/2,127);
                for(int i=0;i<Math.min(hourlies.size(),7);i++)
                {
                    int baseX=51+i*35;
                    Hourly hourly=hourlies.get(i);
                    GregorianCalendar calendar=new GregorianCalendar();
                    calendar.setTimeInMillis(hourly.getTime());
                    string=new SimpleDateFormat("HH:mm").format(new Date(hourly.getTime()));
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,9);
                    string=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.FRANCE);
                    string=string.substring(0,1).toUpperCase()+string.substring(1);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,19);
                    g2d.drawImage(Icons.getIcon(hourly.getIcon()),baseX+1,21,null);
                    string=DECIMAL_0_FORMAT.format(hourly.getTemperature());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,43);
                    string=INTEGER_FORMAT.format(hourly.getHumidity()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,55);
                    string=DECIMAL_0_FORMAT.format(hourly.getDewPoint());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,67);
                    g2d.setFont(alternativeBaseFont);
                    string=INTEGER_FORMAT.format(hourly.getWindGust())+"/"+HomeWeatherPage.convertWindAngle(hourly.getWindBearing());
                    stringWidth=(int)Math.ceil(alternativeBaseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,79);
                    g2d.setFont(baseFont);
                    string=DECIMAL_0_FORMAT.format(hourly.getPrecipIntensity());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,91);
                    string=INTEGER_FORMAT.format(hourly.getPrecipProbability()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,103);
                    string=INTEGER_FORMAT.format(hourly.getPressure());
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,115);
                    string=INTEGER_FORMAT.format(hourly.getCloudCover()*100d);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,baseX+18-stringWidth/2,127);
                }
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("weather3.png")))
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
        new HourlyWeatherForecast1Page(null).potentiallyUpdate();
    }
}
