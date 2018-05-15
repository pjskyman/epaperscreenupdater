package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementType;
import sky.program.Duration;

public class HomeWeatherPage extends AbstractNetatmoPage
{
    private long lastRefreshTime;

    public HomeWeatherPage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Données météo (actuelles)";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(6).secondPlus(300).millisecond())
        {
            lastRefreshTime=now;
            Map<String,Measure[]> lastMeasures=getLastMeasures();
            Measure lastSalonTemperature=getLastMeasure(lastMeasures,SALON_TEMPERATURE);
            Measure lastSalonHumidity=getLastMeasure(lastMeasures,SALON_HUMIDITY);
            Measure lastSalonPressure=getLastMeasure(lastMeasures,SALON_PRESSURE);
            Measure lastSalonCarbonDioxyde=getLastMeasure(lastMeasures,SALON_CARBON_DIOXYDE);
            Measure lastSalonNoise=getLastMeasure(lastMeasures,SALON_NOISE);
            Measure lastChambreTemperature=getLastMeasure(lastMeasures,CHAMBRE_TEMPERATURE);
            Measure lastChambreHumidity=getLastMeasure(lastMeasures,CHAMBRE_HUMIDITY);
            Measure lastChambreCarbonDioxyde=getLastMeasure(lastMeasures,CHAMBRE_CARBON_DIOXYDE);
            Measure lastSalleDeBainTemperature=getLastMeasure(lastMeasures,SALLE_DE_BAIN_TEMPERATURE);
            Measure lastSalleDeBainHumidity=getLastMeasure(lastMeasures,SALLE_DE_BAIN_HUMIDITY);
            Measure lastSalleDeBainCarbonDioxyde=getLastMeasure(lastMeasures,SALLE_DE_BAIN_CARBON_DIOXYDE);
            Measure lastSousSolTemperature=getLastMeasure(lastMeasures,SOUS_SOL_TEMPERATURE);
            Measure lastSousSolHumidity=getLastMeasure(lastMeasures,SOUS_SOL_HUMIDITY);
            Measure lastSousSolCarbonDioxyde=getLastMeasure(lastMeasures,SOUS_SOL_CARBON_DIOXYDE);
            Measure lastJardinTemperature=getLastMeasure(lastMeasures,JARDIN_TEMPERATURE);
            Measure lastJardinHumidity=getLastMeasure(lastMeasures,JARDIN_HUMIDITY);
            Measure lastPluviometreRain=getLastMeasure(lastMeasures,PLUVIOMETRE_RAIN);
            Measure lastAnemometreWindStrength=getLastMeasure(lastMeasures,ANEMOMETRE_WIND_STRENGTH);
            Measure lastAnemometreWindAngle=getLastMeasure(lastMeasures,ANEMOMETRE_WIND_ANGLE);
            Measure lastAnemometreGustStrength=getLastMeasure(lastMeasures,ANEMOMETRE_GUST_STRENGTH);
            Measure lastAnemometreGustAngle=getLastMeasure(lastMeasures,ANEMOMETRE_GUST_ANGLE);
            List<Temperature> temperatures=loadTemperatures(1);
            Measure lastToiletsTemperature=temperatures.isEmpty()?null:new StandAloneMeasure(new Date(temperatures.get(0).getTime()),MeasurementType.TEMPERATURE,temperatures.get(0).getTemperature());
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);

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
                g2d.drawLine(74,52,76,52);
                g2d.drawLine(76,52,76,50);
                g2d.drawLine(76,50,74,50);
                g2d.drawLine(74,50,74,52);
                g2d.drawLine(83,58,85,58);
                g2d.drawLine(85,58,85,56);
                g2d.drawLine(85,56,83,56);
                g2d.drawLine(83,56,83,58);
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
                g2d.drawLine(213,64,213,52);
                g2d.drawLine(217,52,217,60);
                g2d.drawLine(213,53,217,53);
                g2d.drawLine(213,55,217,55);
                g2d.drawLine(213,57,217,57);
                g2d.drawLine(213,59,217,59);
                g2d.drawLine(234,64,226,64);
                g2d.drawLine(234,61,226,61);
                g2d.drawLine(226,61,226,52);
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

                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(20f);
                Font measureFont=baseFont.deriveFont(16f).deriveFont(AffineTransform.getScaleInstance(.7d,1d));
                g2d.setFont(measureFont);

