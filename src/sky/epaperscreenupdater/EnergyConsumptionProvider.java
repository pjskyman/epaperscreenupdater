package sky.epaperscreenupdater;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import sky.program.Duration;

public class EnergyConsumptionProvider
{
    public static final double BLUE_DAY_OFF_PEAK_HOUR_PRICE=.1113d;
    public static final double BLUE_DAY_PEAK_HOUR_PRICE=.1327d;
    public static final double WHITE_DAY_OFF_PEAK_HOUR_PRICE=.1347d;
    public static final double WHITE_DAY_PEAK_HOUR_PRICE=.1629d;
    public static final double RED_DAY_OFF_PEAK_HOUR_PRICE=.1747d;
    public static final double RED_DAY_PEAK_HOUR_PRICE=.5482d;

    public static List<InstantaneousConsumption> getInstantaneousConsumptions(int year)
    {
        return getInstantaneousConsumptions(1,1,year,31,12,year);
    }

    public static List<InstantaneousConsumption> getInstantaneousConsumptions(int month,int year)
    {
        return getInstantaneousConsumptions(1,month,year,new GregorianCalendar(year,month-1,1).getActualMaximum(Calendar.DAY_OF_MONTH),month,year);
    }

    public static List<InstantaneousConsumption> getInstantaneousConsumptions(int day,int month,int year)
    {
        return getInstantaneousConsumptions(day,month,year,day,month,year);
    }

