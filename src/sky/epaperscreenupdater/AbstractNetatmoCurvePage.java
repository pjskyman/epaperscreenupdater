package sky.epaperscreenupdater;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
    protected static final double[] TICK_OFFSETS=
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

                drawChart(measureMap,baseFont,verticalBaseFont,g2d);

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

    protected void drawChart(Map<String,Measure[]> measureMap,Font baseFont,Font verticalBaseFont,Graphics2D g2d)
    {
        Measure[] rawMeasures=measureMap.get(getMeasureMapKey());
        Measure[] measures=HomeWeatherVariationPage.filterTimedWindowMeasures(rawMeasures,3);
        if(measures!=null)
        {
            String ordinateLabelText=getOrdinateLabelText();
            int ordinateLabelTextWidth=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getWidth());
            int ordinateLabelTextHeight=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getHeight());
            g2d.setFont(verticalBaseFont);
            g2d.drawString(ordinateLabelText,ordinateLabelTextHeight-5,(128-ordinateLabelTextHeight+3)/2+ordinateLabelTextWidth/2);
            XRange xRange=computeXRange(measures);
            YRange yRange=computeYRange(measures);
            double choosenTickOffset=0d;
            for(int index=0;index<TICK_OFFSETS.length;index++)
            {
                choosenTickOffset=TICK_OFFSETS[index];
                if((128d-(double)ordinateLabelTextHeight+3d)*choosenTickOffset/yRange.getAmplitude()>=14d)
                    break;
            }
            int unitMin=(int)Math.ceil(yRange.getMin()/choosenTickOffset);
            int unitMax=(int)Math.floor(yRange.getMax()/choosenTickOffset);
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
            List<Point2D> measurePoints=new ArrayList<>(measures.length);
            for(Measure measure:measures)
            {
                long time=measure.getDate().getTime();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=measure.getValue();
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                measurePoints.add(new Point2D.Double(x,y));
            }
            g2d.drawLine(ordinateLabelTextHeight+maxOrdinateWidth,0,ordinateLabelTextHeight+maxOrdinateWidth,128-ordinateLabelTextHeight+3);
            g2d.drawLine(ordinateLabelTextHeight+maxOrdinateWidth,128-ordinateLabelTextHeight+3,295,128-ordinateLabelTextHeight+3);
            g2d.setFont(baseFont);
            g2d.setStroke(new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,1f,new float[]{1f,4f},0f));
            for(PreparedTick preparedOrdinateTick:preparedOrdinateTicks)
            {
                double value=preparedOrdinateTick.getValue();
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                g2d.drawString(preparedOrdinateTick.getName(),ordinateLabelTextHeight+maxOrdinateWidth-(int)Math.ceil(preparedOrdinateTick.getNameDimensions().getWidth())-2,(int)y+(int)Math.ceil(preparedOrdinateTick.getNameDimensions().getHeight()/2d)-3);
                g2d.drawLine(ordinateLabelTextHeight+maxOrdinateWidth-1,(int)y,296,(int)y);
            }
            for(int i=measures.length-1;i>=0;i-=6)
            {
                double x=measurePoints.get(i).getX();
                String timeString=SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(measures[i].getDate());
                int timeStringWidth=(int)Math.ceil(baseFont.getStringBounds(timeString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(timeString,(int)x-(i==measures.length-1?timeStringWidth-10:timeStringWidth/2),128);
                g2d.drawLine((int)x,128-ordinateLabelTextHeight+5,(int)x,0);
            }
            g2d.setStroke(new BasicStroke());
            drawData(g2d,measurePoints,ordinateLabelTextHeight);
        }
    }

    protected XRange computeXRange(Measure[] measures)
    {
        long xMin=measures[0].getDate().getTime();
        long xMax=measures[measures.length-1].getDate().getTime();
        return new XRange(xMin,xMax);
    }

    protected YRange computeYRange(Measure[] measures)
    {
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
        if(yAmplitude<0d)
        {
            yMin=0d;
            yMax=1d;
            yAmplitude=1d;
        }
        else
            if(yAmplitude<getMinimalYRange())
            {
                double offset=(getMinimalYRange()-yAmplitude)/2d;
                yMin-=offset;
                yMax+=offset;
                yAmplitude=yMax-yMin;
            }
        if(yMin<getMinimalY())
        {
            double offset=getMinimalY()-yMin;
            yMin+=offset;
            yMax+=offset;
        }
        yMin-=yAmplitude/15d;
        yMax+=yAmplitude/15d;
        return new YRange(yMin,yMax);
    }

    protected abstract double getMinimalYRange();

    protected abstract double getMinimalY();

    protected void drawData(Graphics2D g2d,List<Point2D> measurePoints,int ordinateLabelTextHeight)
    {
        for(int i=0;i<measurePoints.size();i++)
        {
            double x=measurePoints.get(i).getX();
            double y=measurePoints.get(i).getY();
            g2d.drawOval((int)x-2,(int)y-2,4,4);
        }
        if(measurePoints.size()==2)
        {
            double x1=measurePoints.get(0).getX();
            double y1=measurePoints.get(0).getY();
            double x2=measurePoints.get(1).getX();
            double y2=measurePoints.get(1).getY();
            g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
        }
        else//construction de la spline
        {
            Path2D path=new Path2D.Double();
            int np=measurePoints.size(); // number of points
            double[] d=new double[np]; // Newton form coefficients
            double[] x=new double[np]; // x-coordinates of nodes
            double y;
            double t;
            double oldy=0d;
            double oldt=0d;

            double[] a=new double[np];
            double t1;
            double t2;
            double[] h=new double[np];

            for(int i=0;i<np;i++)
            {
                x[i]=measurePoints.get(i).getX();
                d[i]=measurePoints.get(i).getY();
            }

            for(int i=1;i<=np-1;i++)
                h[i]=x[i]-x[i-1];
            double[] sub=new double[np-1];
            double[] diag=new double[np-1];
            double[] sup=new double[np-1];

            for(int i=1;i<=np-2;i++)
            {
                diag[i]=(h[i]+h[i+1])/3d;
                sup[i]=h[i+1]/6d;
                sub[i]=h[i]/6d;
                a[i]=(d[i+1]-d[i])/h[i+1]-(d[i]-d[i-1])/h[i];
            }
            solveTridiag(sub,diag,sup,a,np-2);

            // note that a[0]=a[np-1]=0
            // draw
            oldt=x[0];
            oldy=d[0];
            int precision=5;
            path.moveTo((int)oldt,(int)oldy);
            for(int i=1;i<=np-1;i++)
            {
                // loop over intervals between nodes
                for(int j=1;j<=precision;j++)
                {
                    t1=(h[i]*j)/precision;
                    t2=h[i]-t1;
                    y=((-a[i-1]/6d*(t2+h[i])*t1+d[i-1])*t2+(-a[i]/6d*(t1+h[i])*t2+d[i])*t1)/h[i];
                    t=x[i-1]+t1;
                    path.lineTo((int)t,(int)y);
                    oldt=t;
                    oldy=y;
                }
            }
            g2d.draw(path);
        }
    }

    private static void solveTridiag(double[] sub,double[] diag,double[] sup,double[] b,int n)
    {
/*      solve linear system with tridiagonal n by n matrix a
        using Gaussian elimination *without* pivoting
        where   a(i,i-1) = sub[i]  for 2<=i<=n
        a(i,i)   = diag[i] for 1<=i<=n
        a(i,i+1) = sup[i]  for 1<=i<=n-1
        (the values sub[1], sup[n] are ignored)
        right hand side vector b[1:n] is overwritten with solution
        NOTE: 1...n is used in all arrays, 0 is unused */
        int i;
/*                  factorization and forward substitution */
        for(i=2;i<=n;i++)
        {
            sub[i]=sub[i]/diag[i-1];
            diag[i]=diag[i]-sub[i]*sup[i-1];
            b[i]=b[i]-sub[i]*b[i-1];
        }
        b[n]=b[n]/diag[n];
        for(i=n-1;i>=1;i--)
            b[i]=(b[i]-sup[i]*b[i+1])/diag[i];
    }

    protected abstract long getRefreshDelay();

    protected abstract String getMeasureMapKey();

    protected abstract String getOrdinateLabelText();

    protected abstract String getVerificationFileName();

    protected static class PreparedTick
    {
        private final double value;
        private final String name;
        private final Rectangle2D nameDimensions;
        private static final DecimalFormat DECIMAL_FORMAT=new DecimalFormat("0.########E00");

        protected PreparedTick(double value,Font font,FontRenderContext fontRenderContext)
        {
            value=Double.parseDouble(DECIMAL_FORMAT.format(value).replace(",","."));
            this.value=value;
            if(value==(double)(int)value)
                name=""+(int)value;
            else
                name=""+value;
            this.nameDimensions=font.getStringBounds(name,fontRenderContext);
        }

        protected double getValue()
        {
            return value;
        }

        protected String getName()
        {
            return name;
        }

        protected Rectangle2D getNameDimensions()
        {
            return nameDimensions;
        }
    }

    public static void main(String[] args)
    {
        AnemometreWindCurvePage.main(args);
    }
}
