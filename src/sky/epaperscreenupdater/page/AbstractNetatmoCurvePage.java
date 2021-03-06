package sky.epaperscreenupdater.page;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import sky.housecommon.Logger;
import sky.netatmo.Measure;
import sky.program.Duration;

public abstract class AbstractNetatmoCurvePage extends AbstractSinglePage
{
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
    public static final double STANDARD_WIND_MINIMAL_Y_RANGE=10d;
    public static final double STANDARD_WIND_MINIMAL_Y=0d;
    public static final double STANDARD_CARBON_DIOXYDE_MINIMAL_Y_RANGE=500d;
    public static final double STANDARD_CARBON_DIOXYDE_MINIMAL_Y=0d;
    public static final double STANDARD_HUMIDITY_MINIMAL_Y_RANGE=5d;
    public static final double STANDARD_HUMIDITY_MINIMAL_Y=0d;
    public static final double STANDARD_TEMPERATURE_MINIMAL_Y_RANGE=2.5d;
    public static final double STANDARD_TEMPERATURE_MINIMAL_Y=-100d;
    public static final double STANDARD_NOISE_MINIMAL_Y_RANGE=25d;
    public static final double STANDARD_NOISE_MINIMAL_Y=0d;
    public static final double STANDARD_PRESSURE_MINIMAL_Y_RANGE=2d;
    public static final double STANDARD_PRESSURE_MINIMAL_Y=0d;

