package sky.epaperscreenupdater;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sky.program.Duration;

public class AnalogClockPage extends AbstractSinglePage
{
    public AnalogClockPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Horloge analogique";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH_IN_FAST_MODE;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).secondMinus(50).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        g2d.drawOval(148-64,0,127,127);
        g2d.fillOval(148-5,64-5,9,9);
        GregorianCalendar calendar=new GregorianCalendar();
        double second=(double)calendar.get(Calendar.SECOND);
        double minute=((double)calendar.get(Calendar.MINUTE)+second/60d);
        double hour=((double)calendar.get(Calendar.HOUR_OF_DAY)+minute/60d);
        g2d.setStroke(new BasicStroke(3f));
        g2d.draw(new Line2D.Double(148d-Math.sin(hour*Math.PI/6d)*14d,64d+Math.cos(hour*Math.PI/6d)*14d,148d+Math.sin(hour*Math.PI/6d)*39d,64d-Math.cos(hour*Math.PI/6d)*39d));
        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(new Line2D.Double(148d-Math.sin(minute*Math.PI/30d)*14d,64d+Math.cos(minute*Math.PI/30d)*14d,148d+Math.sin(minute*Math.PI/30d)*56d,64d-Math.cos(minute*Math.PI/30d)*56d));
        g2d.setStroke(new BasicStroke(1f));
        g2d.draw(new Line2D.Double(148d-Math.sin(second*Math.PI/30d)*22d,64d+Math.cos(second*Math.PI/30d)*22d,148d+Math.sin(second*Math.PI/30d)*58d,64d-Math.cos(second*Math.PI/30d)*58d));
        for(int i=0;i<60;i++)
            g2d.draw(new Line2D.Double(148d+Math.sin((double)i*Math.PI/30d)*63d,64d-Math.cos((double)i*Math.PI/30d)*63d,148d+Math.sin((double)i*Math.PI/30d)*(i%5==0?52d:59d),64d-Math.cos((double)i*Math.PI/30d)*(i%5==0?52d:59d)));
    }

    protected String getDebugImageFileName()
    {
        return "analogclock.png";
    }

    public static void main(String[] args)
    {
        new AnalogClockPage(null).potentiallyUpdate();
    }
}
