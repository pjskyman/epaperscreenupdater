package sky.epaperscreenupdater;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ConsumptionProfileCalculator
{
    private final List<WasherGenericConsumption> washerGenericConsumptions;

    public static ConsumptionProfileCalculator construct(List<InstantaneousConsumption> instantaneousConsumptions)
    {
        List<WasherGenericConsumption> washerGenericConsumptions=new ArrayList<>(instantaneousConsumptions.size());
        instantaneousConsumptions.stream()
                .map(WasherGenericConsumption::new)
                .forEach(washerGenericConsumptions::add);
        return new ConsumptionProfileCalculator(washerGenericConsumptions);
    }

    public ConsumptionProfileCalculator(List<WasherGenericConsumption> washerGenericConsumptions)
    {
        this.washerGenericConsumptions=washerGenericConsumptions;
    }

    public double getTotalPricing(int startHour,int startMinute,List<PricingPeriodZone> pricingPeriodZones)
    {
        double totalPrice=0d;
        for(int i=0;i<washerGenericConsumptions.size();i++)
        {
            double timeOffset;//en secondes
            if(i==0)
                timeOffset=(double)(washerGenericConsumptions.get(1).getTime()-washerGenericConsumptions.get(0).getTime())/1000d;
            else
                if(i==washerGenericConsumptions.size()-1)
                    timeOffset=(double)(washerGenericConsumptions.get(washerGenericConsumptions.size()-1).getTime()-washerGenericConsumptions.get(washerGenericConsumptions.size()-2).getTime())/1000d;
                else
                {
                    WasherGenericConsumption previousWasherGenericConsumption=washerGenericConsumptions.get(i-1);
                    WasherGenericConsumption nextWasherGenericConsumption=washerGenericConsumptions.get(i+1);
                    timeOffset=(double)(nextWasherGenericConsumption.getTime()-previousWasherGenericConsumption.getTime())/2d/1000d;
                }
            int time=(int)((washerGenericConsumptions.get(i).getTime()-washerGenericConsumptions.get(0).getTime())/1000L)/60+startHour*60+startMinute;
            int hour=time/60;
            int minute=time-hour*60;
            try
            {
                PricingPeriod pricingPeriod=pricingPeriodZones.stream()
                        .filter(pricingPeriodZone->pricingPeriodZone.isTimeInside(hour,minute))
                        .findFirst()
                        .get()
                        .getPricingPeriod();
                totalPrice+=(double)washerGenericConsumptions.get(i).getConsumption()*timeOffset*pricingPeriod.getPrice();
            }
            catch(NoSuchElementException e)
            {
                return 0d;
            }
        }
        return totalPrice/3600d/1000d;
    }
}
