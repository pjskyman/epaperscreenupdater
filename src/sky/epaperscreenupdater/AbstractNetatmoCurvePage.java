package sky.epaperscreenupdater;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sky.netatmo.Measure;

public abstract class AbstractNetatmoCurvePage extends AbstractNetatmoPage
{
    private long lastRefreshTime;
    private static final double[] TICK_OFFSETS=
    {
        1e-10d,
        2e-10d,
        5e-10d,
        1e-9d,
        2e-9d,
        5e-9d,
        1e-8d,
        2e-8d,
        5e-8d,
        1e-7d,
        2e-7d,
        5e-7d,
        1e-6d,
        2e-6d,
        5e-6d,
        1e-5d,
        2e-5d,
        5e-5d,
        1e-4d,
        2e-4d,
        5e-4d,
        1e-3d,
        2e-3d,
        5e-3d,
        1e-2d,
        2e-2d,
        5e-2d,
        1e-1d,
        2e-1d,
        5e-1d,
        1d,
        2d,
        5d,
        1e1d,
        2e1d,
        5e1d,
        1e2d,
        2e2d,
        5e2d,
        1e3d,
        2e3d,
        5e3d,
        1e4d,
        2e4d,
        5e4d,
        1e5d,
        2e5d,
        5e5d,
        1e6d,
        2e6d,
        5e6d,
        1e7d,
        2e7d,
        5e7d,
        1e8d,
        2e8d,
        5e8d,
        1e9d,
        2e9d,
        5e9d,
        1e10d,
        2e10d,
        5e10d,
    };

