package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import sky.program.Duration;

public class WasherSupervisionPage extends AbstractPage
{
    private long lastRefreshTime;

    public WasherSupervisionPage()
    {
        lastRefreshTime=0L;
    }

    public int getSerial()
    {
        return 19;
    }

    public String getName()
    {
        return "Surveillance L&V";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(1).minutePlus(7).second())
        {
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(18f);
                g2d.setFont(baseFont);
                long baseTime=System.currentTimeMillis();
                List<WasherInstantaneousConsumption> consumptions=loadWasherInstantaneousConsumptions(baseTime-Duration.of(1).day(),baseTime+Duration.of(10).second());
                //WasherInstantaneousConsumption lastNullConsumption=null;
                WasherInstantaneousConsumption lastLowConsumption=null;
                WasherInstantaneousConsumption lastWorkingConsumption=null;
                for(int i=consumptions.size()-1;i>=0;i--)
                {
                    WasherInstantaneousConsumption consumption=consumptions.get(i);
                    if(lastLowConsumption==null&&consumption.getConsumption()>0)
                        lastLowConsumption=consumption;
                    if(lastWorkingConsumption==null&&consumption.getConsumption()>=10)
                        lastWorkingConsumption=consumption;
                    if(lastLowConsumption!=null&&lastWorkingConsumption!=null)
                        break;
                }
                String message1;
                String message2;
                if(lastLowConsumption==null&&lastWorkingConsumption==null)
                {
                    message1="Pas de cycle lancé récemment";
                    message2="";
                }
                else
                    if(lastWorkingConsumption!=null&&lastLowConsumption!=null)
                    {
                        long offset1=baseTime-lastWorkingConsumption.getTime();
                        long offset2=baseTime-lastLowConsumption.getTime();
                        if(offset1<Duration.of(1).minute())
                        {
                            message1="Cycle en cours";
                            message2="";
                        }
                        else
                        {
                            if(offset1<Duration.of(1).hour())
                                message1="Cycle terminé depuis "+offset1/60000L+" minute(s)";
                            else
                                message1="Cycle terminé depuis "+offset1/3600000L+" heure(s)";
                            if(offset2<Duration.of(1).minute())
                                message2="Machine encore allumée";
                            else
                                message2="";
                        }
                    }
                    else
                    {
                        message1="?";
                        message2="?";
                    }
//                int clockStringWidth=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getWidth());
//                int clockStringHeight=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getHeight());
                g2d.drawString(message1,2,18);
                g2d.drawString(message2,2,36);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("washer_supervision.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels().writeImage(sourceImage);
                Logger.LOGGER.info("Page "+getSerial()+" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    public Pixels getPixels()
    {
        return pixels;
    }

    public boolean hasHighFrequency()
    {
        return false;
    }

    public static void main(String[] args)
    {
        new WasherSupervisionPage().potentiallyUpdate();
    }

    public static List<WasherInstantaneousConsumption> loadWasherInstantaneousConsumptions(long startTimeRequested,long stopTimeRequested)
    {
        try
        {
            try(Connection connection=Database.getConnection())
            {
                long startTime=System.currentTimeMillis();
                List<WasherInstantaneousConsumption> washerInstantaneousConsumptions=new ArrayList<>();
                try(Statement statement=connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY))
                {
                    try(ResultSet resultSet=statement.executeQuery("SELECT time,pricingPeriod,consumer7Consumption as consumption FROM instantaneous_consumption WHERE time>="+startTimeRequested+" AND time<"+stopTimeRequested+" ORDER BY time ASC;"))
                    {
                        while(resultSet.next())
                        {
                            long time=resultSet.getLong("time");
                            PricingPeriod pricingPeriod=PricingPeriod.getPricingPeriodForCode(resultSet.getInt("pricingPeriod"));
                            int consumption=resultSet.getInt("consumption");
                            washerInstantaneousConsumptions.add(new WasherInstantaneousConsumption(time,pricingPeriod,consumption));
                        }
                    }
                }
//                Logger.LOGGER.info(washerInstantaneousConsumptions.size()+" rows fetched in "+(System.currentTimeMillis()-startTime)+" ms");
                return washerInstantaneousConsumptions;
            }
        }
        catch(NotAvailableDatabaseException|SQLException e)
        {
            Logger.LOGGER.error("Unable to parse the request response ("+e.toString()+")");
            return new ArrayList<>(0);
        }
    }
}
