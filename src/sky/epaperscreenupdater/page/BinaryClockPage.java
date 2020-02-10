package sky.epaperscreenupdater.page;

import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import sky.program.Duration;

public class BinaryClockPage extends AbstractSinglePage
{
    public BinaryClockPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Horloge binaire";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).secondMinus(100).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Date date=new Date();
        String hour=new SimpleDateFormat("HH").format(date);
        String minute=new SimpleDateFormat("mm").format(date);
        String second=new SimpleDateFormat("ss").format(date);
        for(int i=1;i<=4;i++)
            if((Integer.parseInt(hour.substring(0,1))/(1<<i-1))%2==1)
                g2d.fillRect(53,129-32*i,30,30);
        for(int i=1;i<=4;i++)
            if((Integer.parseInt(hour.substring(1,2))/(1<<i-1))%2==1)
                g2d.fillRect(85,129-32*i,30,30);
        for(int i=1;i<=4;i++)
            if((Integer.parseInt(minute.substring(0,1))/(1<<i-1))%2==1)
                g2d.fillRect(117,129-32*i,30,30);
        for(int i=1;i<=4;i++)
            if((Integer.parseInt(minute.substring(1,2))/(1<<i-1))%2==1)
                g2d.fillRect(149,129-32*i,30,30);
        for(int i=1;i<=4;i++)
            if((Integer.parseInt(second.substring(0,1))/(1<<i-1))%2==1)
                g2d.fillRect(181,129-32*i,30,30);
        for(int i=1;i<=4;i++)
            if((Integer.parseInt(second.substring(1,2))/(1<<i-1))%2==1)
                g2d.fillRect(213,129-32*i,30,30);
    }

    protected String getDebugImageFileName()
    {
        return "binaryclock.png";
    }

    public static void main(String[] args)
    {
        new BinaryClockPage(null).potentiallyUpdate();
    }
}
