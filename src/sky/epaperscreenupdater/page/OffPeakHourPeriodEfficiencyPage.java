package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(10).minuteMinus(27).secondPlus(300).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(17f);
        g2d.setFont(baseFont);

        GregorianCalendar calendar=new GregorianCalendar();
        int nowHour=calendar.get(Calendar.HOUR_OF_DAY);
        if(nowHour<6)
            calendar.setTimeInMillis(calendar.getTimeInMillis()-Duration.of(1).day());
        int todayYear=calendar.get(Calendar.YEAR);
        int todayMonth=calendar.get(Calendar.MONTH)+1;
        int todayDay=calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTimeInMillis(calendar.getTimeInMillis()-Duration.of(1).day());
        int yesterdayYear=calendar.get(Calendar.YEAR);
        int yesterdayMonth=calendar.get(Calendar.MONTH)+1;
        int yesterdayDay=calendar.get(Calendar.DAY_OF_MONTH);

        g2d.drawString("Efficacité des heures creuses",1,17);

        OffPeakHourPeriodEfficiency yesterdayOffPeakHourPeriodEfficiency=ElectricityUtils.calculateOffPeakHourPeriodEfficiency(yesterdayDay,yesterdayMonth,yesterdayYear);
        String yesterdayString="\u2013 Hier (";
        PricingPeriod yesterdayPricingPeriod=yesterdayOffPeakHourPeriodEfficiency.getPricingPeriod();
        yesterdayString+=(yesterdayPricingPeriod.isBlueDay()?"bleu":yesterdayPricingPeriod.isWhiteDay()?"blanc":"rouge");
        yesterdayString+=") : ";
        double yesterdaySavedMoney;
        if(yesterdayPricingPeriod.isBlueDay())
            yesterdaySavedMoney=yesterdayOffPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.BLUE_DAY_PEAK_HOUR.getPrice()-PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice());
        else
            if(yesterdayPricingPeriod.isWhiteDay())
                yesterdaySavedMoney=yesterdayOffPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.WHITE_DAY_PEAK_HOUR.getPrice()-PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice());
            else
                yesterdaySavedMoney=yesterdayOffPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.RED_DAY_PEAK_HOUR.getPrice()-PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice());
        yesterdayString+=DECIMAL_00_FORMAT.format(yesterdaySavedMoney)+" € économisés";
        g2d.drawString(yesterdayString,1,35);

        g2d.drawString("kWh",1,53);
        g2d.drawRect(45,40,195,12);
        g2d.drawString(DECIMAL_0_FORMAT.format(yesterdayOffPeakHourPeriodEfficiency.getConsumptionEfficiency())+" %",245,53);
        for(int i=46;i<46+(int)(yesterdayOffPeakHourPeriodEfficiency.getConsumptionEfficiency()*194d/100d);i++)
            g2d.drawLine(i,40,i,40+12);
        int thirtyThreePercentAbscissa=46+(int)(194d/3d);
        g2d.drawLine(thirtyThreePercentAbscissa,39,thirtyThreePercentAbscissa,38);
        g2d.drawLine(thirtyThreePercentAbscissa,53,thirtyThreePercentAbscissa,54);

        g2d.drawString("€",1,71);
        g2d.drawRect(45,58,195,12);
        g2d.drawString(DECIMAL_0_FORMAT.format(yesterdayOffPeakHourPeriodEfficiency.getPriceEfficiency())+" %",245,71);
        for(int i=46;i<46+(int)(yesterdayOffPeakHourPeriodEfficiency.getPriceEfficiency()*194d/100d);i++)
            g2d.drawLine(i,58,i,58+12);
        double yesterdayProrata;
        if(yesterdayPricingPeriod.isBlueDay())
            yesterdayProrata=(1d/3d*PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice())/(2d/3d*PricingPeriod.BLUE_DAY_PEAK_HOUR.getPrice()+1d/3d*PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice());
        else
            if(yesterdayPricingPeriod.isWhiteDay())
                yesterdayProrata=(1d/3d*PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice())/(2d/3d*PricingPeriod.WHITE_DAY_PEAK_HOUR.getPrice()+1d/3d*PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice());
            else
                yesterdayProrata=(1d/3d*PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice())/(2d/3d*PricingPeriod.RED_DAY_PEAK_HOUR.getPrice()+1d/3d*PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice());
        int yesterdayProrataAbscissa=46+(int)(194d*yesterdayProrata);
        g2d.drawLine(yesterdayProrataAbscissa,57,yesterdayProrataAbscissa,56);
        g2d.drawLine(yesterdayProrataAbscissa,71,yesterdayProrataAbscissa,72);

        OffPeakHourPeriodEfficiency todayOffPeakHourPeriodEfficiency=ElectricityUtils.calculateOffPeakHourPeriodEfficiency(todayDay,todayMonth,todayYear);
        double efficiency;
        try
        {
            efficiency=DECIMAL_0_FORMAT.parse(DECIMAL_0_FORMAT.format(todayOffPeakHourPeriodEfficiency.getConsumptionEfficiency())).doubleValue();
        }
        catch(ParseException e)
        {
            efficiency=0d;
        }
        if(efficiency>0d)
        {
            String todayString="\u2013 Aujourd'hui (";
            PricingPeriod todayPricingPeriod=todayOffPeakHourPeriodEfficiency.getPricingPeriod();
            todayString+=(todayPricingPeriod.isBlueDay()?"bleu":todayPricingPeriod.isWhiteDay()?"blanc":"rouge");
            todayString+=") : ";
            double todaySavedMoney;
            if(todayPricingPeriod.isBlueDay())
                todaySavedMoney=todayOffPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.BLUE_DAY_PEAK_HOUR.getPrice()-PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice());
            else
                if(todayPricingPeriod.isWhiteDay())
                    todaySavedMoney=todayOffPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.WHITE_DAY_PEAK_HOUR.getPrice()-PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice());
                else
                    todaySavedMoney=todayOffPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.RED_DAY_PEAK_HOUR.getPrice()-PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice());
            todayString+=DECIMAL_00_FORMAT.format(todaySavedMoney)+" € écon.";
            g2d.drawString(todayString,1,89);

            g2d.drawString("kWh",1,107);
            g2d.drawRect(45,94,195,12);
            g2d.drawString(DECIMAL_0_FORMAT.format(todayOffPeakHourPeriodEfficiency.getConsumptionEfficiency())+" %",245,107);
            for(int i=46;i<46+(int)(todayOffPeakHourPeriodEfficiency.getConsumptionEfficiency()*194d/100d);i++)
                g2d.drawLine(i,94,i,94+12);
            g2d.drawLine(thirtyThreePercentAbscissa,93,thirtyThreePercentAbscissa,92);
            g2d.drawLine(thirtyThreePercentAbscissa,107,thirtyThreePercentAbscissa,108);

            g2d.drawString("€",1,125);
            g2d.drawRect(45,112,195,12);
            g2d.drawString(DECIMAL_0_FORMAT.format(todayOffPeakHourPeriodEfficiency.getPriceEfficiency())+" %",245,125);
            for(int i=46;i<46+(int)(todayOffPeakHourPeriodEfficiency.getPriceEfficiency()*194d/100d);i++)
                g2d.drawLine(i,112,i,112+12);
            double todayProrata;
            if(todayPricingPeriod.isBlueDay())
                todayProrata=(1d/3d*PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice())/(2d/3d*PricingPeriod.BLUE_DAY_PEAK_HOUR.getPrice()+1d/3d*PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice());
            else
                if(todayPricingPeriod.isWhiteDay())
                    todayProrata=(1d/3d*PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice())/(2d/3d*PricingPeriod.WHITE_DAY_PEAK_HOUR.getPrice()+1d/3d*PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice());
                else
                    todayProrata=(1d/3d*PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice())/(2d/3d*PricingPeriod.RED_DAY_PEAK_HOUR.getPrice()+1d/3d*PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice());
            int todayProrataAbscissa=46+(int)(194d*todayProrata);
            g2d.drawLine(todayProrataAbscissa,111,todayProrataAbscissa,110);
            g2d.drawLine(todayProrataAbscissa,125,todayProrataAbscissa,126);
        }
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
