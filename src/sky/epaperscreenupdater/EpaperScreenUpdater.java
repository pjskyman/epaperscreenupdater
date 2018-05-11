package sky.epaperscreenupdater;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sky.program.Duration;

public final class EpaperScreenUpdater
{
    private static int currentPage=0;
    private static int currentlySelectedPage=-1;
    public static final Font FREDOKA_ONE_FONT;

    static
    {
        Font font=null;
        try(FileInputStream inputStream=new FileInputStream(new File("FredokaOne-Regular.ttf")))
        {
            font=Font.createFont(Font.TRUETYPE_FONT,inputStream);
            Logger.LOGGER.info("Font loaded successfully");
        }
        catch(IOException|FontFormatException e)
        {
            Logger.LOGGER.error("Unable to load the font ("+e.toString()+")");
        }
        FREDOKA_ONE_FONT=font;
    }

    private EpaperScreenUpdater()
    {
    }

    public static void main(String[] args)
    {
        Logger.LOGGER.info("Starting "+EpaperScreenUpdater.class.getSimpleName()+"...");
        try
        {
            List<Page> pages=new ArrayList<>();
            pages.add(new InstantaneousConsumptionPage().potentiallyUpdate());
            pages.add(new HomeWeatherPage().potentiallyUpdate());
            pages.add(new HomeWeatherVariationPage().potentiallyUpdate());
            pages.add(new TempoCalendarPage().potentiallyUpdate());
            pages.add(new DigitalClockPage().potentiallyUpdate());
            pages.add(new AnalogClockPage().potentiallyUpdate());
            pages.add(new BinaryClockPage().potentiallyUpdate());
            pages.add(new RERCPage().potentiallyUpdate());
            pages.add(new DailyWeatherForecast1Page().potentiallyUpdate());
            pages.add(new DailyWeatherForecast2Page().potentiallyUpdate());
            pages.add(new HourlyWeatherForecast1Page().potentiallyUpdate());
            pages.add(new HourlyWeatherForecast2Page().potentiallyUpdate());
            pages.add(new HourlyWeatherForecast3Page().potentiallyUpdate());
            pages.add(new HourlyWeatherForecast4Page().potentiallyUpdate());
            pages.add(new EnergyConsumptionPage().potentiallyUpdate());
            pages.add(new TodayPricePage().potentiallyUpdate());
            pages.add(new MoonPage().potentiallyUpdate());
            pages.add(new AnniversaryPage().potentiallyUpdate());
            pages.add(new WasherSupervisionPage().potentiallyUpdate());
            pages.add(new AboutPage().potentiallyUpdate());
            pages.sort((o1,o2)->Integer.compare(o1.getSerial(),o2.getSerial()));//au cas où...
            Pixels currentPixels=pages.get(0).potentiallyUpdate().getPixels();
            long lastCompleteRefresh=System.currentTimeMillis();
            EpaperScreenManager.displayPage(currentPixels,false,false);
            Logger.LOGGER.info("Display content successfully updated from page "+pages.get(0).getSerial()+" (total refresh)");
            RotaryEncoderManager.addRotationListener(rotationDirection->currentlySelectedPage=((currentlySelectedPage==-1?currentPage:currentlySelectedPage)+(rotationDirection==RotationDirection.CLOCKWISE?1:-1)+pages.size())%pages.size());
            RotaryEncoderManager.addSwitchListener(()->
            {
                if(currentlySelectedPage==-1)
                    return;//TODO peut-être forcer un total refresh
                currentPage=currentlySelectedPage;
                currentlySelectedPage=-1;
            });
            Logger.LOGGER.info(EpaperScreenUpdater.class.getSimpleName()+" is ready!");
            new Thread("pageUpdater")
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(Duration.of(1).second());
                        while(true)
                        {
                            try
                            {
                                pages.forEach(page->page.potentiallyUpdate());
                            }
                            catch(Throwable t)
                            {
                                Logger.LOGGER.error("Unmanaged throwable during refresh ("+t.toString()+")");
                            }
                            Thread.sleep(Duration.of(100).millisecond());
                        }
                    }
                    catch(InterruptedException e)
                    {
                    }
                }
            }.start();
            try
            {
                int lastDrawnIncrust=currentlySelectedPage;
                while(true)
                {
                    int currentPageCopy=currentPage;
                    int currentlySelectedPageCopy=currentlySelectedPage;
                    Pixels newPixels=pages.get(currentPageCopy).getPixels();
                    if(newPixels!=currentPixels||currentlySelectedPageCopy!=lastDrawnIncrust)
                    {
                        long now=System.currentTimeMillis();
                        boolean partialRefresh=true;
                        if(currentlySelectedPageCopy==-1&&now-lastCompleteRefresh>Duration.of(10).minute())
                        {
                            partialRefresh=false;
                            lastCompleteRefresh=now;
                        }
                        Pixels pixels=currentlySelectedPageCopy==-1?newPixels:newPixels.incrustTransparentImage(new IncrustGenerator(pages.get(currentlySelectedPageCopy)).generateIncrust());
                        boolean fastMode=false;
                        EpaperScreenManager.displayPage(pixels,partialRefresh,fastMode);
                        Logger.LOGGER.info("Display content successfully updated from page "+pages.get(currentPageCopy).getSerial()+" ("+(partialRefresh?"partial":"total")+" refresh)");
                        currentPixels=newPixels;
                        lastDrawnIncrust=currentlySelectedPageCopy;
                    }
                    Thread.sleep(Duration.of(50).millisecond());
                }
            }
            catch(InterruptedException e)
            {
            }
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unknown error ("+e.toString()+")");
        }
    }

    public static List<InstantaneousConsumption> loadInstantaneousConsumptions(int number)
    {
        try
        {
            try(Connection connection=Database.getConnection())
            {
                long startTime=System.currentTimeMillis();
                List<InstantaneousConsumption> instantaneousConsumptions=new ArrayList<>();
                try(Statement statement=connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY))
                {
                    try(ResultSet resultSet=statement.executeQuery("SELECT * FROM instantaneous_consumption ORDER BY time DESC LIMIT "+number+";"))
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
                Collections.reverse(instantaneousConsumptions);
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
}
