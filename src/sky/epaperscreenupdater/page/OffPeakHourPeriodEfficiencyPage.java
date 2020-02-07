package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class OffPeakHourPeriodEfficiencyPage extends AbstractSinglePage
{
    public OffPeakHourPeriodEfficiencyPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Efficacité H.C.";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(10).minuteMinus(27).secondPlus(300).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(18f);
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

        g2d.drawString("Efficacité des heures creuses",1,17);

        g2d.drawString("\u2013 Hier :",1,35);
        OffPeakHourPeriodEfficiency yesterdayOffPeakHourPeriodEfficiency=EnergyConsumptionProvider.calculateOffPeakHourPeriodEfficiency(yesterdayDay,yesterdayMonth,yesterdayYear);

        g2d.drawString("kWh",1,53);
        g2d.drawRect(45,38,180,14);
        g2d.drawString(DECIMAL_0_FORMAT.format(yesterdayOffPeakHourPeriodEfficiency.getConsumptionEfficiency())+" %",230,53);
        for(int i=46;i<46+(int)(yesterdayOffPeakHourPeriodEfficiency.getConsumptionEfficiency()*179d/100d);i++)
            g2d.drawLine(i,38,i,38+14);

        g2d.drawString("€",1,71);
        g2d.drawRect(45,56,180,14);
        g2d.drawString(DECIMAL_0_FORMAT.format(yesterdayOffPeakHourPeriodEfficiency.getPriceEfficiency())+" %",230,71);
        for(int i=46;i<46+(int)(yesterdayOffPeakHourPeriodEfficiency.getPriceEfficiency()*179d/100d);i++)
            g2d.drawLine(i,56,i,56+14);

        g2d.drawString("\u2013 Aujourd'hui :",1,89);
        OffPeakHourPeriodEfficiency todayOffPeakHourPeriodEfficiency=EnergyConsumptionProvider.calculateOffPeakHourPeriodEfficiency(todayDay,todayMonth,todayYear);

        g2d.drawString("kWh",1,107);
        g2d.drawRect(45,92,180,14);
        g2d.drawString(DECIMAL_0_FORMAT.format(todayOffPeakHourPeriodEfficiency.getConsumptionEfficiency())+" %",230,107);
        for(int i=46;i<46+(int)(todayOffPeakHourPeriodEfficiency.getConsumptionEfficiency()*179d/100d);i++)
            g2d.drawLine(i,92,i,92+14);

        g2d.drawString("€",1,125);
        g2d.drawRect(45,110,180,14);
        g2d.drawString(DECIMAL_0_FORMAT.format(todayOffPeakHourPeriodEfficiency.getPriceEfficiency())+" %",230,125);
        for(int i=46;i<46+(int)(todayOffPeakHourPeriodEfficiency.getPriceEfficiency()*179d/100d);i++)
            g2d.drawLine(i,110,i,110+14);
    }

    protected String getDebugImageFileName()
    {
        return "eff_h_c.png";
    }

    public static void main(String[] args)
    {
        new OffPeakHourPeriodEfficiencyPage(null).potentiallyUpdate();
    }
}
