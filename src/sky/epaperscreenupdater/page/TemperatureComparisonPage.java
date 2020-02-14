package sky.epaperscreenupdater.page;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class TemperatureComparisonPage extends AbstractNetatmoCurvePage
{
    public TemperatureComparisonPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Comparaison températures";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(23).secondPlus(567).millisecond();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._0200000010ba_TEMPERATURE;
    }

    protected String getOrdinateLabelText()
    {
        return "Température (°C)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_TEMPERATURE_MINIMAL_Y;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void drawChart(Map<String,Measure[]> measureMap,Font baseFont,Font verticalBaseFont,Graphics2D g2d)
    {
        //cette redéfinition est spécifique pour dessiner toutes les courbes de température
        String[] measureKinds=new String[]
        {
            NetatmoUtils._70ee50000dea_TEMPERATURE,
            NetatmoUtils._03000003fe8e_TEMPERATURE,
            NetatmoUtils._0200000010ba_TEMPERATURE
        };
        Measure[][] rawMeasures=new Measure[measureKinds.length][];
        for(int i=0;i<measureKinds.length;i++)
            rawMeasures[i]=measureMap.get(measureKinds[i]);
        Measure[][] measures=new Measure[measureKinds.length][];
        for(int i=0;i<measureKinds.length;i++)
            measures[i]=NetatmoUtils.filterTimedWindowMeasures(rawMeasures[i],3);
        if(Arrays.stream(measures).allMatch(array->array!=null))
        {
            String ordinateLabelText=getOrdinateLabelText();
            int ordinateLabelTextWidth=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getWidth());
            int ordinateLabelTextHeight=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getHeight());
            g2d.setFont(verticalBaseFont);
            g2d.drawString(ordinateLabelText,ordinateLabelTextHeight-5,(128-ordinateLabelTextHeight+3)/2+ordinateLabelTextWidth/2);
            XRange xRange=computeXRange(measures[0]);
            YRange yRange=computeYRange(measures[0]);
            for(int i=1;i<measureKinds.length;i++)
                yRange=new YRange(yRange,computeYRange(measures[i]));
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
            List<Point2D>[] measurePoints=(List<Point2D>[])new List[measureKinds.length];
            for(int i=0;i<measureKinds.length;i++)
            {
                measurePoints[i]=new ArrayList<>(measures[i].length);
                for(Measure measure:measures[i])
                {
                    long time=measure.getDate().getTime();
                    int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                    int xRight=295-10;
                    double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                    double value=measure.getValue();
                    int yTop=0;
                    int yBottom=128-ordinateLabelTextHeight+3;
                    double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                    measurePoints[i].add(new Point2D.Double(x,y));
                }
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
            for(int i=measures[0].length-1;i>=0;i-=6)
            {
                double x=measurePoints[0].get(i).getX();
                String timeString=SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(measures[0][i].getDate());
                int timeStringWidth=(int)Math.ceil(baseFont.getStringBounds(timeString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(timeString,(int)x-(i==measures[0].length-1?timeStringWidth-10:timeStringWidth/2),128);
                g2d.drawLine((int)x,128-ordinateLabelTextHeight+5,(int)x,0);
            }
            g2d.setStroke(new BasicStroke());
            for(int i=0;i<measureKinds.length;i++)
                drawData(g2d,measurePoints[i],ordinateLabelTextHeight,CurveLineType.SPLINE,i<measureKinds.length-1?CurveStrokeType.CONTINUOUS_LINE:CurveStrokeType.DASHED_LINE,i<measureKinds.length-1?CurvePointShape.values()[i%3]:CurvePointShape.NOTHING);
        }
    }

    protected String getDebugImageFileName()
    {
        return "comparaison_temperature.png";
    }

    public static void main(String[] args)
    {
        new TemperatureComparisonPage(null).potentiallyUpdate();
    }
}
