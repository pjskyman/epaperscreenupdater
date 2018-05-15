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
            MainMenuPage mainMenuPage=new MainMenuPage();
            mainMenuPage.potentiallyUpdate();
            Pixels currentPixels=mainMenuPage.potentiallyUpdate().getPixels();
            long lastCompleteRefresh=System.currentTimeMillis();
            EpaperScreenManager.displayPage(currentPixels,RefreshType.TOTAL_REFRESH);
            Logger.LOGGER.info("Display content successfully updated from page \""+mainMenuPage.getActivePageName()+"\" ("+RefreshType.TOTAL_REFRESH.toString()+")");
            RotaryEncoderManager.addRotationListener(rotationDirection->mainMenuPage.rotated(rotationDirection));
            RotaryEncoderManager.addSwitchListener(()->mainMenuPage.clicked(false));
            Logger.LOGGER.info(EpaperScreenUpdater.class.getSimpleName()+" is now ready!");
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
                                mainMenuPage.potentiallyUpdate();
                            }
                            catch(Throwable t)
                            {
                                Logger.LOGGER.error("Unmanaged error during refresh ("+t.toString()+")");
                            }
                            Thread.sleep(Duration.of(107).millisecond());
                        }
                    }
                    catch(InterruptedException e)
                    {
                    }
                }
            }.start();
            try
            {
                while(true)
                {
                    Pixels newPixels=mainMenuPage.getPixels();
                    if(newPixels!=currentPixels)
                    {
                        long now=System.currentTimeMillis();
                        RefreshType realRefreshType=newPixels.getRefreshType();
                        if(now-lastCompleteRefresh>Duration.of(10).minute())
                        {
                            realRefreshType=realRefreshType.combine(RefreshType.TOTAL_REFRESH);
                            lastCompleteRefresh=now;
                        }
                        EpaperScreenManager.displayPage(newPixels,realRefreshType);
                        Logger.LOGGER.info("Display content successfully updated from page \""+mainMenuPage.getActivePageName()+"\" ("+realRefreshType.toString()+")");
                        currentPixels=newPixels;
                    }
                    Thread.sleep(Duration.of(48).millisecond());
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
