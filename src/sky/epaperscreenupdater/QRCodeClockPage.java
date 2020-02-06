package sky.epaperscreenupdater;

import java.awt.Color;
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
    private long lastRefreshTime;

    public QRCodeClockPage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "Horloge QR code";
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(1).second())
        {
            Logger.LOGGER.info("Page \""+getName()+"\" needs to be updated");
            lastRefreshTime=now;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                byte[] binary=QRCode.from(SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM).format(new Date())).stream().toByteArray();
                BufferedImage image=ImageIO.read(new ByteArrayInputStream(binary));
                AffineTransform affineTransform=AffineTransform.getScaleInstance(1.5d,1.5d);
                affineTransform.concatenate(AffineTransform.getTranslateInstance(37d,-19.5d));
                g2d.drawImage(image,affineTransform,null);
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("qrcode.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH_IN_FAST_MODE).writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error when updating page \""+getName()+"\"");
                e.printStackTrace();
            }
        }
        return this;
    }

    public static void main(String[] args)
    {
        new QRCodeClockPage(null).potentiallyUpdate();
    }
}
