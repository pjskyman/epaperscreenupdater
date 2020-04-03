package sky.epaperscreenupdater.page;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class _06000000729aWindCurvePage extends AbstractNetatmoCurvePage
{
    private static final double LOW_HEIGHT_RATIO=1.373d;//coefficient corrigeant les valeurs en fonction de la hauteur annoncée de l'anémomètre

    public _06000000729aWindCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe vent";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(43).second();
    }

    protected String getMeasureKind()
    {
        return NetatmoUtils._06000000729a_WIND_STRENGTH;
    }

    protected String getOrdinateLabelText()
    {
        return "Vent jardin (km/h)";
    }

    protected double getMinimalYRange()
    {
        return STANDARD_WIND_MINIMAL_Y_RANGE;
    }

    protected double getMinimalY()
    {
        return STANDARD_WIND_MINIMAL_Y;
    }

    @Override
    protected void drawChart(Map<String,Measure[]> measureMap,Font baseFont,Font verticalBaseFont,Graphics2D g2d)
    {
        //cette redéfinition est spécifique pour dessiner les 2 courbes du vent ainsi que les flèches de direction en haut du graphique
        Measure[] rawMeasures=measureMap.get(NetatmoUtils._06000000729a_WIND_STRENGTH);
        Measure[] rawAngleMeasures=measureMap.get(NetatmoUtils._06000000729a_WIND_ANGLE);
        Measure[] rawGustMeasures=measureMap.get(NetatmoUtils._06000000729a_GUST_STRENGTH);
        Measure[] rawGustAngleMeasures=measureMap.get(NetatmoUtils._06000000729a_GUST_ANGLE);
        Measure[] measures=NetatmoUtils.filterTimedWindowMeasures(rawMeasures,3);
        Measure[] angleMeasures=NetatmoUtils.filterTimedWindowMeasures(rawAngleMeasures,3);
        Measure[] gustMeasures=NetatmoUtils.filterTimedWindowMeasures(rawGustMeasures,3);
        Measure[] gustAngleMeasures=NetatmoUtils.filterTimedWindowMeasures(rawGustAngleMeasures,3);
        if(measures!=null&&gustMeasures!=null&&angleMeasures!=null&&gustAngleMeasures!=null)
        {
            Measure nowMeasure=NetatmoUtils.estimate(measures);
            Measure nowAngleMeasure=NetatmoUtils.estimate(angleMeasures);
            Measure nowGustMeasure=NetatmoUtils.estimate(gustMeasures);
            Measure nowGustAngleMeasure=NetatmoUtils.estimate(gustAngleMeasures);
            String ordinateLabelText=getOrdinateLabelText();
            int ordinateLabelTextWidth=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getWidth());
            int ordinateLabelTextHeight=(int)Math.ceil(baseFont.getStringBounds(ordinateLabelText,g2d.getFontRenderContext()).getHeight());
            g2d.setFont(verticalBaseFont);
            g2d.drawString(ordinateLabelText,ordinateLabelTextHeight-5,(128-ordinateLabelTextHeight+3)/2+ordinateLabelTextWidth/2);
            XRange xRange=computeXRange(measures,nowMeasure);
            YRange yRange1=computeYRange(measures,nowMeasure);
            YRange yRange2=computeYRange(gustMeasures,nowGustMeasure);
            YRange yRange=new YRange(yRange1,yRange2);
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
            Wind[] winds=new Wind[measures.length];
            for(int i=0;i<winds.length;i++)
            {
                long time=measures[i].getDate().getTime();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=calculateRealValue(measures[i].getValue());
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y1=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                value=calculateRealValue(gustMeasures[i].getValue());
                double y2=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                winds[i]=new Wind(measures[i].getDate(),
                        calculateRealValue(measures[i].getValue()),
                        angleMeasures[i].getValue(),
                        calculateRealValue(gustMeasures[i].getValue()),
                        gustAngleMeasures[i].getValue(),
                        x,
                        y1,
                        y2);
            }
            Wind nowWind=null;
            if(nowMeasure!=null&&nowGustMeasure!=null&&nowAngleMeasure!=null&&nowGustAngleMeasure!=null)
            {
                long time=nowMeasure.getDate().getTime();
                int xLeft=ordinateLabelTextHeight+maxOrdinateWidth;
                int xRight=295-10;
                double x=(double)(xRight-xLeft)*(1d-(double)(xRange.getMax()-time)/(double)xRange.getAmplitude())+(double)xLeft;
                double value=calculateRealValue(nowMeasure.getValue());
                int yTop=0;
                int yBottom=128-ordinateLabelTextHeight+3;
                double y1=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                value=calculateRealValue(nowGustMeasure.getValue());
                double y2=(double)(yBottom-yTop)*(1d-(value-yRange.getMin())/yRange.getAmplitude())+(double)yTop;
                nowWind=new Wind(nowMeasure.getDate(),
                        calculateRealValue(nowMeasure.getValue()),
                        nowAngleMeasure.getValue(),
                        calculateRealValue(nowGustMeasure.getValue()),
                        nowGustAngleMeasure.getValue(),
                        x,
                        y1,
                        y2);
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
            for(int i=winds.length-(nowWind!=null?0:1);i>=0;i-=6)
            {
                double x=(i==winds.length?nowWind:winds[i]).getX();
                String timeString=SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format((i==winds.length?nowWind:winds[i]).getTime());
                int timeStringWidth=(int)Math.ceil(baseFont.getStringBounds(timeString,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(timeString,(int)x-(i==winds.length-(nowWind!=null?0:1)?timeStringWidth-10:timeStringWidth/2),128);
                g2d.drawLine((int)x,128-ordinateLabelTextHeight+5,(int)x,0);
            }
            g2d.setStroke(new BasicStroke());
            drawData(g2d,new SpecialWindList(winds,false),ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.CONTINUOUS_LINE,CurvePointShape.CIRCLE);
            drawData(g2d,new SpecialWindList(winds,true),ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.CONTINUOUS_LINE,CurvePointShape.TRIANGLE);
            if(nowWind!=null)
            {
                drawData(g2d,new SpecialWindList(new Wind[]{winds[winds.length-1],nowWind},false),ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.CONTINUOUS_LINE,CurvePointShape.NOTHING);
                drawData(g2d,new SpecialWindList(new Wind[]{winds[winds.length-1],nowWind},true),ordinateLabelTextHeight,CurveLineType.SPLINE,CurveStrokeType.CONTINUOUS_LINE,CurvePointShape.NOTHING);
            }
            for(int i=winds.length-1;i>=0;i-=4)//on ne tient pas compte de l'estimation nowWind dans ce dessin car rien n'est sûr
            {
                int j=Math.max(0,i-3);
                Wind[] selectedWinds=new Wind[i-j+1];
                for(int k=0;k<selectedWinds.length;k++)
                    selectedWinds[k]=winds[i-k];
                double meanWind=0d;
                for(Wind selectedWind:selectedWinds)
                    meanWind+=selectedWind.getWind();
                meanWind/=(double)selectedWinds.length;
                if(meanWind<2d)
                    continue;
                double meanX=0d;
                for(Wind selectedWind:selectedWinds)
                    meanX+=selectedWind.getX();
                meanX/=(double)selectedWinds.length;
                if(meanX<38)
                    continue;
                double meanWindAngleX=0d;
                double meanWindAngleY=0d;
                for(Wind selectedWind:selectedWinds)
                {
                    meanWindAngleX+=Math.sin(selectedWind.getWindAngle()*Math.PI/180d);
                    meanWindAngleY+=Math.cos(selectedWind.getWindAngle()*Math.PI/180d);
                }
                double meanWindAngle=90d-Math.atan2(meanWindAngleY,meanWindAngleX)*180d/Math.PI;
                double y=10d;
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawLine(
                        (int)(meanX+Math.sin(meanWindAngle*Math.PI/180d)*8d),(int)(y-Math.cos(meanWindAngle*Math.PI/180d)*8d),
                        (int)(meanX-Math.sin(meanWindAngle*Math.PI/180d)*6d),(int)(y+Math.cos(meanWindAngle*Math.PI/180d)*6d)
                );
                Path2D path=new Path2D.Double();
                path.moveTo(meanX-Math.sin(meanWindAngle*Math.PI/180d)*10d,y+Math.cos(meanWindAngle*Math.PI/180d)*10d);
                path.lineTo(meanX-Math.sin((meanWindAngle+90d)*Math.PI/180d)*5d,y+Math.cos((meanWindAngle+90d)*Math.PI/180d)*5d);
                path.lineTo(meanX-Math.sin((meanWindAngle-90d)*Math.PI/180d)*5d,y+Math.cos((meanWindAngle-90d)*Math.PI/180d)*5d);
                path.closePath();
                g2d.fill(path);
            }
        }
    }

    private double calculateRealValue(double value)
    {
        return (double)Integer.parseInt(INTEGER_FORMAT.format(value/LOW_HEIGHT_RATIO))*LOW_HEIGHT_RATIO;
    }

    @Override
    protected YRange computeYRange(Measure[] measures,Measure nowMeasure)
    {
        //cette redéfinition est spécifique pour rajouter davantage de marge en haut des courbes pour accueillir les flèches de direction du vent
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
        yMax+=yAmplitude/2.5d;//cette redéfinition de computeYRange rajoute davantage de marge en haut des courbes pour accueillir les flèches de direction du vent
        return new YRange(yMin,yMax);
    }

    protected String getDebugImageFileName()
    {
        return "courbeaw.png";
    }

    public static void main(String[] args)
    {
        new _06000000729aWindCurvePage(null).potentiallyUpdate();
    }
}
