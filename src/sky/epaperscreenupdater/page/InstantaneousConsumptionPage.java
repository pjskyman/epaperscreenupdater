package sky.epaperscreenupdater.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import sky.housecommon.Database;
import sky.housecommon.ElectricityUtils;
import sky.housecommon.InstantaneousConsumption;
import sky.netatmo.Measure;
import sky.netatmo.MeasurementType;
import sky.program.Duration;

public class InstantaneousConsumptionPage extends AbstractSinglePage
{
    private long lastConsumptionTime;

    public InstantaneousConsumptionPage(Page parentPage)
    {
        super(parentPage);
        lastConsumptionTime=0L;
    }

    public String getName()
    {
        return "Puissances instantanées";
    }

    @Override
    protected boolean canUpdate()
    {
        InstantaneousConsumption instantaneousConsumption=Database.getLastInstantaneousConsumption();
        if(instantaneousConsumption==null)
            return false;
        long consumptionTime=instantaneousConsumption.getTime();
        if(consumptionTime!=lastConsumptionTime)
        {
            lastConsumptionTime=consumptionTime;
            return true;
        }
        return false;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).secondPlus(178).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        InstantaneousConsumption instantaneousConsumption=Database.getLastInstantaneousConsumption();
        if(instantaneousConsumption==null)
            throw new VetoException();

        g2d.fill(new RoundRectangle2D.Double(0d,0d,81.1d,30.1d,9d,9d));
        g2d.fill(new RoundRectangle2D.Double(83d,0d,11.1d,30.1d,9d,9d));
        g2d.fill(new RoundRectangle2D.Double(0d,32d,94.1d,30.1d,9d,9d));
        g2d.fill(new RoundRectangle2D.Double(0d,64d,94.1d,30.1d,9d,9d));
        g2d.fill(new RoundRectangle2D.Double(0d,96d,94.1d,30.1d,9d,9d));
        g2d.fillRect(83,25,12,11);
        g2d.drawLine(82,28,82,31);
        g2d.drawLine(81,30,81,31);
        g2d.drawLine(79,31,80,31);
        g2d.setColor(Color.WHITE);