    public static List<InstantaneousConsumption> getInstantaneousConsumptions(int day1,int month1,int year1,int day2,int month2,int year2)
    {
        try
        {
            GregorianCalendar startCalendar=new GregorianCalendar();
            startCalendar.clear();
            startCalendar.set(Calendar.YEAR,year1);
            startCalendar.set(Calendar.MONTH,month1-1);
            startCalendar.set(Calendar.DAY_OF_MONTH,day1);
            startCalendar.set(Calendar.HOUR_OF_DAY,6);
            startCalendar.set(Calendar.MINUTE,2);
            GregorianCalendar endCalendar=new GregorianCalendar();
            endCalendar.clear();
            endCalendar.set(Calendar.YEAR,year2);
            endCalendar.set(Calendar.MONTH,month2-1);
            endCalendar.set(Calendar.DAY_OF_MONTH,day2);
            endCalendar.set(Calendar.HOUR_OF_DAY,6);
            endCalendar.set(Calendar.MINUTE,2);
            endCalendar.setTimeInMillis(endCalendar.getTimeInMillis()+Duration.of(1).day());
            int endYear=endCalendar.get(Calendar.YEAR);
            int endMonth=endCalendar.get(Calendar.MONTH);
            int endDay=endCalendar.get(Calendar.DAY_OF_MONTH);
            endCalendar.clear();
            endCalendar.set(Calendar.YEAR,endYear);
            endCalendar.set(Calendar.MONTH,endMonth);
            endCalendar.set(Calendar.DAY_OF_MONTH,endDay);
            endCalendar.set(Calendar.HOUR_OF_DAY,6);
            endCalendar.set(Calendar.MINUTE,2);
            try(Connection connection=Database.getConnection())
            {
                long startTime=System.currentTimeMillis();
                List<InstantaneousConsumption> instantaneousConsumptions=new ArrayList<>();
                try(Statement statement=connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY))
                {
                    try(ResultSet resultSet=statement.executeQuery("SELECT * FROM instantaneous_consumption WHERE time >= "+startCalendar.getTimeInMillis()+" AND time < "+endCalendar.getTimeInMillis()+" ORDER BY time;"))
                    {
                        while(resultSet.next())
                        {
                            long time=resultSet.getLong("time");
                            PricingPeriod pricingPeriod=PricingPeriod.getPricingPeriodForCode(resultSet.getInt("pricingPeriod"));
                            double blueDayOffPeakHourTotal=resultSet.getDouble("blueDayOffPeakHourTotal");
                            double blueDayPeakHourTotal=resultSet.getDouble("blueDayPeakHourTotal");
                            double whiteDayOffPeakHourTotal=resultSet.getDouble("whiteDayOffPeakHourTotal");
                            double whiteDayPeakHourTotal=resultSet.getDouble("whiteDayPeakHourTotal");
                            double redDayOffPeakHourTotal=resultSet.getDouble("redDayOffPeakHourTotal");
                            double redDayPeakHourTotal=resultSet.getDouble("redDayPeakHourTotal");
                            String consumer1Name=resultSet.getString("consumer1Name");
                            int consumer1Consumption=resultSet.getInt("consumer1Consumption");
                            String consumer2Name=resultSet.getString("consumer2Name");
                            int consumer2Consumption=resultSet.getInt("consumer2Consumption");
                            String consumer3Name=resultSet.getString("consumer3Name");
                            int consumer3Consumption=resultSet.getInt("consumer3Consumption");
                            String consumer4Name=resultSet.getString("consumer4Name");
                            int consumer4Consumption=resultSet.getInt("consumer4Consumption");
                            String consumer5Name=resultSet.getString("consumer5Name");
                            int consumer5Consumption=resultSet.getInt("consumer5Consumption");
                            String consumer6Name=resultSet.getString("consumer6Name");
                            int consumer6Consumption=resultSet.getInt("consumer6Consumption");
                            String consumer7Name=resultSet.getString("consumer7Name");
                            int consumer7Consumption=resultSet.getInt("consumer7Consumption");
                            String consumer8Name=resultSet.getString("consumer8Name");
                            int consumer8Consumption=resultSet.getInt("consumer8Consumption");
                            String consumer9Name=resultSet.getString("consumer9Name");
                            int consumer9Consumption=resultSet.getInt("consumer9Consumption");
                            String consumer10Name=resultSet.getString("consumer10Name");
                            int consumer10Consumption=resultSet.getInt("consumer10Consumption");
                            instantaneousConsumptions.add(new InstantaneousConsumption(time,
                                    pricingPeriod,
                                    blueDayOffPeakHourTotal,
                                    blueDayPeakHourTotal,
                                    whiteDayOffPeakHourTotal,
                                    whiteDayPeakHourTotal,
                                    redDayOffPeakHourTotal,
                                    redDayPeakHourTotal,
                                    consumer1Name,
                                    consumer1Consumption,
                                    consumer2Name,
                                    consumer2Consumption,
                                    consumer3Name,
                                    consumer3Consumption,
                                    consumer4Name,
                                    consumer4Consumption,
                                    consumer5Name,
                                    consumer5Consumption,
                                    consumer6Name,
                                    consumer6Consumption,
                                    consumer7Name,
                                    consumer7Consumption,
                                    consumer8Name,
                                    consumer8Consumption,
                                    consumer9Name,
                                    consumer9Consumption,
                                    consumer10Name,
                                    consumer10Consumption));
                        }
                    }
                }
//                List<InstantaneousConsumption> list=new ArrayList<>();
//                instantaneousConsumptions.stream().filter(t->
//                {
//                    if(t.getPricingPeriod().isBlueDay())
//                        return instantaneousConsumptions.get(instantaneousConsumptions.size()/2).getPricingPeriod().isBlueDay();
//                    else
//                        if(t.getPricingPeriod().isWhiteDay())
//                            return instantaneousConsumptions.get(instantaneousConsumptions.size()/2).getPricingPeriod().isWhiteDay();
//                        else
//                            return instantaneousConsumptions.get(instantaneousConsumptions.size()/2).getPricingPeriod().isRedDay();
//                }).forEach(list::add);
//                Logger.LOGGER.info(instantaneousConsumptions.size()+" rows fetched in "+(System.currentTimeMillis()-startTime)+" ms");
                return instantaneousConsumptions;
            }
        }
        catch(NotAvailableDatabaseException|SQLException e)
        {
            Logger.LOGGER.error("Unable to parse the request response ("+e.toString()+")");
            return new ArrayList<>(0);
        }
    }

    public static EnergyConsumption calculateEnergyConsumption(int year)
    {
        return calculateEnergyConsumption(getInstantaneousConsumptions(year));
    }

    public static EnergyConsumption calculateEnergyConsumption(int month,int year)
    {
        return calculateEnergyConsumption(getInstantaneousConsumptions(month,year));
    }

    public static EnergyConsumption calculateEnergyConsumption(int day,int month,int year)
    {
        return calculateEnergyConsumption(getInstantaneousConsumptions(day,month,year));
    }

    public static EnergyConsumption calculateEnergyConsumption(int day1,int month1,int year1,int day2,int month2,int year2)
    {
        return calculateEnergyConsumption(getInstantaneousConsumptions(day1,month1,year1,day2,month2,year2));
    }

    public static EnergyConsumption calculateEnergyConsumption(List<InstantaneousConsumption> instantaneousConsumptions)
    {
        double[] accumulations=new double[10];
        double[] prices=new double[10];
        for(int i=0;i<instantaneousConsumptions.size();i++)
        {
            double timeOffset;//en secondes
            if(i==0)
                timeOffset=(double)(instantaneousConsumptions.get(1).getTime()-instantaneousConsumptions.get(0).getTime())/1000d;
            else
                if(i==instantaneousConsumptions.size()-1)
                    timeOffset=(double)(instantaneousConsumptions.get(instantaneousConsumptions.size()-1).getTime()-instantaneousConsumptions.get(instantaneousConsumptions.size()-2).getTime())/1000d;
                else
                {
                    InstantaneousConsumption previousInstantaneousConsumption=instantaneousConsumptions.get(i-1);
                    InstantaneousConsumption nextInstantaneousConsumption=instantaneousConsumptions.get(i+1);
                    timeOffset=(double)(nextInstantaneousConsumption.getTime()-previousInstantaneousConsumption.getTime())/2d/1000d;
                }
            for(int j=0;j<10;j++)
            {
                double increment=(double)instantaneousConsumptions.get(i).getConsumerConsumption(j+1)*timeOffset;
                accumulations[j]+=increment;
                PricingPeriod pricingPeriod=instantaneousConsumptions.get(i).getPricingPeriod();
                if(pricingPeriod==PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR)
                    prices[j]+=increment*BLUE_DAY_OFF_PEAK_HOUR_PRICE;
                else
                    if(pricingPeriod==PricingPeriod.BLUE_DAY_PEAK_HOUR)
                        prices[j]+=increment*BLUE_DAY_PEAK_HOUR_PRICE;
                    else
                        if(pricingPeriod==PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR)
                            prices[j]+=increment*WHITE_DAY_OFF_PEAK_HOUR_PRICE;
                        else
                            if(pricingPeriod==PricingPeriod.WHITE_DAY_PEAK_HOUR)
                                prices[j]+=increment*WHITE_DAY_PEAK_HOUR_PRICE;
                            else
                                if(pricingPeriod==PricingPeriod.RED_DAY_OFF_PEAK_HOUR)
                                    prices[j]+=increment*RED_DAY_OFF_PEAK_HOUR_PRICE;
                                else
                                    if(pricingPeriod==PricingPeriod.RED_DAY_PEAK_HOUR)
                                        prices[j]+=increment*RED_DAY_PEAK_HOUR_PRICE;
            }
        }
        InstantaneousConsumption instantaneousConsumption=instantaneousConsumptions.isEmpty()?Main.loadInstantaneousConsumptions(1).get(0):instantaneousConsumptions.get(0);
        return new EnergyConsumption(instantaneousConsumption.getConsumer1Name(),accumulations[0]/3600d/1000d,prices[0]/3600d/1000d,
                                     instantaneousConsumption.getConsumer2Name(),accumulations[1]/3600d/1000d,prices[1]/3600d/1000d,
                                     instantaneousConsumption.getConsumer3Name(),accumulations[2]/3600d/1000d,prices[2]/3600d/1000d,
                                     instantaneousConsumption.getConsumer4Name(),accumulations[3]/3600d/1000d,prices[3]/3600d/1000d,
                                     instantaneousConsumption.getConsumer5Name(),accumulations[4]/3600d/1000d,prices[4]/3600d/1000d,
                                     instantaneousConsumption.getConsumer6Name(),accumulations[5]/3600d/1000d,prices[5]/3600d/1000d,
                                     instantaneousConsumption.getConsumer7Name(),accumulations[6]/3600d/1000d,prices[6]/3600d/1000d,
                                     instantaneousConsumption.getConsumer8Name(),accumulations[7]/3600d/1000d,prices[7]/3600d/1000d,
                                     instantaneousConsumption.getConsumer9Name(),accumulations[8]/3600d/1000d,prices[8]/3600d/1000d,
                                     instantaneousConsumption.getConsumer10Name(),accumulations[9]/3600d/1000d,prices[9]/3600d/1000d);
    }

    public static void main(String[] args)
    {
        long startTime=System.currentTimeMillis();
        EnergyConsumption energyConsumption=calculateEnergyConsumption(8,5,2018);
        System.out.println(AbstractPage.DECIMAL_000_FORMAT.format(energyConsumption.getTotalOfConsumptions())+" kWh");
        System.out.println(AbstractPage.DECIMAL_00_FORMAT.format(energyConsumption.getTotalOfPrices())+" €");
        System.out.println("Total "+(System.currentTimeMillis()-startTime)+" ms");
    }
}
