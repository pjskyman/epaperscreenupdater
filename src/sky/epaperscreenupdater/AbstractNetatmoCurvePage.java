package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.imageio.ImageIO;
import sky.netatmo.Measure;

public abstract class AbstractNetatmoCurvePage extends AbstractNetatmoPage
{
    private long lastRefreshTime;

    protected AbstractNetatmoCurvePage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>getRefreshDelay())
        {
            lastRefreshTime=now;
            Map<String,Measure[]> measureMap=getLastMeasures();
            Measure[] lastMeasures=measureMap.get(getMeasureMapKey());
            Measure[] filteredMeasures=HomeWeatherVariationPage.filterTimedWindowMeasures(lastMeasures,2);
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);

                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(14f);
                g2d.setFont(baseFont);

                if(filteredMeasures!=null)
                {
                    g2d.drawLine(20,0,20,115);
                    g2d.drawLine(20,115,295,115);
                    double minY=1e10d;
                    double maxY=-1e10d;
                    for(Measure measure:filteredMeasures)
                    {
                        if(measure.getValue()<minY)
                            minY=measure.getValue();
                        if(measure.getValue()>maxY)
                            maxY=measure.getValue();
                    }
                    double amplitude=maxY-minY;
                    if(amplitude<=0d)
                        amplitude=1d;
                    for(int i=1;i<filteredMeasures.length;i++)
                    {
                        Measure measure1=filteredMeasures[i-1];
                        long time1=measure1.getDate().getTime();
                        int x1=(int)((296d-21d-1d)*(1d-(double)((filteredMeasures[filteredMeasures.length-1].getDate().getTime()-time1)/1000L)/7200d)+21d-1d);
                        double value1=measure1.getValue();
                        int y1=(int)((115d-1d)*(1d-(value1-minY)/amplitude))+1;
                        Measure measure2=filteredMeasures[i];
                        long time2=measure2.getDate().getTime();
                        int x2=(int)((296d-21d-1d)*(1d-(double)((filteredMeasures[filteredMeasures.length-1].getDate().getTime()-time2)/1000L)/7200d)+21d-1d);
                        double value2=measure2.getValue();
                        int y2=(int)((115d-1d)*(1d-(value2-minY)/amplitude))+1;
                        g2d.drawLine(x1,y1,x2,y2);
                        g2d.drawRect(x1-1,y1-1,2,2);
                        if(i==filteredMeasures.length-1)
                            g2d.drawRect(x2-1,y2-1,2,2);
                    }
                }

