package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sky.housecommon.ElectricityUtils;
import sky.housecommon.EnergyConsumption;
import sky.program.Duration;

public class TodayPricePage extends AbstractSinglePage
{
    public TodayPricePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prix d'aujourd'hui";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(5).minutePlus(42).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(90f);
        g2d.setFont(baseFont);
        GregorianCalendar calendar=new GregorianCalendar();
        int nowHour=calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute=calendar.get(Calendar.MINUTE);
        if(nowHour<6)
            calendar.setTimeInMillis(calendar.getTimeInMillis()-Duration.of(1).day());
        int todayYear=calendar.get(Calendar.YEAR);
        int todayMonth=calendar.get(Calendar.MONTH)+1;
        int todayDay=calendar.get(Calendar.DAY_OF_MONTH);
        EnergyConsumption todayEnergyConsumption=ElectricityUtils.calculateEnergyConsumption(todayDay,todayMonth,todayYear);
        String todayPriceString=DECIMAL_00_FORMAT.format(todayEnergyConsumption.getTotalOfPrices())+" €";
        int todayPriceStringWidth=(int)Math.ceil(baseFont.getStringBounds(todayPriceString,g2d.getFontRenderContext()).getWidth());
        int todayPriceStringHeight=(int)Math.ceil(baseFont.getStringBounds(todayPriceString,g2d.getFontRenderContext()).getHeight());
        g2d.drawString(todayPriceString,148-todayPriceStringWidth/2,45+todayPriceStringHeight/2);
    }

    protected String getDebugImageFileName()
    {
        return "today_price.png";
    }

    public static void main(String[] args)
    {
        new TodayPricePage(null).potentiallyUpdate();
    }
}
