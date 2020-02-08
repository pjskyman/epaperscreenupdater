package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class WasherSupervisionPage extends AbstractSinglePage
{
    public WasherSupervisionPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Surveillance LV/LL";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(7).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(18f);
        g2d.setFont(baseFont);
        long baseTime=System.currentTimeMillis();
        List<WasherInstantaneousConsumption> consumptions=ElectricityUtils.loadWasherInstantaneousConsumptions(baseTime-Duration.of(1).day(),baseTime+Duration.of(10).second());
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
//        int clockStringWidth=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getWidth());
//        int clockStringHeight=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getHeight());
        g2d.drawString(message1,2,18);
        g2d.drawString(message2,2,36);
    }

    protected String getDebugImageFileName()
    {
        return "washer_supervision.png";
    }

    public static void main(String[] args)
    {
        new WasherSupervisionPage(null).potentiallyUpdate();
    }
}