    protected AbstractNetatmoCurvePage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>getRefreshDelay())
        {
            lastRefreshTime=now;
            Map<String,Measure[]> measureMap=getLastMeasures();
            Measure[] rawMeasures=measureMap.get(getMeasureMapKey());
            Measure[] measures=HomeWeatherVariationPage.filterTimedWindowMeasures(rawMeasures,2);
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);

                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(11f);
                g2d.setFont(baseFont);
                Font verticalBaseFont=baseFont.deriveFont(AffineTransform.getQuadrantRotateInstance(-1));

                if(measures!=null)
                {
                    String ordinateLabelText=getOrdinateLabelText();
                    int ordinateLabelTextWidth=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getWidth());
                    int ordinateLabelTextHeight=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getHeight());
                    g2d.setFont(verticalBaseFont);
                    g2d.drawString(ordinateLabelText,ordinateLabelTextHeight-5,(128-ordinateLabelTextHeight+3)/2+ordinateLabelTextWidth/2);
                    double yMin=1e10d;
                    double yMax=-1e10d;
                    for(Measure measure:measures)
                    {
                        if(measure.getValue()<yMin)
                            yMin=measure.getValue();
                        if(measure.getValue()>yMax)
                            yMax=measure.getValue();
                    }
                    double yAmplitude=yMax-yMin;
                    if(yAmplitude<=0d)
                    {
                        if(yMin==1e10d)
                            yMin=0d;
                        yAmplitude=1d;
                        yMax=yMin+1d;
                    }
                    double choosenTickOffset=0d;
                    for(int index=0;index<TICK_OFFSETS.length;index++)
                    {
                        choosenTickOffset=TICK_OFFSETS[index];
                        if((128d-(double)ordinateLabelTextHeight+3d)*choosenTickOffset/yAmplitude>=20d)//ordinateLabelTextHeight représente aussi la hauteur des futurs libellés en abscisse, donc on peut l'utiliser
                            break;
                    }
                    int unitMin=(int)Math.ceil(yMin/choosenTickOffset);
                    int unitMax=(int)Math.floor(yMax/choosenTickOffset);
                    double[] ticks=new double[(unitMax-unitMin)+1];
                    for(int i=unitMin;i<=unitMax;i++)
                        ticks[i-unitMin]=choosenTickOffset*(double)i;
                    List<PreparedTick> preparedOrdinateTicks=new ArrayList<>();
                    for(double tick:ticks)
                        preparedOrdinateTicks.add(new PreparedTick(tick,g2d.getFont(),g2d.getFontRenderContext()));
                    int maxOrdinateWidth=0;
                    for(PreparedTick preparedOrdinateTick:preparedOrdinateTicks)
                        if((int)Math.ceil(preparedOrdinateTick.getNameDimensions().getWidth())>maxOrdinateWidth)
                            maxOrdinateWidth=(int)Math.ceil(preparedOrdinateTick.getNameDimensions().getWidth());
                    g2d.drawLine(ordinateLabelTextHeight+maxOrdinateWidth,0,ordinateLabelTextHeight+maxOrdinateWidth,128-ordinateLabelTextHeight+3);//ordinateLabelTextHeight représente aussi la hauteur des futurs libellés en abscisse, donc on peut l'utiliser
                    g2d.drawLine(ordinateLabelTextHeight+maxOrdinateWidth,128-ordinateLabelTextHeight+3,296,128-ordinateLabelTextHeight+3);
                    g2d.setFont(baseFont);
                    g2d.setStroke(new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,1f,new float[]{1f,4f},0f));
                    for(PreparedTick preparedOrdinateTick:preparedOrdinateTicks)
                    {
                        double value=preparedOrdinateTick.getValue();
                        int y=(int)((128d-(double)ordinateLabelTextHeight+2d)*(1d-(value-yMin)/yAmplitude))+1;
                        g2d.drawString(preparedOrdinateTick.getName(),ordinateLabelTextHeight+maxOrdinateWidth-(int)Math.ceil(preparedOrdinateTick.getNameDimensions().getWidth())-2,y+(int)Math.ceil(preparedOrdinateTick.getNameDimensions().getHeight()/2d)-3);
                        g2d.drawLine(ordinateLabelTextHeight+maxOrdinateWidth-1,y,296,y);
                    }
                    for(int i=measures.length-1;i>=0;i-=5)
                    {
                        long time=measures[i].getDate().getTime();
                        int x=(int)((296d-(double)ordinateLabelTextHeight-(double)maxOrdinateWidth+9d)*(1d-(double)((measures[measures.length-1].getDate().getTime()-time)/1000L)/7200d)+(double)ordinateLabelTextHeight+(double)maxOrdinateWidth-11d);
                        String timeString=SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(measures[i].getDate());
                        int timeStringWidth=(int)Math.ceil(baseFont.getStringBounds(timeString,g2d.getFontRenderContext()).getWidth());
                        g2d.drawString(timeString,x-(i==measures.length-1?timeStringWidth-2:timeStringWidth/2),128);
                        g2d.drawLine(x,128-ordinateLabelTextHeight+5,x,0);
                    }
                    g2d.setStroke(new BasicStroke());
                    for(int i=1;i<measures.length;i++)
                    {
                        Measure measure1=measures[i-1];
                        long time1=measure1.getDate().getTime();
                        int x1=(int)((296d-(double)ordinateLabelTextHeight-(double)maxOrdinateWidth+9d)*(1d-(double)((measures[measures.length-1].getDate().getTime()-time1)/1000L)/7200d)+(double)ordinateLabelTextHeight+(double)maxOrdinateWidth-11d);
                        double value1=measure1.getValue();
                        int y1=(int)((128d-(double)ordinateLabelTextHeight+2d)*(1d-(value1-yMin)/yAmplitude))+1;
                        Measure measure2=measures[i];
                        long time2=measure2.getDate().getTime();
                        int x2=(int)((296d-(double)ordinateLabelTextHeight-(double)maxOrdinateWidth+9d)*(1d-(double)((measures[measures.length-1].getDate().getTime()-time2)/1000L)/7200d)+(double)ordinateLabelTextHeight+(double)maxOrdinateWidth-11d);
                        double value2=measure2.getValue();
                        int y2=(int)((128d-(double)ordinateLabelTextHeight+2d)*(1d-(value2-yMin)/yAmplitude))+1;
                        g2d.drawLine(x1,y1,x2,y2);
                        g2d.drawOval(x1-2,y1-2,4,4);
                        if(i==measures.length-1)
                            g2d.drawOval(x2-2,y2-2,4,4);
                    }
                }

                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File(getVerificationFileName())))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH).writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    protected abstract long getRefreshDelay();

    protected abstract String getMeasureMapKey();

    protected abstract String getOrdinateLabelText();

    protected abstract String getVerificationFileName();

    private static class PreparedTick
    {
        private final double value;
        private final String name;
        private final Rectangle2D nameDimensions;
        private static final DecimalFormat DECIMAL_FORMAT=new DecimalFormat("0.########E00");

        private PreparedTick(double value,Font font,FontRenderContext fontRenderContext)
        {
            value=Double.parseDouble(DECIMAL_FORMAT.format(value).replace(",","."));
            this.value=value;
            if(value==(double)(int)value)
                name=""+(int)value;
            else
                name=""+value;
            this.nameDimensions=font.getStringBounds(name,fontRenderContext);
        }

        private double getValue()
        {
            return value;
        }

        private String getName()
        {
            return name;
        }

        private Rectangle2D getNameDimensions()
        {
            return nameDimensions;
        }
    }

    public static void main(String[] args)
    {
        JardinTemperatureCurvePage.main(args);
    }
}