        Font baseFont=FREDOKA_ONE_FONT.deriveFont(20f);
        Font bigFont=baseFont.deriveFont(34f).deriveFont(AffineTransform.getScaleInstance(.7d,1d));
        Font unitFont=baseFont.deriveFont(14f);
        Font verticalUnitFont=unitFont.deriveFont(AffineTransform.getQuadrantRotateInstance(3));
        Font consumerNameFont1=baseFont.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(1.3d,1d));
        Font consumerNameFont2=baseFont.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(.85d,1d));
        Font consumerNameFont3=baseFont.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(.6d,1d));
        Font verticalConsumptionFont=baseFont.deriveFont(15f).deriveFont(AffineTransform.getQuadrantRotateInstance(3));

        Map<String,Measure[]> lastMeasures=NetatmoUtils.getLastMeasures();
        Measure[] array=lastMeasures.get(NetatmoUtils._0200000010ba_TEMPERATURE);
        Measure temperature=NetatmoUtils.estimate(array);
        array=NetatmoUtils.filterTimedWindowMeasures(lastMeasures.get(NetatmoUtils._70ee50000dea_PRESSURE),2);
        Measure pressure=array!=null&&array.length>=1?array[array.length-1]:null;
        PressureTendancy pressureTendancy=PressureTendancy.UNKNOWN;
        if(array!=null&&array.length>=3)
        {
            double[] smoothedPressures=new double[array.length-1];
            for(int i=1;i<array.length;i++)
                smoothedPressures[i-1]=(array[i-1].getValue()+array[i].getValue())/2d;//chaque nouvelle mesure est moyennée à sa précédente pour lisser les erreurs de mesure
            double[] deltaPressures=new double[smoothedPressures.length-1];
            for(int i=1;i<smoothedPressures.length;i++)
                deltaPressures[i-1]=smoothedPressures[i]-smoothedPressures[i-1];//chaque nouvelle valeur est la différence d'une mesure avec sa précédente
            double epsilonPressure=0d;
            for(double deltaPressure:deltaPressures)
                epsilonPressure+=deltaPressure;
            epsilonPressure/=(double)deltaPressures.length;//calcul de la moyenne de toutes les différences
            if(Math.abs(epsilonPressure)<=.02d)
                pressureTendancy=PressureTendancy.CONSTANT;
            else
                if(epsilonPressure>0d)
                    pressureTendancy=PressureTendancy.POSITIVE;
                else
                    pressureTendancy=PressureTendancy.NEGATIVE;
        }
        array=lastMeasures.get(NetatmoUtils._70ee50000dea_CARBON_DIOXYDE);
        Measure carbonDioxyde=NetatmoUtils.estimate(array);
        if(pressureTendancy==PressureTendancy.CONSTANT)
        {
            g2d.drawLine(85,12,85,19);
            g2d.drawLine(86,12,86,19);
            g2d.drawLine(87,13,87,18);
            g2d.drawLine(88,13,88,18);
            g2d.drawLine(89,14,89,17);
            g2d.drawLine(90,14,90,17);
            g2d.drawLine(91,15,91,16);
            g2d.drawLine(92,15,92,16);
        }
        else
            if(pressureTendancy==PressureTendancy.POSITIVE)
            {
                g2d.drawLine(85,16,92,16);
                g2d.drawLine(85,15,92,15);
                g2d.drawLine(86,14,91,14);
                g2d.drawLine(86,13,91,13);
                g2d.drawLine(87,12,90,12);
                g2d.drawLine(87,11,90,11);
                g2d.drawLine(88,10,89,10);
                g2d.drawLine(88,9,89,9);
            }
            else
                if(pressureTendancy==PressureTendancy.NEGATIVE)
                {
                    g2d.drawLine(85,16,92,16);
                    g2d.drawLine(85,17,92,17);
                    g2d.drawLine(86,18,91,18);
                    g2d.drawLine(86,19,91,19);
                    g2d.drawLine(87,20,90,20);
                    g2d.drawLine(87,21,90,21);
                    g2d.drawLine(88,22,89,22);
                    g2d.drawLine(88,23,89,23);
                }

        g2d.setFont(bigFont);
        String temperatureString=temperature!=null?DECIMAL_0_FORMAT.format(temperature.getValue()).replace(",","."):"?";
        int temperatureStringWidth=(int)Math.ceil(bigFont.getStringBounds(temperatureString,g2d.getFontRenderContext()).getWidth());

        g2d.setFont(unitFont);
        String temperatureUnitString="°C";
        int temperatureUnitStringWidth=(int)Math.ceil(unitFont.getStringBounds(temperatureUnitString,g2d.getFontRenderContext()).getWidth());

        g2d.setFont(bigFont);
        g2d.drawString(temperatureString,42f-(float)(temperatureStringWidth+temperatureUnitStringWidth)/2f,28f);
        g2d.setFont(unitFont);
        g2d.drawString(temperatureUnitString,42-(temperatureStringWidth+temperatureUnitStringWidth)/2+temperatureStringWidth+1,17-4);

        g2d.setFont(bigFont);
        String pressureString=pressure!=null?""+pressure.getValue():"?";
        int pressureStringWidth=(int)Math.ceil(bigFont.getStringBounds(pressureString,g2d.getFontRenderContext()).getWidth());

        g2d.setFont(verticalUnitFont);
        String pressureUnitString="hPa";
        int pressureUnitStringWidth=(int)Math.ceil(verticalUnitFont.getStringBounds(pressureUnitString,g2d.getFontRenderContext()).getWidth());
        int pressureUnitStringHeight=(int)Math.ceil(verticalUnitFont.getStringBounds(pressureUnitString,g2d.getFontRenderContext()).getHeight());

        g2d.setFont(bigFont);
        g2d.drawString(pressureString,50f-(float)(pressureStringWidth+pressureUnitStringHeight)/2f,60f);
        g2d.setFont(verticalUnitFont);
        g2d.drawString(pressureUnitString,50-(pressureStringWidth+pressureUnitStringHeight)/2+pressureStringWidth+12,pressureUnitStringWidth+34+1);

        g2d.setFont(bigFont);
        String carbonDioxydeString=carbonDioxyde!=null?""+(int)carbonDioxyde.getValue():"?";
        int carbonDioxydeStringWidth=(int)Math.ceil(bigFont.getStringBounds(carbonDioxydeString,g2d.getFontRenderContext()).getWidth());

        g2d.setFont(verticalUnitFont);
        String carbonDioxydeUnitString="ppm";
        int carbonDioxydeUnitStringWidth=(int)Math.ceil(verticalUnitFont.getStringBounds(carbonDioxydeUnitString,g2d.getFontRenderContext()).getWidth());
        int carbonDioxydeUnitStringHeight=(int)Math.ceil(verticalUnitFont.getStringBounds(carbonDioxydeUnitString,g2d.getFontRenderContext()).getHeight());

        g2d.setFont(bigFont);
        g2d.drawString(carbonDioxydeString,50f-(float)(carbonDioxydeStringWidth+carbonDioxydeUnitStringHeight)/2f,92f);
        g2d.setFont(verticalUnitFont);
        g2d.drawString(carbonDioxydeUnitString,50-(carbonDioxydeStringWidth+carbonDioxydeUnitStringHeight)/2+carbonDioxydeStringWidth+9,carbonDioxydeUnitStringWidth+66-1);

        g2d.setFont(bigFont);
        String totalPowerString=""+instantaneousConsumption.getTotalOfConsumptions();
        int totalPowerStringWidth=(int)Math.ceil(bigFont.getStringBounds(totalPowerString,g2d.getFontRenderContext()).getWidth());

        g2d.setFont(unitFont);
        String totalPowerUnitString="W";
        int totalPowerUnitStringWidth=(int)Math.ceil(unitFont.getStringBounds(totalPowerUnitString,g2d.getFontRenderContext()).getWidth());

        g2d.setFont(bigFont);
        g2d.drawString(totalPowerString,92-(totalPowerStringWidth+totalPowerUnitStringWidth),124);
        g2d.setFont(unitFont);
        g2d.drawString(totalPowerUnitString,92f-(float)(totalPowerStringWidth+totalPowerUnitStringWidth)+(float)totalPowerStringWidth+1f,113f-4f);

        g2d.drawLine(1,108,19,108);
        Rectangle rectangle=new Rectangle(2,104,5,4);
        for(int x=rectangle.x;x<rectangle.x+rectangle.width;x++)
            for(int y=rectangle.y;y<rectangle.y+rectangle.height;y++)
                if((x+y)%2==0||instantaneousConsumption.getPricingPeriod().isBlueDay())
                    g2d.drawRect(x,y,0,0);
        rectangle=new Rectangle(8,102,5,6);
        for(int x=rectangle.x;x<rectangle.x+rectangle.width;x++)
            for(int y=rectangle.y;y<rectangle.y+rectangle.height;y++)
                if((x+y)%2==0||instantaneousConsumption.getPricingPeriod().isWhiteDay())
                    g2d.drawRect(x,y,0,0);
        rectangle=new Rectangle(14,98,5,10);
        for(int x=rectangle.x;x<rectangle.x+rectangle.width;x++)
            for(int y=rectangle.y;y<rectangle.y+rectangle.height;y++)
                if((x+y)%2==0||instantaneousConsumption.getPricingPeriod().isRedDay())
                    g2d.drawRect(x,y,0,0);
