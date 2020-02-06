package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class CarbonDioxydeWeightPage extends AbstractNetatmoPage
{
    private long lastRefreshTime;

    public CarbonDioxydeWeightPage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Poids de CO2";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(10).minutePlus(2).secondPlus(500).millisecond())
        {
            Logger.LOGGER.info("Page \""+getName()+"\" needs to be updated");
            lastRefreshTime=now;
            Map<String,Measure[]> lastMeasures=getLastMeasures();
            Measure last70ee50000deaCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,_70ee50000dea_CARBON_DIOXYDE);//salon
            Measure last030000000216CarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,_030000000216_CARBON_DIOXYDE);//filles
            Measure last03000000076eCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,_03000000076e_CARBON_DIOXYDE);//garage
            Measure last03000003fe8eCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,_03000003fe8e_CARBON_DIOXYDE);//pj
            double totalCarbonDioxydeWeight1=0d;
            if(last70ee50000deaCarbonDioxyde!=null)
                totalCarbonDioxydeWeight1+=1.87d*(66d-11.7d)*2.5d*last70ee50000deaCarbonDioxyde.getValue()/1e6d;//salon
            if(last030000000216CarbonDioxyde!=null)
                totalCarbonDioxydeWeight1+=1.87d*11.7d*2.5d*last030000000216CarbonDioxyde.getValue()/1e6d;//filles
            double totalCarbonDioxydeWeight2=0d;
            if(last03000000076eCarbonDioxyde!=null)
                totalCarbonDioxydeWeight1+=1.87d*30d*2d*last03000000076eCarbonDioxyde.getValue()/1e6d;//garage
            if(last03000003fe8eCarbonDioxyde!=null)
                totalCarbonDioxydeWeight2+=1.87d*(66d-30d)*2d*last03000003fe8eCarbonDioxyde.getValue()/1e6d;//pj
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Font baseFont=Main.FREDOKA_ONE_FONT.deriveFont(24f);
                g2d.setFont(baseFont);
                g2d.drawString("Masse totale de CO2 :",5,25);
                g2d.drawString("- Étage="+DECIMAL_000_FORMAT.format(totalCarbonDioxydeWeight1)+" kg",5,55);
                g2d.drawString("- Sous-sol="+DECIMAL_000_FORMAT.format(totalCarbonDioxydeWeight2)+" kg",5,85);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("co2.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH).writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error when updating page \""+getName()+"\"");
                e.printStackTrace();
            }
        }
        return this;
    }

    public static void main(String[] args)
    {
        new CarbonDioxydeWeightPage(null).potentiallyUpdate();
    }
}
