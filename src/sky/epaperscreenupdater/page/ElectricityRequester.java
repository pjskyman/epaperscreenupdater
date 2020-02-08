package sky.epaperscreenupdater.page;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import sky.epaperscreenupdater.Logger;
import sky.epaperscreenupdater.Main;
import sky.program.Duration;

public class ElectricityRequester
{
    private static final Random RANDOM=new Random();

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
            e.printStackTrace();
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
                prices[j]+=increment*instantaneousConsumptions.get(i).getPricingPeriod().getPrice();
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

    public static OffPeakHourPeriodEfficiency calculateOffPeakHourPeriodEfficiency(int year)
    {
        return calculateOffPeakHourPeriodEfficiency(getInstantaneousConsumptions(year));
    }

    public static OffPeakHourPeriodEfficiency calculateOffPeakHourPeriodEfficiency(int month,int year)
    {
        return calculateOffPeakHourPeriodEfficiency(getInstantaneousConsumptions(month,year));
    }

    public static OffPeakHourPeriodEfficiency calculateOffPeakHourPeriodEfficiency(int day,int month,int year)
    {
        return calculateOffPeakHourPeriodEfficiency(getInstantaneousConsumptions(day,month,year));
    }

    public static OffPeakHourPeriodEfficiency calculateOffPeakHourPeriodEfficiency(int day1,int month1,int year1,int day2,int month2,int year2)
    {
        return calculateOffPeakHourPeriodEfficiency(getInstantaneousConsumptions(day1,month1,year1,day2,month2,year2));
    }

