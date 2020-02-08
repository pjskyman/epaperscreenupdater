package sky.epaperscreenupdater;

import sky.epaperscreenupdater.page.MainMenuPage;
import sky.program.Duration;

public final class Main
{
    private Main()
    {
    }

    public static void main(String[] args)
    {
        Logger.LOGGER.info("Starting "+Main.class.getSimpleName()+"...");
        try
        {
            MainMenuPage mainMenuPage=new MainMenuPage();
            Pixels currentPixels=mainMenuPage.potentiallyUpdate().getPixels();
            long lastCompleteRefresh=System.currentTimeMillis();
            EpaperScreenManager.displayPage(currentPixels,RefreshType.TOTAL_REFRESH);
            Logger.LOGGER.info("Display content successfully updated from page \""+mainMenuPage.getActivePageName()+"\" ("+RefreshType.TOTAL_REFRESH.toString()+")");
            RotaryEncoderManager.addRotationListener(mainMenuPage::rotated);
            RotaryEncoderManager.addSwitchListener(()->mainMenuPage.clicked(false));
            Logger.LOGGER.info(Main.class.getSimpleName()+" is now ready!");
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
//                    Logger.LOGGER.info("Getting new pixels from page \""+mainMenuPage.getActivePageName()+"\"");
                    Pixels newPixels=mainMenuPage.getPixels();
//                    Logger.LOGGER.info("New pixels successfully got from page \""+mainMenuPage.getActivePageName()+"\"");
                    if(newPixels!=currentPixels)
                    {
                        long now=System.currentTimeMillis();
                        RefreshType realRefreshType=newPixels.getRefreshType();
                        if(now-lastCompleteRefresh>Duration.of(10).minute())
                        {
                            realRefreshType=realRefreshType.combine(RefreshType.TOTAL_REFRESH);
                            lastCompleteRefresh=now;
                        }
                        Logger.LOGGER.info("Updating display content from page \""+mainMenuPage.getActivePageName()+"\" ("+realRefreshType.toString()+")");
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
            Logger.LOGGER.error("Unknown error");
            e.printStackTrace();
        }
    }
}
