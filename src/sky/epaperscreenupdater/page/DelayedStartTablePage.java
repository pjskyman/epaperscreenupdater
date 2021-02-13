package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import sky.housecommon.InstantaneousConsumption;
import sky.housecommon.PricingPeriod;
import sky.program.Duration;

public class DelayedStartTablePage extends AbstractSinglePage
{
    public DelayedStartTablePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Abaque différés LV/LL";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(13).secondPlus(253).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(19f)
                .deriveFont(AffineTransform.getScaleInstance(.7d,1d));
        g2d.setFont(baseFont);
        GregorianCalendar calendar=new GregorianCalendar();
        int nowHour=calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute=calendar.get(Calendar.MINUTE);
        g2d.drawLine(0,21,296,21);
        g2d.drawLine(0,22,296,22);
        g2d.drawLine(24,43,296,43);
        g2d.drawLine(24,64,296,64);
        g2d.drawLine(0,85,296,85);
        g2d.drawLine(0,86,296,86);
        g2d.drawLine(24,107,296,107);
        g2d.drawLine(24,22,24,128);
        g2d.drawString("Type",21,18);
        g2d.drawString("LV",3,61);
        g2d.drawString("LL",4,114);
        g2d.drawString("50°",42,40);
        g2d.drawString("45°-65°",28,61);
        g2d.drawString("70°",42,82);
        g2d.drawString("40°",41,104);
        g2d.drawString("60°",42,125);
        g2d.drawLine(76,0,76,128);
        g2d.drawLine(77,0,77,128);
        if(nowHour<2||nowHour==2&&nowMinute==0||nowHour>=16)
        {
            String today="ND";
            InstantaneousConsumption instantaneousConsumption=ElectricityUtils.getLastInstantaneousConsumption();
            if(instantaneousConsumption!=null)
            {
                if(instantaneousConsumption.getPricingPeriod().isBlueDay())
                    today="BLEU";
                else
                    if(instantaneousConsumption.getPricingPeriod().isWhiteDay())
                        today="BLANC";
                    else
                        if(instantaneousConsumption.getPricingPeriod().isRedDay())
                            today="ROUGE";
            }
//            today="BLANC";
            String tomorrow=TomorrowUtils.getTomorrow();
//            String tomorrow="BLEU";
            List<PricingPeriodZone> pricingPeriodZones=new ArrayList<>();
            if(today.contains("BLEU"))
                pricingPeriodZones.add(new PricingPeriodZone(0,0,6,0,PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR));
            else
                if(today.contains("BLANC"))
                    pricingPeriodZones.add(new PricingPeriodZone(0,0,6,0,PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR));
                else
                    if(today.contains("ROUGE"))
                        pricingPeriodZones.add(new PricingPeriodZone(0,0,6,0,PricingPeriod.RED_DAY_OFF_PEAK_HOUR));
            if(tomorrow.contains("BLEU"))
                pricingPeriodZones.add(new PricingPeriodZone(6,0,20,0,PricingPeriod.BLUE_DAY_PEAK_HOUR));
            else
                if(tomorrow.contains("BLANC"))
                    pricingPeriodZones.add(new PricingPeriodZone(6,0,20,0,PricingPeriod.WHITE_DAY_PEAK_HOUR));
                else
                    if(tomorrow.contains("RED"))
                        pricingPeriodZones.add(new PricingPeriodZone(6,0,20,0,PricingPeriod.RED_DAY_PEAK_HOUR));
            if(!today.contains("ND")&&!tomorrow.contains("ND"))
            {
                g2d.drawString("Différé",90,18);
                g2d.drawLine(145,0,145,128);
                g2d.drawString("Coût",165,18);
                g2d.drawLine(214,0,214,128);
                g2d.drawString("Temps rest.",219,18);

                double[] infos=getInfos(WasherProfiles.getLV50(),pricingPeriodZones,1,12,nowHour,nowMinute);

                if(infos[2]!=-1d)
                {
                    String string=(int)infos[0]+" h";
                    int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(145+77)/2-stringWidth/2,40);

                    string=DECIMAL_000_FORMAT.format(infos[1])+" €";
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(214+145)/2-stringWidth/2,40);

                    string=formatTime((int)infos[2]);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(296+214)/2-stringWidth/2,40);
                }

                infos=getInfos(WasherProfiles.getLV4565(),pricingPeriodZones,1,12,nowHour,nowMinute);

                if(infos[2]!=-1d)
                {
                    String string=(int)infos[0]+" h";
                    int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(145+77)/2-stringWidth/2,61);

                    string=DECIMAL_000_FORMAT.format(infos[1])+" €";
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(214+145)/2-stringWidth/2,61);

                    string=formatTime((int)infos[2]);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(296+214)/2-stringWidth/2,61);
                }

                infos=getInfos(WasherProfiles.getLV70(),pricingPeriodZones,1,12,nowHour,nowMinute);

                if(infos[2]!=-1d)
                {
                    String string=(int)infos[0]+" h";
                    int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(145+77)/2-stringWidth/2,82);

                    string=DECIMAL_000_FORMAT.format(infos[1])+" €";
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(214+145)/2-stringWidth/2,82);

                    string=formatTime((int)infos[2]);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(296+214)/2-stringWidth/2,82);
                }

                infos=getInfos(WasherProfiles.getLL40(),pricingPeriodZones,3,9,nowHour,nowMinute);

                if(infos[2]!=-1d)
                {
                    String string=(int)infos[0]+" h";
                    int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(145+77)/2-stringWidth/2,104);

                    string=DECIMAL_000_FORMAT.format(infos[1])+" €";
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(214+145)/2-stringWidth/2,104);

                    string=formatTime((int)infos[2]);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(296+214)/2-stringWidth/2,104);
                }

                infos=getInfos(WasherProfiles.getLL60(),pricingPeriodZones,3,9,nowHour,nowMinute);

                if(infos[2]!=-1d)
                {
                    String string=(int)infos[0]+" h";
                    int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(145+77)/2-stringWidth/2,125);

                    string=DECIMAL_000_FORMAT.format(infos[1])+" €";
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(214+145)/2-stringWidth/2,125);

                    string=formatTime((int)infos[2]);
                    stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                    g2d.drawString(string,(296+214)/2-stringWidth/2,125);
                }
            }
        }
    }

    protected String getDebugImageFileName()
    {
        return "delayed_start2.png";
    }

    private static double[] getInfos(List<WasherGenericConsumption> washerGenericConsumptions,List<PricingPeriodZone> pricingPeriodZones,int delayOffset,int maxDelayHour,int nowHour,int nowMinute)
    {
        List<EstimatedPrice> estimatedPrices=new ArrayList<>();
        ConsumptionProfileCalculator consumptionProfileCalculator=new ConsumptionProfileCalculator(washerGenericConsumptions);
        for(int hour=0;hour<=8;hour++)
            for(int minute=0;minute<=59;minute++)
                estimatedPrices.add(new EstimatedPrice(hour,minute,consumptionProfileCalculator.getTotalPricing(hour,minute,pricingPeriodZones)));
        double bestPrice=estimatedPrices.get(0).getPrice();
        int index;
        for(index=1;index<estimatedPrices.size();index++)
            if(estimatedPrices.get(index).getPrice()>bestPrice)
                break;
        EstimatedPrice latestStart=estimatedPrices.get(--index);
        int latestStartHour=latestStart.getStartHour();
        int latestStartMinute=latestStart.getStartMinute();
        int delayHour;
        int remainingTime=-1;
        for(delayHour=0;delayHour<=maxDelayHour;delayHour+=delayOffset)
        {
            int delayedHour=(nowHour+delayHour)%24;
            if(delayedHour>=16)
                continue;
            if(delayedHour==9)
                break;
            int delayedMinute=nowMinute;
            if(delayedHour>latestStartHour||delayedHour==latestStartHour&&delayedMinute>latestStartMinute)
            {
                delayHour-=delayOffset;
                delayedHour-=delayOffset;
                remainingTime=latestStartHour*60+latestStartMinute-(delayedHour*60+delayedMinute);
                break;
            }
        }
        return new double[]{(double)delayHour,bestPrice,(double)remainingTime};
    }

    private static String formatTime(int time)
    {
        if(time<60)
            return time+" min";
        int hour=time/60;
        return hour+" h "+(time-hour*60)+" min";
    }

    public static void main(String[] args)
    {
        new DelayedStartTablePage(null).potentiallyUpdate();
    }
}
