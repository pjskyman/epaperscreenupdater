package sky.epaperscreenupdater;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import net.glxn.qrgen.javase.QRCode;
import sky.program.Duration;

public class QRCodeClockPage extends AbstractSinglePage
{
    public QRCodeClockPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Horloge QR code";
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
        byte[] binary=QRCode.from(SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM).format(new Date())).stream().toByteArray();
        BufferedImage image=ImageIO.read(new ByteArrayInputStream(binary));
        AffineTransform affineTransform=AffineTransform.getScaleInstance(1.5d,1.5d);
        affineTransform.concatenate(AffineTransform.getTranslateInstance(37d,-19.5d));
        g2d.drawImage(image,affineTransform,null);
    }

    protected String getDebugImageFileName()
    {
        return "qrcode.png";
    }

    public static void main(String[] args)
    {
        new QRCodeClockPage(null).potentiallyUpdate();
    }
}