    public static OffPeakHourPeriodEfficiency calculateOffPeakHourPeriodEfficiency(List<InstantaneousConsumption> instantaneousConsumptions)
    {
        double accumulation=0d;
        double price=0d;
        double offPeakAccumulation=0d;
        double offPeakPrice=0d;
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
            InstantaneousConsumption instantaneousConsumption=instantaneousConsumptions.get(i);
            double increment=(double)instantaneousConsumption.getTotalOfConsumptions()*timeOffset;
            accumulation+=increment;
            double priceIncrement=increment*instantaneousConsumption.getPricingPeriod().getPrice();
            price+=priceIncrement;
            if(instantaneousConsumption.getPricingPeriod().isOffPeakHourPeriod())
            {
                offPeakAccumulation+=increment;
                offPeakPrice+=priceIncrement;
            }
        }
        double consumptionEfficiency=offPeakAccumulation/accumulation;
        if(Double.isNaN(consumptionEfficiency)||Double.isInfinite(consumptionEfficiency))
            consumptionEfficiency=0d;
        double priceEfficiency=offPeakPrice/price;
        if(Double.isNaN(priceEfficiency)||Double.isInfinite(priceEfficiency))
            priceEfficiency=0d;
        PricingPeriod pricingPeriod;
        if(instantaneousConsumptions.size()>1000)
            pricingPeriod=instantaneousConsumptions.get(1000).getPricingPeriod();
        else
            if(!instantaneousConsumptions.isEmpty())
                pricingPeriod=instantaneousConsumptions.get(RANDOM.nextInt(instantaneousConsumptions.size())).getPricingPeriod();
            else
                pricingPeriod=PricingPeriod.BLUE_DAY_PEAK_HOUR;
        return new OffPeakHourPeriodEfficiency(consumptionEfficiency*100d,priceEfficiency*100d,pricingPeriod,offPeakAccumulation/3600d/1000d);
    }

    public static void main(String[] args)
    {
        for(int day=1;day<=10;day++)
        {
            System.out.println("day="+day);
            EnergyConsumption energyConsumption=calculateEnergyConsumption(day,2,2020);
            System.out.println(AbstractPage.DECIMAL_000_FORMAT.format(energyConsumption.getTotalOfConsumptions())+" kWh");
            System.out.println(AbstractPage.DECIMAL_00_FORMAT.format(energyConsumption.getTotalOfPrices())+" €");
            OffPeakHourPeriodEfficiency offPeakHourPeriodEfficiency=calculateOffPeakHourPeriodEfficiency(day,2,2020);
            System.out.println(AbstractPage.DECIMAL_0_FORMAT.format(offPeakHourPeriodEfficiency.getConsumptionEfficiency())+" %");
            System.out.println(AbstractPage.DECIMAL_0_FORMAT.format(offPeakHourPeriodEfficiency.getPriceEfficiency())+" %");
            PricingPeriod pricingPeriod=offPeakHourPeriodEfficiency.getPricingPeriod();
            double savedMoney;
            if(pricingPeriod.isBlueDay())
                savedMoney=offPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.BLUE_DAY_PEAK_HOUR.getPrice()-PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR.getPrice());
            else
                if(pricingPeriod.isWhiteDay())
                    savedMoney=offPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.WHITE_DAY_PEAK_HOUR.getPrice()-PricingPeriod.WHITE_DAY_OFF_PEAK_HOUR.getPrice());
                else
                    savedMoney=offPeakHourPeriodEfficiency.getOffPeakConsumption()*(PricingPeriod.RED_DAY_PEAK_HOUR.getPrice()-PricingPeriod.RED_DAY_OFF_PEAK_HOUR.getPrice());
            System.out.println(AbstractPage.DECIMAL_00_FORMAT.format(savedMoney)+" €");
            System.out.println();
        }
    }

    public static void main2(String[] args)
    {
        long startTime=System.currentTimeMillis();
        //Lave-vaisselle 45-65 29/12/2018 1546099386831L 1546108347421L
        //Lave-linge 40 30/12/2018 1546193633099L 1546201293588L
        //Lave-vaisselle 70 01/01/2019 1546356684103L 1546364874626L
        //Lave-vaisselle 50 02/01/2019 1546479018996L 1546489909748L
        //Lave-linge 60 30&31/01/2019 1548904134106L 1548912624649L

        List<InstantaneousConsumption> list=getInstantaneousConsumptions(30,1,2019,31,1,2019);

//        for(InstantaneousConsumption instantaneousConsumption:list)
//            System.out.println(instantaneousConsumption.getTime()+" "+instantaneousConsumption.getConsumer7Consumption());
//        if(2==2)
//            return;

        List<InstantaneousConsumption> list2=new ArrayList<>();
        list.stream()
                .filter(instantaneousConsumption->instantaneousConsumption.getTime()>=1548904134106L)
                .filter(instantaneousConsumption->instantaneousConsumption.getTime()<=1548912624649L)
                .forEach(list2::add);
        try(DataOutputStream outputStream=new DataOutputStream(new FileOutputStream(new File("profile.profile"))))
        {
            outputStream.writeInt(list2.size());
            for(InstantaneousConsumption instantaneousConsumption:list2)
            {
                outputStream.writeLong(instantaneousConsumption.getTime());
                outputStream.writeInt(instantaneousConsumption.getConsumer7Consumption());
                System.out.println(instantaneousConsumption.getTime()+" "+instantaneousConsumption.getConsumer7Consumption());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        ConsumptionProfileCalculator consumptionProfile=ConsumptionProfileCalculator.construct(list2);
        List<PricingPeriodZone> pricingPeriodZones=new ArrayList<>();
        pricingPeriodZones.add(new PricingPeriodZone(0,0,6,2,PricingPeriod.RED_DAY_OFF_PEAK_HOUR));
        pricingPeriodZones.add(new PricingPeriodZone(6,2,7,32,PricingPeriod.BLUE_DAY_OFF_PEAK_HOUR));
        pricingPeriodZones.add(new PricingPeriodZone(7,32,20,00,PricingPeriod.BLUE_DAY_PEAK_HOUR));
        BufferedImage image=new BufferedImage(840,900,BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2d=image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,840,900);
        for(int hour=2;hour<=8;hour++)
        {
            g2d.setColor(Color.BLACK);
            g2d.drawLine((hour-2)*60*2,0,(hour-2)*60*2,900);
            g2d.setColor(Color.RED);
            g2d.drawString(""+hour,(hour-2)*60*2,15);
            g2d.setColor(Color.BLACK);
            for(int minute=0;minute<=59;minute++)
            {
                if(minute>0&&minute%10==0)
                {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawLine((hour-2)*60*2+minute*2,0,(hour-2)*60*2+minute*2,900);
                    g2d.drawLine((hour-2)*60*2+minute*2+1,0,(hour-2)*60*2+minute*2+1,900);
                    g2d.setColor(Color.BLACK);
                }
                double price=consumptionProfile.getTotalPricing(hour,minute,pricingPeriodZones);
                System.out.println(hour+":"+minute+" "+price);
                g2d.drawLine((hour-2)*60*2+minute*2,900,(hour-2)*60*2+minute*2,900-(int)(price/.55d*900d));
                g2d.drawLine((hour-2)*60*2+minute*2+1,900,(hour-2)*60*2+minute*2+1,900-(int)(price/.55d*900d));
            }
        }
        g2d.dispose();
        try
        {
            ImageIO.write(image,"png",new File("price.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Total "+(System.currentTimeMillis()-startTime)+" ms");
    }
}
