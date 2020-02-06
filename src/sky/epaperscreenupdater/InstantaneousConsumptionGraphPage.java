package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class InstantaneousConsumptionGraphPage extends AbstractNetatmoPage
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

    public synchronized Page potentiallyUpdate()
    {
        List<InstantaneousConsumption> list=Main.loadInstantaneousConsumptions(296);
        if(list.isEmpty())
            return this;
        InstantaneousConsumption instantaneousConsumption=list.get(list.size()-1);
        long consumptionTime=instantaneousConsumption.getTime();
        if(consumptionTime!=lastConsumptionTime)
        {
            Logger.LOGGER.info("Page \""+getName()+"\" needs to be updated");
            lastConsumptionTime=consumptionTime;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                int maxPower=list.stream()
                        .mapToInt(InstantaneousConsumption::getTotalOfConsumptions)
                        .max()
                        .orElse(1);
                Path2D topLine=new Path2D.Double();
                Path2D polygon=new Path2D.Double();
                for(int i=list.size()-1;i>=0;i--)
                {
//                    int height=calculatePowerBarHeight(list.get(i).getTotalOfConsumptions());
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
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("powers_graph.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH).writeImage(sourceImage);
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

    private static int calculatePowerBarHeight(int power)
    {
        return (int)calculatePowerBarHeightDouble(power);
    }

    private static double calculatePowerBarHeightDouble(int power)//méthode identique à celle de InstantaneousConsumptionPage mais spécialement adaptée aux besoins de cette page
    {
        if(power==0)
            return 0d;
        double ordinate1=13.105178503466455d
                +.8935371553157391d*(double)power
                +.0011767571668344792d*Math.pow((double)power,2d)
                -1.2087136238229091e-4d*Math.pow((double)power,3d)
                +1.1938931060353606e-6d*Math.pow((double)power,4d)
                -5.366042379604714e-9d*Math.pow((double)power,5d)
                +1.2375520523981775e-11d*Math.pow((double)power,6d)
                -1.5252113325589502e-14d*Math.pow((double)power,7d)
                +1.0111118973594798e-17d*Math.pow((double)power,8d)
                -3.379100166542495e-21d*Math.pow((double)power,9d)
                +4.117422344411683e-25d*Math.pow((double)power,10d)
                +2.951303006401026e-29d*Math.pow((double)power,11d)
                -4.740442092946457e-33d*Math.pow((double)power,12d)
                -8.818731061778005e-37d*Math.pow((double)power,13d)
                -3.5639045982917994e-41d*Math.pow((double)power,14d)
                +1.8920748927329792e-44d*Math.pow((double)power,15d);
        double ordinate2=23.462972022832d+11.798263558634d*Math.log((double)power);
        if(power<180)
            return (Math.max(13d,ordinate1)-13d+1d)/116d*129d-1d;
        else
            if(power>200)
                return (Math.min(128d,ordinate2)-13d+1d)/116d*129d-1d;
            else
            {
                double factor=((double)power-180d)/20d;
                return (((1d-factor)*ordinate1+factor*ordinate2)-13d+1d)/116d*129d-1d;
            }
    }

    public static void main(String[] args)
    {
//        for(int power=0;power<=9000;power++)
//            System.out.println(power+"="+calculatePowerBarHeight(power));
        new InstantaneousConsumptionGraphPage(null).potentiallyUpdate();
    }
}
