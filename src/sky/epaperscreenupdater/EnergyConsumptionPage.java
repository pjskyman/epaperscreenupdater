package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sky.program.Duration;

public class EnergyConsumptionPage extends AbstractPage
{
    private long lastRefreshTime;

    public EnergyConsumptionPage()
    {
        lastRefreshTime=0L;
    }

    public int getSerial()
    {
        return 15;
    }

    public String getName()
    {
        return "Consommations d'énergie";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(10).minute())
        {
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(11f);
                g2d.setFont(baseFont);
                GregorianCalendar calendar=new GregorianCalendar();
                int nowHour=calendar.get(Calendar.HOUR_OF_DAY);
                int nowMinute=calendar.get(Calendar.MINUTE);
                if(nowHour<6||nowHour==6&&nowMinute<2)
                    calendar.setTimeInMillis(calendar.getTimeInMillis()-Duration.of(1).day());
                int todayYear=calendar.get(Calendar.YEAR);
                int todayMonth=calendar.get(Calendar.MONTH)+1;
                int todayDay=calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTimeInMillis(calendar.getTimeInMillis()-Duration.of(1).day());
                int yesterdayYear=calendar.get(Calendar.YEAR);
                int yesterdayMonth=calendar.get(Calendar.MONTH)+1;
                int yesterdayDay=calendar.get(Calendar.DAY_OF_MONTH);
                EnergyConsumption todayEnergyConsumption=EnergyConsumptionProvider.calculateEnergyConsumption(todayDay,todayMonth,todayYear);
                EnergyConsumption yesterdayEnergyConsumption=EnergyConsumptionProvider.calculateEnergyConsumption(yesterdayDay,yesterdayMonth,yesterdayYear);
                for(int i=0;i<12;i++)
                    g2d.drawLine(0,i*11+10,295,i*11+10);
                for(int rank=1;rank<=10;rank++)
                    g2d.drawString(todayEnergyConsumption.getConsumerName(rank),1,rank*11-2);
                g2d.drawString("Total",1,11*11-2);
                g2d.drawLine(94,0,94,120);
                for(int rank=1;rank<=10;rank++)
                    g2d.drawString(ENERGY_FORMAT.format(todayEnergyConsumption.getConsumerConsumption(rank))+" kWh",96,rank*11-2);
                g2d.drawString(ENERGY_FORMAT.format(todayEnergyConsumption.getTotalOfConsumptions())+" kWh",96,11*11-2);
                g2d.drawLine(156,0,156,120);
                for(int rank=1;rank<=10;rank++)
                    g2d.drawString(PRICE_FORMAT.format(todayEnergyConsumption.getConsumerPrice(rank))+" €",158,rank*11-2);
                g2d.drawString(PRICE_FORMAT.format(todayEnergyConsumption.getTotalOfPrices())+" €",158,11*11-2);
                g2d.drawLine(193,0,193,120);
                for(int rank=1;rank<=10;rank++)
                    g2d.drawString(ENERGY_FORMAT.format(yesterdayEnergyConsumption.getConsumerConsumption(rank))+" kWh",195,rank*11-2);
                g2d.drawString(ENERGY_FORMAT.format(yesterdayEnergyConsumption.getTotalOfConsumptions())+" kWh",195,11*11-2);
                g2d.drawLine(255,0,255,120);
                for(int rank=1;rank<=10;rank++)
                    g2d.drawString(PRICE_FORMAT.format(yesterdayEnergyConsumption.getConsumerPrice(rank))+" €",257,rank*11-2);
                g2d.drawString(PRICE_FORMAT.format(yesterdayEnergyConsumption.getTotalOfPrices())+" €",257,11*11-2);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("energy.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels().writeImage(sourceImage);
                Logger.LOGGER.info("Page "+getSerial()+" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    public Pixels getPixels()
    {
        return pixels;
    }

    public boolean hasHighFrequency()
    {
        return false;
    }

    public static void main(String[] args)
    {
        new EnergyConsumptionPage().potentiallyUpdate();
    }
}
