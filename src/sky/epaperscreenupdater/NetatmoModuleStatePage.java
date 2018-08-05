package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class NetatmoModuleStatePage extends AbstractNetatmoPage
{
    private long lastRefreshTime;

    public NetatmoModuleStatePage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "État des modules Netatmo";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(3).minutePlus(46).second())
        {
            lastRefreshTime=now;
            Map<String,Measure[]> lastMeasures=getLastMeasures();
            Measure salonWifi=HomeWeatherPage.getLastMeasure(lastMeasures,SALON_WIFI);
            Measure salonFirmware=HomeWeatherPage.getLastMeasure(lastMeasures,SALON_FIRMWARE);
            Measure chambreBattery=HomeWeatherPage.getLastMeasure(lastMeasures,CHAMBRE_BATTERY);
            Measure chambreRadio=HomeWeatherPage.getLastMeasure(lastMeasures,CHAMBRE_RADIO);
            Measure salleDeBainBattery=HomeWeatherPage.getLastMeasure(lastMeasures,SALLE_DE_BAIN_BATTERY);
            Measure salleDeBainRadio=HomeWeatherPage.getLastMeasure(lastMeasures,SALLE_DE_BAIN_RADIO);
            Measure sousSolBattery=HomeWeatherPage.getLastMeasure(lastMeasures,SOUS_SOL_BATTERY);
            Measure sousSolRadio=HomeWeatherPage.getLastMeasure(lastMeasures,SOUS_SOL_RADIO);
            Measure jardinBattery=HomeWeatherPage.getLastMeasure(lastMeasures,JARDIN_BATTERY);
            Measure jardinRadio=HomeWeatherPage.getLastMeasure(lastMeasures,JARDIN_RADIO);
            Measure pluviometreBattery=HomeWeatherPage.getLastMeasure(lastMeasures,PLUVIOMETRE_BATTERY);
            Measure pluviometreRadio=HomeWeatherPage.getLastMeasure(lastMeasures,PLUVIOMETRE_RADIO);
            Measure anemometreBattery=HomeWeatherPage.getLastMeasure(lastMeasures,ANEMOMETRE_BATTERY);
            Measure anemometreRadio=HomeWeatherPage.getLastMeasure(lastMeasures,ANEMOMETRE_RADIO);
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);

                Font percentageFont=Main.FREDOKA_ONE_FONT.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(.65d,1d));
                Font nameFont=Main.FREDOKA_ONE_FONT.deriveFont(18f).deriveFont(AffineTransform.getScaleInstance(.8d,1d));
                Font infoFont=Main.FREDOKA_ONE_FONT.deriveFont(12f).deriveFont(AffineTransform.getScaleInstance(1.9d,1d));

                String[] names=new String[]
                {
                    "Jardin",
                    "Chamb",
                    "SDB",
                    "SS",
                    "Anémo",
                    "Pluvio",
                };
                double[] batteries=new double[]
                {
                    ((jardinBattery==null?0d:jardinBattery.getValue())-4000d)/(5500d-4000d),
                    ((chambreBattery==null?0d:chambreBattery.getValue())-4560d)/(5640d-4560d),
                    ((salleDeBainBattery==null?0d:salleDeBainBattery.getValue())-4560d)/(5640d-4560d),
                    ((sousSolBattery==null?0d:sousSolBattery.getValue())-4560d)/(5640d-4560d),
                    ((anemometreBattery==null?0d:anemometreBattery.getValue())-4360d)/(5590d-4360d),
                    ((pluviometreBattery==null?0d:pluviometreBattery.getValue())-4000d)/(5500d-4000d),
                };
                double[] radios=new double[]
                {
                    (96d-(jardinRadio==null?100d:jardinRadio.getValue()))/(96d-59d),
                    (96d-(chambreRadio==null?100d:chambreRadio.getValue()))/(96d-59d),
                    (96d-(salleDeBainRadio==null?100d:salleDeBainRadio.getValue()))/(96d-59d),
                    (96d-(sousSolRadio==null?100d:sousSolRadio.getValue()))/(96d-59d),
                    (96d-(anemometreRadio==null?100d:anemometreRadio.getValue()))/(96d-59d),
                    (96d-(pluviometreRadio==null?100d:pluviometreRadio.getValue()))/(96d-59d),
                };
                double wifi=(86d-(salonWifi==null?100d:salonWifi.getValue()))/(86d-56d);
                int firmware=salonFirmware==null?0:(int)salonFirmware.getValue();

                for(int i=0;i<names.length;i++)
                {
                    int baseX=i*49+2;
                    g2d.drawLine(baseX+19,1,baseX+26,1);
                    g2d.drawLine(baseX+17,2,baseX+28,2);
                    g2d.drawLine(baseX+17,3,baseX+28,3);
                    g2d.drawLine(baseX+16,4,baseX+29,4);
                    g2d.drawLine(baseX+16,5,baseX+29,5);
                    g2d.drawLine(baseX+16,6,baseX+29,6);
                    g2d.drawLine(baseX+5,7,baseX+40,7);
                    g2d.drawLine(baseX+3,8,baseX+42,8);
                    g2d.drawLine(baseX+2,9,baseX+43,9);
                    g2d.drawLine(baseX+2,10,baseX+43,10);
                    g2d.drawLine(baseX+1,11,baseX+44,11);
                    g2d.drawLine(baseX+1,84,baseX+44,84);
                    g2d.drawLine(baseX+2,85,baseX+43,85);
                    g2d.drawLine(baseX+2,86,baseX+43,86);
                    g2d.drawLine(baseX+2,86,baseX+43,86);
                    g2d.drawLine(baseX+3,87,baseX+42,87);
                    g2d.drawLine(baseX+5,88,baseX+40,88);
                    g2d.drawLine(baseX+1,12,baseX+1,83);
                    g2d.drawLine(baseX+2,12,baseX+2,83);
                    g2d.drawLine(baseX+3,12,baseX+3,83);
                    g2d.drawLine(baseX+4,12,baseX+4,83);
                    g2d.drawLine(baseX+5,12,baseX+5,83);
                    g2d.drawLine(baseX+40,12,baseX+40,83);
                    g2d.drawLine(baseX+41,12,baseX+41,83);
                    g2d.drawLine(baseX+42,12,baseX+42,83);
                    g2d.drawLine(baseX+43,12,baseX+43,83);
                    g2d.drawLine(baseX+44,12,baseX+44,83);
                    g2d.drawLine(baseX+6,12,baseX+6,12);
                    g2d.drawLine(baseX+39,12,baseX+39,12);
                    g2d.drawLine(baseX+6,83,baseX+6,83);
                    g2d.drawLine(baseX+39,83,baseX+39,83);

                    double bornedBattery=Math.max(0d,Math.min(1d,batteries[i]));
                    int up=80-(int)(bornedBattery*64d);
                    for(int j=79;j>=up;j--)
                        g2d.drawLine(baseX+10,j,baseX+35,j);

                    g2d.setFont(percentageFont);
                    String percentage=(int)(bornedBattery*100d)+"%";
                    int percentageWidth=(int)Math.ceil(percentageFont.getStringBounds(percentage,g2d.getFontRenderContext()).getWidth());
                    if(up<66)
                    {
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(percentage,baseX+23-(int)percentageWidth/2,(79+up)/2+6);
                        g2d.setColor(Color.BLACK);
                    }
                    else
                        g2d.drawString(percentage,baseX+23-(int)percentageWidth/2,(16+up)/2+6);

                    g2d.setFont(nameFont);
                    String name=names[i];
                    int nameWidth=(int)Math.ceil(nameFont.getStringBounds(name,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(name,baseX+22-(int)nameWidth/2,104);

                    double bornedRadio=Math.max(0d,Math.min(1d,radios[i]));
                    g2d.drawLine(baseX+4,117,baseX+40,117);
                    for(int j=1;j<=(int)(bornedRadio*12d);j++)
                    {
                        g2d.drawLine(baseX+2+3*j,116,baseX+2+3*j,116-j+1);
                        g2d.drawLine(baseX+3+3*j,116,baseX+3+3*j,116-j+1);
                    }
                }

                g2d.setFont(infoFont);
                String infoString="Wifi : "+(int)(wifi*100d)+"%  Firmware : "+firmware;
                int infoStringWidth=(int)Math.ceil(infoFont.getStringBounds(infoString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(infoString,148-infoStringWidth/2,128);

                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("module_state.png")))
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
        new NetatmoModuleStatePage(null).potentiallyUpdate();
    }
}