//                Font measureFont=baseFont.deriveFont(16f).deriveFont(AffineTransform.getScaleInstance(.7d,1d));
//
//                String salonString1=(lastSalonTemperature!=null?""+lastSalonTemperature.getValue():"?")+"°C  ";
//                salonString1+=(lastSalonHumidity!=null?""+(int)lastSalonHumidity.getValue():"?")+"%  ";
//                salonString1+=(lastSalonPressure!=null?""+lastSalonPressure.getValue():"?")+" hPa";
//                int salonString1Width=(int)Math.ceil(measureFont.getStringBounds(salonString1,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(salonString1,74f-(float)salonString1Width/2f,110f);
//
//                String salonString2=(lastSalonCarbonDioxyde!=null?""+(int)lastSalonCarbonDioxyde.getValue():"?")+" ppm  ";
//                salonString2+=(lastSalonNoise!=null?""+(int)lastSalonNoise.getValue():"?")+" dB";
//                int salonString2Width=(int)Math.ceil(measureFont.getStringBounds(salonString2,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(salonString2,74f-(float)salonString2Width/2f,124f);
//
//                int maxSalonStringWidth=Math.max(salonString1Width,salonString2Width);
//                g2d.drawLine(74-maxSalonStringWidth/2-1,96,74+maxSalonStringWidth/2+1,96);
//                g2d.drawLine(83,68,64,96);
//                g2d.drawString("Salon",74-maxSalonStringWidth/2,95);
//
//                String chambreString1=(lastChambreTemperature!=null?""+lastChambreTemperature.getValue():"?")+"°C  ";
//                chambreString1+=(lastChambreHumidity!=null?""+(int)lastChambreHumidity.getValue():"?")+"%";
//                int chambreString1Width=(int)Math.ceil(measureFont.getStringBounds(chambreString1,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(chambreString1,37f-(float)chambreString1Width/2f,16f);
//
//                String chambreString2=(lastChambreCarbonDioxyde!=null?""+(int)lastChambreCarbonDioxyde.getValue():"?")+" ppm";
//                int chambreString2Width=(int)Math.ceil(measureFont.getStringBounds(chambreString2,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(chambreString2,37f-(float)chambreString2Width/2f,30f);
//
//                int maxChambreStringWidth=Math.max(chambreString1Width,chambreString2Width);
//                g2d.drawLine(37-maxChambreStringWidth/2-1,31,37+maxChambreStringWidth/2+1,31);
//                g2d.drawLine(74,50,58,31);
//                g2d.drawString("Chambre",37-maxChambreStringWidth/2,45);
//
//                String salleDeBainString1=(lastSalleDeBainTemperature!=null?""+lastSalleDeBainTemperature.getValue():"?")+"°C  ";
//                salleDeBainString1+=(lastSalleDeBainHumidity!=null?""+(int)lastSalleDeBainHumidity.getValue():"?")+"%";
//                int salleDeBainString1Width=(int)Math.ceil(measureFont.getStringBounds(salleDeBainString1,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(salleDeBainString1,111f-(float)salleDeBainString1Width/2f,16f);
//
//                String salleDeBainString2=(lastSalleDeBainCarbonDioxyde!=null?""+(int)lastSalleDeBainCarbonDioxyde.getValue():"?")+" ppm";
//                int salleDeBainString2Width=(int)Math.ceil(measureFont.getStringBounds(salleDeBainString2,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(salleDeBainString2,111f-(float)salleDeBainString2Width/2f,30f);
//
//                int maxSalleDeBainStringWidth=Math.max(salleDeBainString1Width,salleDeBainString2Width);
//                g2d.drawLine(111-maxSalleDeBainStringWidth/2-1,31,111+maxSalleDeBainStringWidth/2+1,31);
//                g2d.drawLine(85,56,103,31);
//                String sdbString="SdB";
//                int sdbStringWidth=(int)Math.ceil(measureFont.getStringBounds(sdbString,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(sdbString,111+maxSalleDeBainStringWidth/2+1-sdbStringWidth,45);
//
//                String toiletsString=(lastToiletsTemperature!=null?DECIMAL_0_FORMAT.format(lastToiletsTemperature.getValue()).replace(",","."):"?")+"°C";
//                int toiletsStringWidth=(int)Math.ceil(measureFont.getStringBounds(toiletsString,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(toiletsString,2,63);
//
//                g2d.drawLine(1,64,2+toiletsStringWidth+1,64);
//                g2d.drawLine(66,64,38,73);
//                g2d.drawLine(38,73,25,64);
//                g2d.drawString("WC",2,78);
//
//                String jardinString1=(lastJardinTemperature!=null?""+lastJardinTemperature.getValue():"?")+"°C  ";
//                jardinString1+=(lastJardinHumidity!=null?""+(int)lastJardinHumidity.getValue():"?")+"%";
//                int jardinString1Width=(int)Math.ceil(measureFont.getStringBounds(jardinString1,g2d.getFontRenderContext()).getWidth());
//                int jardinLeftOffset=Math.max(0,34-jardinString1Width/2);
//                g2d.drawString(jardinString1,185f-(float)jardinString1Width/2f-(float)jardinLeftOffset,110f);
//
//                g2d.drawLine(185-jardinString1Width/2-1-jardinLeftOffset,96,185+jardinString1Width/2+1-jardinLeftOffset,96);
//                g2d.drawLine(190,84,195,96);
//                g2d.drawString("Jardin",185-jardinString1Width/2-jardinLeftOffset,95);
//
//                String sousSolString1=(lastSousSolTemperature!=null?""+lastSousSolTemperature.getValue():"?")+"°C  ";
//                sousSolString1+=(lastSousSolHumidity!=null?""+(int)lastSousSolHumidity.getValue():"?")+"%";
//                int sousSolString1Width=(int)Math.ceil(measureFont.getStringBounds(sousSolString1,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(sousSolString1,259f-(float)sousSolString1Width/2f,110f);
//
//                String sousSolString2=(lastSousSolCarbonDioxyde!=null?""+(int)lastSousSolCarbonDioxyde.getValue():"?")+" ppm";
//                int sousSolString2Width=(int)Math.ceil(measureFont.getStringBounds(sousSolString2,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(sousSolString2,259f-(float)sousSolString2Width/2f,124f);
//
//                int maxSousSolStringWidth=Math.max(sousSolString1Width,sousSolString2Width);
//                g2d.drawLine(259-maxSousSolStringWidth/2-1,96,259+maxSousSolStringWidth/2+1,96);
//                g2d.drawLine(212,68,253,96);
//                String sousSolString="PJ";
//                int sousSolStringWidth=(int)Math.ceil(measureFont.getStringBounds(sousSolString,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(sousSolString,259+maxSousSolStringWidth/2+1-sousSolStringWidth,95);
//
//                String pluviometreString1=(lastPluviometreRain!=null?DECIMAL_0_FORMAT.format(lastPluviometreRain.getValue()).replace(",","."):"?")+" mm/h";
//                int pluviometreString1Width=(int)Math.ceil(measureFont.getStringBounds(pluviometreString1,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(pluviometreString1,180f-(float)pluviometreString1Width/2f,30f);
//
//                g2d.drawLine(180-pluviometreString1Width/2-1,31,180+pluviometreString1Width/2+1,31);
//                g2d.drawLine(193,68,188,63);
//                g2d.drawLine(188,63,188,31);
//                g2d.drawString("Pluie",180-pluviometreString1Width/2,45);
//
//                String anemometreString1="~"+(lastAnemometreWindStrength!=null?""+(int)lastAnemometreWindStrength.getValue():"?")+" km/h ";
//                anemometreString1+=lastAnemometreWindAngle!=null?convertWindAngle(lastAnemometreWindAngle.getValue()):"?";
//                int anemometreString1Width=(int)Math.ceil(measureFont.getStringBounds(anemometreString1,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(anemometreString1,254f-(float)anemometreString1Width/2f,16f);
//
//                String anemometreString2="ˆ"+(lastAnemometreGustStrength!=null?""+(int)lastAnemometreGustStrength.getValue():"?")+" km/h ";
//                anemometreString2+=lastAnemometreGustAngle!=null?convertWindAngle(lastAnemometreGustAngle.getValue()):"?";
//                int anemometreString2Width=(int)Math.ceil(measureFont.getStringBounds(anemometreString2,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(anemometreString2,254f-(float)anemometreString2Width/2f,30f);
//
//                int maxAnemometreStringWidth=Math.max(anemometreString1Width,anemometreString2Width);
//                g2d.drawLine(254-maxAnemometreStringWidth/2-1,31,254+maxAnemometreStringWidth/2+1,31);
//                g2d.drawLine(194,63,194,42);
//                g2d.drawLine(194,42,238,31);
//                String anemometreString="Vent";
//                int anemometreStringWidth=(int)Math.ceil(measureFont.getStringBounds(anemometreString,g2d.getFontRenderContext()).getWidth());
//                g2d.drawString(anemometreString,254+maxAnemometreStringWidth/2/*+1*/-anemometreStringWidth,45);

                g2d.dispose();
                try(OutputStream outputStream=new FileOutputStream(new File(getVerificationFileName())))
                {
                    ImageIO.write(sourceImage,"png",outputStream);
                }
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

    protected abstract long getRefreshDelay();

    protected abstract String getMeasureMapKey();

    protected abstract String getVerificationFileName();
}
