package sky.epaperscreenupdater.page;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;
import sky.netatmo.Measure;
import sky.program.Duration;

public class _05000004152cRainCurvePage extends AbstractNetatmoCurvePage
{
    public _05000004152cRainCurvePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Courbe pluie";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(41).second();
    }

    protected String getMeasureKind()
    {
        return _05000004152c_RAIN;
    }

    protected String getOrdinateLabelText()
    {
        return "Pluie jardin (mm)";
    }

    @Override
    protected YRange computeYRange(Measure[] measures)
    {
        //cette redéfinition est spécifique pour les barres de pluviométrie qui n'ont rien à voir avec des courbes classiques
        double yMin=0d;
        double yMax=-1e10d;
        for(Measure measure:measures)
            if(measure.getValue()>yMax)
                yMax=measure.getValue();
        double yAmplitude=yMax-yMin;
        if(yAmplitude<=0d)
        {
            yMax=1d;
            yAmplitude=1d;
        }
        yMax+=yAmplitude/15d;
        return new YRange(yMin,yMax);
    }

    protected double getMinimalYRange()//n'est pas utilisée avec le pluviomètre
    {
        return 0d;
    }

    protected double getMinimalY()//n'est pas utilisée avec le pluviomètre
    {
        return 0d;
    }

    @Override
    protected void drawData(Graphics2D g2d,List<Point2D> measurePoints,int ordinateLabelTextHeight,CurveLineType curveLineType,CurveStrokeType curveStrokeType,CurvePointShape curvePointShape)
    {
        //cette redéfinition est spécifique pour dessiner les barres de pluviométrie qui n'ont rien à voir avec des courbes classiques
        if(curveLineType==CurveLineType.NOTHING)
            return;
        for(int i=0;i<measurePoints.size();i++)
        {
            double x=measurePoints.get(i).getX();
            double y=measurePoints.get(i).getY();
            int minX;
            if(i==0)
                minX=(int)measurePoints.get(0).getX();
            else
                minX=(int)((measurePoints.get(i-1).getX()+x)/2d)+1;
            int maxX;
            if(i==measurePoints.size()-1)
                maxX=2*(int)x-minX;
            else
                maxX=(int)((measurePoints.get(i+1).getX()+x)/2d)-1;
            g2d.fillRect(minX,(int)y,maxX-minX+1,128-ordinateLabelTextHeight+3-(int)y);
        }
    }

    protected String getDebugImageFileName()
    {
        return "courbepr.png";
    }

    public static void main(String[] args)
    {
        new _05000004152cRainCurvePage(null).potentiallyUpdate();
    }
}
