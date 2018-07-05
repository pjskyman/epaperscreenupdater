package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class CO2WeightPage extends AbstractNetatmoPage
{
    private long lastRefreshTime;

    public CO2WeightPage(Page parentPage)
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
        if(now-lastRefreshTime>Duration.of(1).minutePlus(2).secondPlus(500).millisecond())
        {
            lastRefreshTime=now;
            Map<String,Measure[]> lastMeasures=getLastMeasures();
            Measure lastSalonCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,SALON_CARBON_DIOXYDE);
            Measure lastChambreCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,CHAMBRE_CARBON_DIOXYDE);
            Measure lastSalleDeBainCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,SALLE_DE_BAIN_CARBON_DIOXYDE);
            Measure lastSousSolCarbonDioxyde=HomeWeatherPage.getLastMeasure(lastMeasures,SOUS_SOL_CARBON_DIOXYDE);
            double totalCarbonDioxydeWeight1=0d;
            if(lastSalonCarbonDioxyde!=null)
                totalCarbonDioxydeWeight1+=1.87d*(66d-11.7d-11.7d-6d)*2.5d*lastSalonCarbonDioxyde.getValue()/1e6d;
            if(lastChambreCarbonDioxyde!=null)
                totalCarbonDioxydeWeight1+=1.87d*11.7d*2.5d*lastChambreCarbonDioxyde.getValue()/1e6d;
            if(lastSalleDeBainCarbonDioxyde!=null)
                totalCarbonDioxydeWeight1+=1.87d*(11.7d+6d)*2.5d*lastSalleDeBainCarbonDioxyde.getValue()/1e6d;
            double totalCarbonDioxydeWeight2=0d;
            if(lastSousSolCarbonDioxyde!=null)
                totalCarbonDioxydeWeight2+=1.87d*66d*2.5d*lastSousSolCarbonDioxyde.getValue()/1e6d;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(24f);
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
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    public static void main(String[] args)
    {
        new CO2WeightPage(null).potentiallyUpdate();
    }
}
