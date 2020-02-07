package sky.epaperscreenupdater;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sky.program.Duration;

public class EnergyConsumptionPage extends AbstractSinglePage
{
    public EnergyConsumptionPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Consommations d'énergie";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(10).minutePlus(37).second();
    }

    protected void populateImage(Graphics2D g2d) throws Exception
    {
        Font baseFont=Main.FREDOKA_ONE_FONT.deriveFont(11f);
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
        for(int i=0;i<10;i++)
            g2d.drawLine(0,i*11+10,295,i*11+10);
        g2d.drawLine(0,9*11+10+1,295,9*11+10+1);
        for(int rank=1;rank<=10;rank++)
            g2d.drawString(todayEnergyConsumption.getConsumerName(rank),1,rank*11-2);
        g2d.drawString("Total",1,11*11-2+4);
        g2d.drawLine(94,0,94,127);
        for(int rank=1;rank<=10;rank++)
            g2d.drawString(DECIMAL_000_FORMAT.format(todayEnergyConsumption.getConsumerConsumption(rank))+" kWh",96,rank*11-2);
        g2d.drawString(DECIMAL_000_FORMAT.format(todayEnergyConsumption.getTotalOfConsumptions())+" kWh",96,11*11-2+4);
        g2d.drawLine(156,0,156,127);
        for(int rank=1;rank<=10;rank++)
            g2d.drawString(DECIMAL_00_FORMAT.format(todayEnergyConsumption.getConsumerPrice(rank))+" €",158,rank*11-2);
        g2d.drawString(DECIMAL_00_FORMAT.format(todayEnergyConsumption.getTotalOfPrices())+" €",158,11*11-2+4);
        g2d.drawLine(193,0,193,127);
        for(int rank=1;rank<=10;rank++)
            g2d.drawString(DECIMAL_000_FORMAT.format(yesterdayEnergyConsumption.getConsumerConsumption(rank))+" kWh",195,rank*11-2);
        g2d.drawString(DECIMAL_000_FORMAT.format(yesterdayEnergyConsumption.getTotalOfConsumptions())+" kWh",195,11*11-2+4);
        g2d.drawLine(255,0,255,127);
        for(int rank=1;rank<=10;rank++)
            g2d.drawString(DECIMAL_00_FORMAT.format(yesterdayEnergyConsumption.getConsumerPrice(rank))+" €",257,rank*11-2);
        g2d.drawString(DECIMAL_00_FORMAT.format(yesterdayEnergyConsumption.getTotalOfPrices())+" €",257,11*11-2+4);
    }

    protected String getDebugImageFileName()
    {
        return "energy.png";
    }

    public static void main(String[] args)
    {
        new EnergyConsumptionPage(null).potentiallyUpdate();
    }
}
