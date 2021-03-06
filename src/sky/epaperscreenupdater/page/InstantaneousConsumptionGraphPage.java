package sky.epaperscreenupdater.page;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;
import sky.housecommon.Database;
import sky.housecommon.InstantaneousConsumption;
import sky.program.Duration;

public class InstantaneousConsumptionGraphPage extends AbstractSinglePage
{
    private long lastConsumptionTime;
    private int dashCounter;

    public InstantaneousConsumptionGraphPage(Page parentPage)
    {
        super(parentPage);
        lastConsumptionTime=0L;
        dashCounter=0;
    }

    public String getName()
    {
        return "Graph. puiss. instan.";
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
        return Duration.of(1).secondPlus(246).millisecond();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        List<InstantaneousConsumption> list=Database.getLastInstantaneousConsumptionsWhile(Duration.of(24).minutePlus(40).second());
        if(list.isEmpty())
            throw new VetoException();
        int maxPower=list.stream()
                .mapToInt(InstantaneousConsumption::getTotalOfConsumptions)
                .max()
                .orElse(1);
        Path2D topLine=new Path2D.Double();
        Path2D polygon=new Path2D.Double();
        for(int i=list.size()-1;i>=0;i--)
        {
//            int height=calculatePowerBarHeight(list.get(i).getTotalOfConsumptions());
            int height=(int)(128d*(double)list.get(i).getTotalOfConsumptions()/(double)maxPower);
            double x=295d-((double)list.size()-1d-(double)i);
            double y=128d-height;
            if(i==list.size()-1)//premier point
            {
                topLine.moveTo(x,y);
                polygon.moveTo(x,128d);
                polygon.lineTo(x,y);
            }
            else
                if(i==0)//dernier point
                {
                    topLine.lineTo(x,y);
                    polygon.lineTo(x,y);
                    polygon.lineTo(x,128d);
                }
                else//point intermédiaire
                {
                    topLine.lineTo(x,y);
                    polygon.lineTo(x,y);
                }
        }
        polygon.closePath();
        g2d.draw(topLine);
        for(int y=0;y<128;y++)
            for(int x=0;x<296;x++)
                if((x+y+dashCounter)%2==0)
                    if(polygon.contains((double)x,(double)y)||x==295&&polygon.contains((double)(x-1),(double)y))
                        g2d.drawRect(x,y,0,0);
        dashCounter++;
    }

    protected String getDebugImageFileName()
    {
        return "powers_graph.png";
    }

    public static void main(String[] args)
    {
//        for(int power=0;power<=9000;power++)
//            System.out.println(power+"="+calculatePowerBarHeight(power));
        new InstantaneousConsumptionGraphPage(null).potentiallyUpdate();
    }
}