//        Logger.LOGGER.info("Current pricing period: "+instantaneousConsumption.getPricingPeriod().name());
        String tomorrow=TomorrowUtils.getTomorrow();
        if(tomorrow.contains("BLEU"))
        {
            g2d.drawLine(4,109,4,109);
            g2d.drawLine(3,110,5,110);
            g2d.drawLine(2,111,6,111);
        }
        else
            if(tomorrow.contains("BLANC"))
            {
                g2d.drawLine(10,109,10,109);
                g2d.drawLine(9,110,11,110);
                g2d.drawLine(8,111,12,111);
            }
            else
                if(tomorrow.contains("ROUGE"))
                {
                    g2d.drawLine(16,109,16,109);
                    g2d.drawLine(15,110,17,110);
                    g2d.drawLine(14,111,18,111);
                }
        if(instantaneousConsumption.getPricingPeriod().isPeakHourPeriod())
        {
            g2d.drawLine(10,115,12,115);
            g2d.drawLine(9,116,13,116);
            g2d.drawLine(8,117,14,117);
            g2d.drawLine(8,118,14,118);
            g2d.drawLine(8,119,14,119);
            g2d.drawLine(9,120,13,120);
            g2d.drawLine(10,121,12,121);
            g2d.drawLine(11,114,11,112);
            g2d.drawLine(15,118,18,118);
            g2d.drawLine(11,122,11,124);
            g2d.drawLine(7,118,4,118);
            g2d.drawLine(14,115,14,115);
            g2d.drawLine(15,114,15,114);
            g2d.drawLine(16,113,16,113);
            g2d.drawLine(14,121,14,121);
            g2d.drawLine(15,122,15,122);
            g2d.drawLine(16,123,16,123);
            g2d.drawLine(8,121,8,121);
            g2d.drawLine(7,122,7,122);
            g2d.drawLine(6,123,6,123);
            g2d.drawLine(8,115,8,115);
            g2d.drawLine(7,114,7,114);
            g2d.drawLine(6,113,6,113);
        }
        else
        {
            g2d.drawLine(10,112,14,112);
            g2d.drawLine(8,113,13,113);
            g2d.drawLine(7,114,11,114);
            g2d.drawLine(6,115,10,115);
            g2d.drawLine(6,116,9,116);
            g2d.drawLine(5,117,9,117);
            g2d.drawLine(5,118,9,118);
            g2d.drawLine(5,119,9,119);
            g2d.drawLine(6,120,10,120);
            g2d.drawLine(6,121,12,121);
            g2d.drawLine(7,122,17,122);
            g2d.drawLine(8,123,16,123);
            g2d.drawLine(10,124,14,124);
            g2d.drawLine(17,121,18,121);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine(95,128-13,296,128-13);
        g2d.drawLine(95,128-13-4,95,128-13+4);
        g2d.drawLine(96,128-13-2,96,128-13+2);
        g2d.drawLine(97,128-13-1,97,128-13+1);
        int[] powers=new int[10];
        for(int i=0;i<powers.length;i++)
            powers[i]=instantaneousConsumption.getConsumerConsumption(i+1);
        String[] consumerNames=new String[]{"E1","E2","P1","P2","R","Cu","L&V","F","Ch","CC"};
        for(int i=0;i<powers.length;i++)
        {
            int sliceStartAbscissa=i*20+97;
            int powerBarHeight=ElectricityUtils.calculatePowerBarHeight(powers[i]);
            rectangle=new Rectangle(sliceStartAbscissa+2,128-13-powerBarHeight,16,powerBarHeight);
            g2d.setColor(Color.BLACK);
            g2d.draw(rectangle);
            for(int y=0;y<rectangle.height;y++)
                for(int x=0;x<rectangle.width;x++)
                    g2d.draw(new Rectangle(sliceStartAbscissa+2+x,128-13-y,0,0));

            g2d.setFont(consumerNameFont1);
            String consumerNameString=consumerNames[i];
            int consumerNameStringWidth=(int)Math.ceil(consumerNameFont1.getStringBounds(consumerNameString,g2d.getFontRenderContext()).getWidth());
            if(consumerNameStringWidth>16)
            {
                g2d.setFont(consumerNameFont2);
                consumerNameStringWidth=(int)Math.ceil(consumerNameFont2.getStringBounds(consumerNameString,g2d.getFontRenderContext()).getWidth());
                if(consumerNameStringWidth>19)
                {
                    g2d.setFont(consumerNameFont3);
                    consumerNameStringWidth=(int)Math.ceil(consumerNameFont3.getStringBounds(consumerNameString,g2d.getFontRenderContext()).getWidth());
                }
            }
            g2d.drawString(consumerNameString,sliceStartAbscissa+10-consumerNameStringWidth/2,128);

            if(powers[i]>0)
            {
                g2d.setFont(verticalConsumptionFont);
                String powerString=powers[i]+" W";
                int powerStringWidth=(int)Math.ceil(verticalConsumptionFont.getStringBounds(powerString,g2d.getFontRenderContext()).getWidth());
                if(powerStringWidth+5>powerBarHeight)//écriture à l'extérieur
                {
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(powerString,sliceStartAbscissa+16,128-13-powerBarHeight-1);
                }
                else//écriture normale à l'intérieur
                {
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(powerString,sliceStartAbscissa+16,128-13-powerBarHeight+powerStringWidth+1);
                }
            }
        }
    }

    protected String getDebugImageFileName()
    {
        return "powers.png";
    }

    public static void main2(String[] args)
    {
        new InstantaneousConsumptionPage(null).potentiallyUpdate();
    }

    public static void main(String[] args)
    {
        double[] temperatures=new double[]
        {
            4.9d,
            5.2d,
            5.5d,
            5.8d,
            6.1d,
            6.4d,
            6.7d,
            7.0d,
            7.3d,
            7.6d,
            7.6d,
            7.8d,
            8.0d,
            8.2d,
            8.4d,
            8.7d,
            8.9d,
            9.1d,
            9.3d,
            9.1d,
            9.0d,
            8.9d,
            9.0d,
            9.5d,
        };

        long now=1554637759387L;
        List<Measure> measures=new ArrayList<>(temperatures.length);
        for(int index=0;index<temperatures.length;index++)
            measures.add(new StandAloneMeasure(new Date(now+(long)(index*301_500)),MeasurementType.TEMPERATURE,temperatures[index]));

        List<WeightedObservedPoint> points=new ArrayList<>(measures.size());
        for(int index=0;index<measures.size();index++)
            points.add(new WeightedObservedPoint(index==measures.size()-1?1_000d:1d,(double)measures.get(index).getDate().getTime(),measures.get(index).getValue()));
        while(points.get(points.size()-1).getX()-points.get(0).getX()>1_200_000d&&points.size()>4)
            points.remove(0);
        points.forEach(point->System.out.println("mesure utilisée pour le calcul : "+(long)point.getX()+" "+point.getY()));

        List<WeightedObservedPoint> correctedPoints=new ArrayList<>(points.size());
        points.forEach(point->correctedPoints.add(new WeightedObservedPoint(point.getWeight(),(point.getX()-points.get(0).getX())/60_000d,point.getY())));

        int degree=Math.min(2,correctedPoints.size()-1);
        System.out.println("degré de la régression : "+degree);
        PolynomialCurveFitter curveFitter=PolynomialCurveFitter.create(degree);
        double[] result=curveFitter.fit(correctedPoints);
        System.out.println("coefficients de la régression : "+Arrays.toString(result));
        for(int index=0;index<=30;index++)
        {
            double time=correctedPoints.get(correctedPoints.size()-1).getX()+(double)(60_000*index)/60_000d;
            double value=0d;
            for(int index2=0;index2<result.length;index2++)
                value+=result[index2]*Math.pow(time,(double)index2);
            System.out.println((long)time+" minutes plus tard, valeur estimée : "+DECIMAL_0_FORMAT.format(value)+" ("+value+")");
        }
    }
}
