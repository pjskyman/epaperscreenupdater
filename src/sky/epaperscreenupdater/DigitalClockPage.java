package sky.epaperscreenupdater;

import java.awt.Font;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import sky.program.Duration;

public class DigitalClockPage extends AbstractSinglePage
{
    public DigitalClockPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Horloge numérique";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH_IN_FAST_MODE;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Font baseFont=Main.FREDOKA_ONE_FONT.deriveFont(70f);
        g2d.setFont(baseFont);
        String clockString=SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM).format(new Date());
        int clockStringWidth=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getWidth());
        int clockStringHeight=(int)Math.ceil(baseFont.getStringBounds(clockString,g2d.getFontRenderContext()).getHeight());
        g2d.drawString(clockString,148-clockStringWidth/2,45+clockStringHeight/2);
    }

    protected String getDebugImageFileName()
    {
        return "clock.png";
    }

    public static void main(String[] args)
    {
        new DigitalClockPage(null).potentiallyUpdate();
    }
}
