package sky.epaperscreenupdater;

import java.util.concurrent.atomic.AtomicInteger;
import sky.epaperscreenupdater.page.AnniversaryPage;
import sky.epaperscreenupdater.page.MainMenuPage;
import sky.housecommon.Logger;
import sky.program.Duration;

public final class EpaperScreenUpdater
{
    private EpaperScreenUpdater()
    {
    }

    public static void main(String[] args)
    {
        Logger.LOGGER.info("Starting "+EpaperScreenUpdater.class.getSimpleName()+"...");
        try
        {
            LedManager.setGreenLedOn();
            LedManager.setRedLedOn();
            Thread.sleep(1000L);
            LedManager.setGreenLedOff();
            LedManager.setRedLedOff();
            Thread.sleep(200L);
            LedManager.setGreenLedOn();
            LedManager.setRedLedOn();
            Thread.sleep(200L);
            LedManager.setGreenLedOff();
            LedManager.setRedLedOff();
            Thread.sleep(200L);
            LedManager.setGreenLedOn();
            LedManager.setRedLedOn();
            Thread.sleep(200L);
            LedManager.setGreenLedOff();
            LedManager.setRedLedOff();
            Thread.sleep(200L);
            LedManager.setGreenLedOn();
            LedManager.setRedLedOn();
            Thread.sleep(200L);
            LedManager.setGreenLedOff();
            LedManager.setRedLedOff();
            MainMenuPage mainMenuPage=new MainMenuPage();
            Screen currentScreen=mainMenuPage.potentiallyUpdate().getScreen();
            AtomicInteger currentModificationCount=new AtomicInteger(currentScreen.getModificationCount());
            long lastCompleteRefresh=System.currentTimeMillis();
            EpaperScreenManager.display(currentScreen,RefreshType.TOTAL_REFRESH);
            Logger.LOGGER.info("Display content successfully updated from page \""+mainMenuPage.getActivePageName()+"\" ("+RefreshType.TOTAL_REFRESH.toString()+")");
            RotaryEncoderManager.addRotationListener(mainMenuPage::rotated);
            RotaryEncoderManager.addSwitchListener(()->mainMenuPage.clicked(false));
            RotaryEncoderManager.addRotationListener(rotationDirection->
            {
                try
                {
                    if(rotationDirection==RotationDirection.CLOCKWISE)
                    {
                        LedManager.setGreenLedOn();
                        Thread.sleep(200L);
                        LedManager.setGreenLedOff();
                    }
                    else
                    {
                        LedManager.setGreenLedOn();
                        Thread.sleep(50L);
                        LedManager.setGreenLedOff();
                        Thread.sleep(50L);
                        LedManager.setGreenLedOn();
                        Thread.sleep(50L);
                        LedManager.setGreenLedOff();
                    }
                }
                catch(InterruptedException e)
                {
                }
            });
            RotaryEncoderManager.addSwitchListener(()->
            {
                try
                {
                    LedManager.setGreenLedOn();
                    Thread.sleep(500L);
                    LedManager.setGreenLedOff();
                }
                catch(InterruptedException e)
                {
                }
            });
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
                                t.printStackTrace();
                            }
                            Thread.sleep(Duration.of(207).millisecond());
                        }
                    }
                    catch(InterruptedException e)
                    {
                    }
                }
            }.start();
            new Thread("anniversaryNotifier")
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(Duration.of(30).second());
                        while(true)
                        {
                            boolean anniversaryToday=false;
                            try
                            {
                                anniversaryToday=AnniversaryPage.isAnniversaryToday(AnniversaryPage.getAnniversaries());
                            }
                            catch(Throwable t)
                            {
                                Logger.LOGGER.error("Unmanaged error during anniversary notifying ("+t.toString()+")");
                                t.printStackTrace();
                            }
                            if(anniversaryToday)
                            {
                                LedManager.setGreenLedOn();
                                Thread.sleep(200L);
                                LedManager.setGreenLedOff();
                                Thread.sleep(50L);
                                LedManager.setGreenLedOn();
                                Thread.sleep(200L);
                                LedManager.setGreenLedOff();
                                Thread.sleep(50L);
                                LedManager.setGreenLedOn();
                                Thread.sleep(200L);
                                LedManager.setGreenLedOff();
                                Thread.sleep(50L);
                                LedManager.setGreenLedOn();
                                Thread.sleep(200L);
                                LedManager.setGreenLedOff();
                            }
                            Thread.sleep(Duration.of(15).second());
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
//                    Logger.LOGGER.info("Getting new pixels from page \""+mainMenuPage.getActivePageName()+"\"");
                    Screen newScreen=mainMenuPage.getScreen();
                    int newModificationCount=newScreen.getModificationCount();
//                    Logger.LOGGER.info("New pixels successfully got from page \""+mainMenuPage.getActivePageName()+"\"");
                    if(newScreen!=currentScreen||newModificationCount!=currentModificationCount.get())
                    {
                        long now=System.currentTimeMillis();
                        RefreshType realRefreshType=RefreshType.PARTIAL_REFRESH;
                        if(now-lastCompleteRefresh>Duration.of(10).minute())
                        {
                            realRefreshType=realRefreshType.combine(RefreshType.TOTAL_REFRESH);
                            lastCompleteRefresh=now;
                        }
                        Logger.LOGGER.info("Updating display content from page \""+mainMenuPage.getActivePageName()+"\" ("+realRefreshType.toString()+")");
                        EpaperScreenManager.display(newScreen,realRefreshType);
                        Logger.LOGGER.info("Display content successfully updated from page \""+mainMenuPage.getActivePageName()+"\" ("+realRefreshType.toString()+")");
                        currentScreen=newScreen;
                        currentModificationCount.set(newModificationCount);
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
            Logger.LOGGER.error("Unknown error");
            e.printStackTrace();
        }
    }
}
