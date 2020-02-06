package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sky.program.Duration;

public class TodayPricePage extends AbstractSinglePage
{
    private long lastRefreshTime;

    public TodayPricePage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Prix d'aujourd'hui";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(5).minutePlus(42).second())
        {
            Logger.LOGGER.info("Page \""+getName()+"\" needs to be updated");
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Font baseFont=Main.FREDOKA_ONE_FONT.deriveFont(90f);
                g2d.setFont(baseFont);
                GregorianCalendar calendar=new GregorianCalendar();
                int nowHour=calendar.get(Calendar.HOUR_OF_DAY);
                int nowMinute=calendar.get(Calendar.MINUTE);
                if(nowHour<6||nowHour==6&&nowMinute<2)
                    calendar.setTimeInMillis(calendar.getTimeInMillis()-Duration.of(1).day());
                int todayYear=calendar.get(Calendar.YEAR);
                int todayMonth=calendar.get(Calendar.MONTH)+1;
                int todayDay=calendar.get(Calendar.DAY_OF_MONTH);
                EnergyConsumption todayEnergyConsumption=EnergyConsumptionProvider.calculateEnergyConsumption(todayDay,todayMonth,todayYear);
                String todayPriceString=DECIMAL_00_FORMAT.format(todayEnergyConsumption.getTotalOfPrices())+" €";
                int todayPriceStringWidth=(int)Math.ceil(baseFont.getStringBounds(todayPriceString,g2d.getFontRenderContext()).getWidth());
                int todayPriceStringHeight=(int)Math.ceil(baseFont.getStringBounds(todayPriceString,g2d.getFontRenderContext()).getHeight());
                g2d.drawString(todayPriceString,148-todayPriceStringWidth/2,45+todayPriceStringHeight/2);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("today_price.png")))
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
        new TodayPricePage(null).potentiallyUpdate();
    }
}
