package sky.epaperscreenupdater.page;

import java.awt.geom.Point2D;
import java.util.AbstractList;

public class SpecialWindList extends AbstractList<Point2D>
{
    private final Wind[] winds;
    private final boolean gust;

    public SpecialWindList(Wind[] winds,boolean gust)
    {
        this.winds=winds;
        this.gust=gust;
    }

    public Point2D get(int index)
    {
        return new Point2D()
        {
            public double getX()
            {
                return winds[index].getX();
            }

            public double getY()
            {
                return gust?winds[index].getGustY():winds[index].getWindY();
            }

            public void setLocation(double x,double y)
            {
            }
        };
    }

    public int size()
    {
        return winds.length;
    }
}