    protected AbstractNetatmoCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Map<String,Measure[]> measureMap=NetatmoUtils.getLastMeasures();
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(11f);
        g2d.setFont(baseFont);
        Font verticalBaseFont=baseFont.deriveFont(AffineTransform.getQuadrantRotateInstance(-1));
        drawChart(measureMap,baseFont,verticalBaseFont,g2d);
    }

    protected void drawChart(Map<String,Measure[]> measureMap,Font baseFont,Font verticalBaseFont,Graphics2D g2d)
    {
        Measure[] rawMeasures=measureMap.get(getMeasureKind());
        Measure[] measures=NetatmoUtils.filterTimedWindowMeasures(rawMeasures,3);
        if(measures!=null)
        {
            Measure nowMeasure=NetatmoUtils.estimate(measures);
            Measure[] yesterdayMeasures=NetatmoUtils.getMeasureDatabase().getMeasures(getMeasureKind(),
                                                              measures[0].getDate().getTime()-Duration.of(1).day(),
                                                              (nowMeasure!=null?nowMeasure:measures[measures.length-1]).getDate().getTime()-Duration.of(1).day()
            );
            String ordinateLabelText=getOrdinateLabelText();
            int ordinateLabelTextWidth=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getWidth());
            int ordinateLabelTextHeight=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getHeight());
            g2d.setFont(verticalBaseFont);
            g2d.drawString(ordinateLabelText,ordinateLabelTextHeight-5,(128-ordinateLabelTextHeight+3)/2+ordinateLabelTextWidth/2);
            XRange xRange=computeXRange(measures,nowMeasure);
            YRange todayYRange=computeYRange(measures,nowMeasure);
            YRange yesterdayYRange=computeYRange(yesterdayMeasures);
            YRange yRange=yesterdayMeasures.length>0?new YRange(todayYRange,yesterdayYRange):todayYRange;
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
            Point2D nowMeasurePoint=null;
            if(nowMeasure!=null)
            {
                long time=nowMeasure.getDate().getTime();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=nowMeasure.getValue();
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                nowMeasurePoint=new Point2D.Double(x,y);
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
            for(int i=measures.length-(nowMeasure!=null?0:1);i>=0;i-=6)
            {
                double x=(i==measures.length?nowMeasurePoint:measurePoints.get(i)).getX();
                String timeString=SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format((i==measures.length?nowMeasure:measures[i]).getDate());
                int timeStringWidth=(int)Math.ceil(baseFont.getStringBounds(timeString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(timeString,(int)x-(i==measures.length-(nowMeasure!=null?0:1)?timeStringWidth-10:timeStringWidth/2),128);
                g2d.drawLine((int)x,128-ordinateLabelTextHeight+5,(int)x,0);
            }
            g2d.setStroke(new BasicStroke());
            drawData(g2d,measurePoints,ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.CONTINUOUS_LINE,CurvePointShape.CIRCLE);
            if(nowMeasure!=null)
                drawData(g2d,Arrays.asList(measurePoints.get(measurePoints.size()-1),nowMeasurePoint),ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.CONTINUOUS_LINE,CurvePointShape.NOTHING);
            List<Point2D> yesterdayMeasurePoints=new ArrayList<>(yesterdayMeasures.length);
            for(Measure yesterdayMeasure:yesterdayMeasures)
            {
                long time=yesterdayMeasure.getDate().getTime()+Duration.of(1).day();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=yesterdayMeasure.getValue();
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                yesterdayMeasurePoints.add(new Point2D.Double(x,y));
            }
            drawData(g2d,yesterdayMeasurePoints,ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.DOTTED_LINE,CurvePointShape.NOTHING);
        }
    }

    protected abstract String getMeasureKind();

    protected abstract String getOrdinateLabelText();

    protected XRange computeXRange(Measure[] measures,Measure nowMeasure)
    {
        long xMin=measures[0].getDate().getTime();
        long xMax=(nowMeasure!=null?nowMeasure:measures[measures.length-1]).getDate().getTime();
        return new XRange(xMin,xMax);
    }

    protected YRange computeYRange(Measure[] measures)
    {
        return computeYRange(measures,null);
    }

    protected YRange computeYRange(Measure[] measures,Measure nowMeasure)
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
        if(nowMeasure!=null)
        {
            if(nowMeasure.getValue()<yMin)
                yMin=nowMeasure.getValue();
            if(nowMeasure.getValue()>yMax)
                yMax=nowMeasure.getValue();
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

    protected void drawData(Graphics2D g2d,List<Point2D> measurePoints,int ordinateLabelTextHeight,CurveLineType curveLineType,CurveStrokeType curveStrokeType,CurvePointShape curvePointShape)
    {
        BasicStroke stroke=null;
        if(curveStrokeType==CurveStrokeType.DOTTED_LINE)
        {
            stroke=new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1f,new float[]{1f,1.5f},0f);
            g2d.setStroke(stroke);
        }
        else
            if(curveStrokeType==CurveStrokeType.DASHED_LINE)
            {
                stroke=new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1f,new float[]{5f,5f},0f);
                g2d.setStroke(stroke);
            }
        if(curveLineType==CurveLineType.LINE)
            for(int i=1;i<measurePoints.size();i++)
            {
                double x1=measurePoints.get(i-1).getX();
                double y1=measurePoints.get(i-1).getY();
                double x2=measurePoints.get(i).getX();
                double y2=measurePoints.get(i).getY();
                g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
                if(stroke!=null)
                {
                    stroke=new BasicStroke(stroke.getLineWidth(),stroke.getEndCap(),stroke.getLineJoin(),stroke.getMiterLimit(),stroke.getDashArray(),stroke.getDashPhase()+(float)Point2D.distance(x1,y1,x2,y2));
                    g2d.setStroke(stroke);
                }
            }
        else
            if(curveLineType==CurveLineType.SPLINE)
                if(measurePoints.size()==2)//pas de spline si deux points ou moins, et ligne uniquement si deux points, sinon rien
                {
                    double x1=measurePoints.get(0).getX();
                    double y1=measurePoints.get(0).getY();
                    double x2=measurePoints.get(1).getX();
                    double y2=measurePoints.get(1).getY();
                    g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
                }
                else
                    if(measurePoints.size()>2)//construction de la spline possible
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
                        int count=1;
                        for(int i=1;i<=np-1;i++)
                        {
                            // loop over intervals between nodes
                            for(int j=1;j<=precision;j++)
                            {
                                t1=(h[i]*j)/precision;
                                t2=h[i]-t1;
                                y=((-a[i-1]/6d*(t2+h[i])*t1+d[i-1])*t2+(-a[i]/6d*(t1+h[i])*t2+d[i])*t1)/h[i];
                                t=x[i-1]+t1;
                                if(Double.isFinite(y)&&y>=0d&&y<=128d)
                                    path.lineTo((int)t,(int)y);
                                count++;
                                oldt=t;
                                oldy=y;
                            }
                        }
                        if(count>=2)
                            g2d.draw(path);
                        else
                        {
                            Logger.LOGGER.warn("The spline cannot be drawn in page "+getName());
                            for(int i=1;i<measurePoints.size();i++)
                            {
                                double x1=measurePoints.get(i-1).getX();
                                double y1=measurePoints.get(i-1).getY();
                                double x2=measurePoints.get(i).getX();
                                double y2=measurePoints.get(i).getY();
                                g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
                                if(stroke!=null)
                                {
                                    stroke=new BasicStroke(stroke.getLineWidth(),stroke.getEndCap(),stroke.getLineJoin(),stroke.getMiterLimit(),stroke.getDashArray(),stroke.getDashPhase()+(float)Point2D.distance(x1,y1,x2,y2));
                                    g2d.setStroke(stroke);
                                }
                            }
                        }
                    }
        if(curveStrokeType==CurveStrokeType.DOTTED_LINE||curveStrokeType==CurveStrokeType.DASHED_LINE)
            g2d.setStroke(new BasicStroke());
        if(curvePointShape==CurvePointShape.CIRCLE)
            for(int i=0;i<measurePoints.size();i++)
            {
                double x=measurePoints.get(i).getX();
                double y=measurePoints.get(i).getY();
                g2d.drawOval((int)x-2,(int)y-2,4,4);
            }
        else
            if(curvePointShape==CurvePointShape.CROSS)
                for(int i=0;i<measurePoints.size();i++)
                {
                    double x=measurePoints.get(i).getX();
                    double y=measurePoints.get(i).getY();
                    g2d.drawLine((int)x-2,(int)y-2,(int)x+2,(int)y+2);
                    g2d.drawLine((int)x-2,(int)y+2,(int)x+2,(int)y-2);
                }
            else
                if(curvePointShape==CurvePointShape.TRIANGLE)
                    for(int i=0;i<measurePoints.size();i++)
                    {
                        double x=measurePoints.get(i).getX();
                        double y=measurePoints.get(i).getY();
                        g2d.drawLine((int)x,(int)y-2,(int)x-2,(int)y+2);
                        g2d.drawLine((int)x-2,(int)y+2,(int)x+2,(int)y+2);
                        g2d.drawLine((int)x+2,(int)y+2,(int)x,(int)y-2);
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

    public static void main(String[] args)
    {
        _0200000010baTemperatureCurvePage.main(args);
    }
}
