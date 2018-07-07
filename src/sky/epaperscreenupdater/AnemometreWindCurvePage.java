package sky.epaperscreenupdater;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class AnemometreWindCurvePage extends AbstractNetatmoCurvePage
{
    public AnemometreWindCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe vent";
    }

    protected long getRefreshDelay()
    {
        return Duration.of(1).minutePlus(43).second();
    }

    protected String getMeasureMapKey()
    {
        return ANEMOMETRE_GUST_STRENGTH;
    }

    protected String getOrdinateLabelText()
    {
        return "Vent jardin (km/h)";
    }

    protected String getVerificationFileName()
    {
        return "courbeaw.png";
    }

    protected double getMinimalYRange()
    {
        return 10d;
    }

    @Override
    protected void drawChart(Map<String,Measure[]> measureMap,Font baseFont,Font verticalBaseFont,Graphics2D g2d)
    {
        Measure[] rawMeasures1=measureMap.get(ANEMOMETRE_WIND_STRENGTH);
        Measure[] rawAngleMeasures1=measureMap.get(ANEMOMETRE_WIND_ANGLE);
        Measure[] rawMeasures2=measureMap.get(ANEMOMETRE_GUST_STRENGTH);
        Measure[] rawAngleMeasures2=measureMap.get(ANEMOMETRE_GUST_ANGLE);
        Measure[] measures1=HomeWeatherVariationPage.filterTimedWindowMeasures(rawMeasures1,3);
        Measure[] angleMeasures1=HomeWeatherVariationPage.filterTimedWindowMeasures(rawAngleMeasures1,3);
        Measure[] measures2=HomeWeatherVariationPage.filterTimedWindowMeasures(rawMeasures2,3);
        Measure[] angleMeasures2=HomeWeatherVariationPage.filterTimedWindowMeasures(rawAngleMeasures2,3);
        if(measures1!=null&&measures2!=null&&angleMeasures1!=null&&angleMeasures2!=null)
        {
            String ordinateLabelText=getOrdinateLabelText();
            int ordinateLabelTextWidth=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getWidth());
            int ordinateLabelTextHeight=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getHeight());
            g2d.setFont(verticalBaseFont);
            g2d.drawString(ordinateLabelText,ordinateLabelTextHeight-5,(128-ordinateLabelTextHeight+3)/2+ordinateLabelTextWidth/2);
            XRange xRange=computeXRange(measures1);
            YRange yRange1=computeYRange(measures1);
            YRange yRange2=computeYRange(measures2);
            YRange yRange=new YRange(Math.min(yRange1.getMin(),yRange2.getMin()),Math.max(yRange1.getMax(),yRange2.getMax()));
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
            List<Point2D> measurePoints1=new ArrayList<>(measures1.length);
            for(Measure measure:measures1)
            {
                long time=measure.getDate().getTime();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=measure.getValue();
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                measurePoints1.add(new Point2D.Double(x,y));
            }
            List<Point2D> measurePoints2=new ArrayList<>(measures1.length);
            for(Measure measure:measures2)
            {
                long time=measure.getDate().getTime();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=measure.getValue();
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                measurePoints2.add(new Point2D.Double(x,y));
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
            for(int i=measures1.length-1;i>=0;i-=6)
            {
                double x=measurePoints1.get(i).getX();
                String timeString=SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(measures1[i].getDate());
                int timeStringWidth=(int)Math.ceil(baseFont.getStringBounds(timeString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(timeString,(int)x-(i==measures1.length-1?timeStringWidth-10:timeStringWidth/2),128);
                g2d.drawLine((int)x,128-ordinateLabelTextHeight+5,(int)x,0);
            }
            g2d.setStroke(new BasicStroke());
            drawData(g2d,measures1,measurePoints1,ordinateLabelTextHeight);
            drawData(g2d,measures2,measurePoints2,ordinateLabelTextHeight);
        }
    }

    public static void main(String[] args)
    {
        new AnemometreWindCurvePage(null).potentiallyUpdate();
    }
}