                String salonString1=(lastSalonTemperature!=null?""+lastSalonTemperature.getValue():"?")+"°C  ";
                salonString1+=(lastSalonHumidity!=null?""+(int)lastSalonHumidity.getValue():"?")+"%  ";
                salonString1+=(lastSalonPressure!=null?""+lastSalonPressure.getValue():"?")+" hPa";
                int salonString1Width=(int)Math.ceil(measureFont.getStringBounds(salonString1,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(salonString1,74f-(float)salonString1Width/2f,110f);

                String salonString2=(lastSalonCarbonDioxyde!=null?""+(int)lastSalonCarbonDioxyde.getValue():"?")+" ppm  ";
                salonString2+=(lastSalonNoise!=null?""+(int)lastSalonNoise.getValue():"?")+" dB";
                int salonString2Width=(int)Math.ceil(measureFont.getStringBounds(salonString2,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(salonString2,74f-(float)salonString2Width/2f,124f);

                int maxSalonStringWidth=Math.max(salonString1Width,salonString2Width);
                g2d.drawLine(74-maxSalonStringWidth/2-1,96,74+maxSalonStringWidth/2+1,96);
                g2d.drawLine(83,68,64,96);
                g2d.drawString("Salon",74-maxSalonStringWidth/2,95);

                String chambreString1=(lastChambreTemperature!=null?""+lastChambreTemperature.getValue():"?")+"°C  ";
                chambreString1+=(lastChambreHumidity!=null?""+(int)lastChambreHumidity.getValue():"?")+"%";
                int chambreString1Width=(int)Math.ceil(measureFont.getStringBounds(chambreString1,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(chambreString1,37f-(float)chambreString1Width/2f,16f);

                String chambreString2=(lastChambreCarbonDioxyde!=null?""+(int)lastChambreCarbonDioxyde.getValue():"?")+" ppm";
                int chambreString2Width=(int)Math.ceil(measureFont.getStringBounds(chambreString2,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(chambreString2,37f-(float)chambreString2Width/2f,30f);

                int maxChambreStringWidth=Math.max(chambreString1Width,chambreString2Width);
                g2d.drawLine(37-maxChambreStringWidth/2-1,31,37+maxChambreStringWidth/2+1,31);
                g2d.drawLine(74,50,58,31);
                g2d.drawString("Chambre",37-maxChambreStringWidth/2,45);

                String salleDeBainString1=(lastSalleDeBainTemperature!=null?""+lastSalleDeBainTemperature.getValue():"?")+"°C  ";
                salleDeBainString1+=(lastSalleDeBainHumidity!=null?""+(int)lastSalleDeBainHumidity.getValue():"?")+"%";
                int salleDeBainString1Width=(int)Math.ceil(measureFont.getStringBounds(salleDeBainString1,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(salleDeBainString1,111f-(float)salleDeBainString1Width/2f,16f);

                String salleDeBainString2=(lastSalleDeBainCarbonDioxyde!=null?""+(int)lastSalleDeBainCarbonDioxyde.getValue():"?")+" ppm";
                int salleDeBainString2Width=(int)Math.ceil(measureFont.getStringBounds(salleDeBainString2,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(salleDeBainString2,111f-(float)salleDeBainString2Width/2f,30f);

                int maxSalleDeBainStringWidth=Math.max(salleDeBainString1Width,salleDeBainString2Width);
                g2d.drawLine(111-maxSalleDeBainStringWidth/2-1,31,111+maxSalleDeBainStringWidth/2+1,31);
                g2d.drawLine(85,56,103,31);
                String sdbString="SdB";
                int sdbStringWidth=(int)Math.ceil(measureFont.getStringBounds(sdbString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(sdbString,111+maxSalleDeBainStringWidth/2+1-sdbStringWidth,45);

                String toiletsString=(lastToiletsTemperature!=null?DECIMAL_0_FORMAT.format(lastToiletsTemperature.getValue()).replace(",","."):"?")+"°C";
                int toiletsStringWidth=(int)Math.ceil(measureFont.getStringBounds(toiletsString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(toiletsString,2,63);

                g2d.drawLine(1,64,2+toiletsStringWidth+1,64);
                g2d.drawLine(66,64,38,73);
                g2d.drawLine(38,73,25,64);
                g2d.drawString("WC",2,78);

                String jardinString1=(lastJardinTemperature!=null?""+lastJardinTemperature.getValue():"?")+"°C  ";
                jardinString1+=(lastJardinHumidity!=null?""+(int)lastJardinHumidity.getValue():"?")+"%";
                int jardinString1Width=(int)Math.ceil(measureFont.getStringBounds(jardinString1,g2d.getFontRenderContext()).getWidth());
                int jardinLeftOffset=Math.max(0,34-jardinString1Width/2);
                g2d.drawString(jardinString1,185f-(float)jardinString1Width/2f-(float)jardinLeftOffset,110f);

                g2d.drawLine(185-jardinString1Width/2-1-jardinLeftOffset,96,185+jardinString1Width/2+1-jardinLeftOffset,96);
                g2d.drawLine(190,84,195,96);
                g2d.drawString("Jardin",185-jardinString1Width/2-jardinLeftOffset,95);

                String sousSolString1=(lastSousSolTemperature!=null?""+lastSousSolTemperature.getValue():"?")+"°C  ";
                sousSolString1+=(lastSousSolHumidity!=null?""+(int)lastSousSolHumidity.getValue():"?")+"%";
                int sousSolString1Width=(int)Math.ceil(measureFont.getStringBounds(sousSolString1,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(sousSolString1,259f-(float)sousSolString1Width/2f,110f);

                String sousSolString2=(lastSousSolCarbonDioxyde!=null?""+(int)lastSousSolCarbonDioxyde.getValue():"?")+" ppm";
                int sousSolString2Width=(int)Math.ceil(measureFont.getStringBounds(sousSolString2,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(sousSolString2,259f-(float)sousSolString2Width/2f,124f);

                int maxSousSolStringWidth=Math.max(sousSolString1Width,sousSolString2Width);
                g2d.drawLine(259-maxSousSolStringWidth/2-1,96,259+maxSousSolStringWidth/2+1,96);
                g2d.drawLine(212,68,253,96);
                String sousSolString="PJ";
                int sousSolStringWidth=(int)Math.ceil(measureFont.getStringBounds(sousSolString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(sousSolString,259+maxSousSolStringWidth/2+1-sousSolStringWidth,95);

                String pluviometreString1=(lastPluviometreRain!=null?DECIMAL_0_FORMAT.format(lastPluviometreRain.getValue()*12d).replace(",","."):"?")+" mm/h";
                int pluviometreString1Width=(int)Math.ceil(measureFont.getStringBounds(pluviometreString1,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(pluviometreString1,180f-(float)pluviometreString1Width/2f,30f);

                g2d.drawLine(180-pluviometreString1Width/2-1,31,180+pluviometreString1Width/2+1,31);
                g2d.drawLine(193,68,188,63);
                g2d.drawLine(188,63,188,31);
                g2d.drawString("Pluie",180-pluviometreString1Width/2,45);

                String anemometreString1="~"+(lastAnemometreWindStrength!=null?""+(int)lastAnemometreWindStrength.getValue():"?")+" km/h ";
                anemometreString1+=lastAnemometreWindAngle!=null?convertWindAngle(lastAnemometreWindAngle.getValue()):"?";
                int anemometreString1Width=(int)Math.ceil(measureFont.getStringBounds(anemometreString1,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(anemometreString1,254f-(float)anemometreString1Width/2f,16f);

                String anemometreString2="ˆ"+(lastAnemometreGustStrength!=null?""+(int)lastAnemometreGustStrength.getValue():"?")+" km/h ";
                anemometreString2+=lastAnemometreGustAngle!=null?convertWindAngle(lastAnemometreGustAngle.getValue()):"?";
                int anemometreString2Width=(int)Math.ceil(measureFont.getStringBounds(anemometreString2,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(anemometreString2,254f-(float)anemometreString2Width/2f,30f);

                int maxAnemometreStringWidth=Math.max(anemometreString1Width,anemometreString2Width);
                g2d.drawLine(254-maxAnemometreStringWidth/2-1,31,254+maxAnemometreStringWidth/2+1,31);
                g2d.drawLine(194,63,194,42);
                g2d.drawLine(194,42,238,31);
                String anemometreString="Vent";
                int anemometreStringWidth=(int)Math.ceil(measureFont.getStringBounds(anemometreString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(anemometreString,254+maxAnemometreStringWidth/2/*+1*/-anemometreStringWidth,45);

                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("meteo.png")))
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

    private static Measure getLastMeasure(Map<String,Measure[]> lastMeasures,String type)
    {
        Measure[] array=lastMeasures.get(type);
        return array!=null&&array.length>=1?array[array.length-1]:null;
    }

    public static String convertWindAngle(int angle)
    {
        return convertWindAngle((double)angle);
    }

    public static String convertWindAngle(double angle)
    {
        if(angle<11.25d)
            return "N";
        else
            if(angle<33.75d)
                return "NNE";
            else
                if(angle<56.25d)
                    return "NE";
                else
                    if(angle<78.75d)
                        return "ENE";
                    else
                        if(angle<101.25d)
                            return "E";
                        else
                            if(angle<123.75d)
                                return "ESE";
                            else
                                if(angle<146.25d)
                                    return "SE";
                                else
                                    if(angle<168.75d)
                                        return "SSE";
                                    else
                                        if(angle<191.25d)
                                            return "S";
                                        else
                                            if(angle<213.75d)
                                                return "SSO";
                                            else
                                                if(angle<236.25d)
                                                    return "SO";
                                                else
                                                    if(angle<258.75d)
                                                        return "OSO";
                                                    else
                                                        if(angle<281.25d)
                                                            return "O";
                                                        else
                                                            if(angle<303.75d)
                                                                return "ONO";
                                                            else
                                                                if(angle<326.25d)
                                                                    return "NO";
                                                                else
                                                                    if(angle<348.75d)
                                                                        return "NNO";
                                                                    else
                                                                        return "N";
    }

    private static List<Temperature> loadTemperatures(int number)
    {
        try
        {
            try(Connection connection=getConnection2())
            {
//                long startTime=System.currentTimeMillis();
                List<Temperature> temperatures=new ArrayList<>();
                try(Statement statement=connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY))
                {
                    try(ResultSet resultSet=statement.executeQuery("SELECT * FROM temperature ORDER BY time DESC LIMIT "+number+";"))
                    {
                        while(resultSet.next())
                        {
                            long time=resultSet.getLong("time");
                            double temperature=resultSet.getDouble("temperature");
                            double setPoint=resultSet.getDouble("setPoint");
                            double ratio=resultSet.getDouble("ratio");
                            boolean heaterOn=resultSet.getInt("heaterOn")==1;
                            temperatures.add(new Temperature(time,temperature,setPoint,ratio,heaterOn));
                        }
                    }
                }
                Collections.reverse(temperatures);
//                Logger.LOGGER.info(temperatures.size()+" rows fetched in "+(System.currentTimeMillis()-startTime)+" ms");
                return temperatures;
            }
        }
        catch(NotAvailableDatabaseException|SQLException e)
        {
            Logger.LOGGER.error(e.toString());
            return new ArrayList<>(0);
        }
    }

    private static Connection getConnection2() throws NotAvailableDatabaseException
    {
        Connection connection=null;
        try
        {
            connection=DriverManager.getConnection("jdbc:mariadb://192.168.0.71:3306/toilettes?user=root&password=loop27c");
//            Logger.LOGGER.info("Connection to SQLite has been established.");
        }
        catch(SQLException e)
        {
            try
            {
                if(connection!=null)
                    connection.close();
            }
            catch(SQLException ex)
            {
            }
            Logger.LOGGER.error(e.toString());
        }
        return connection;
    }

    public static void main(String[] args)
    {
        new HomeWeatherPage(null).potentiallyUpdate();
    }
}
