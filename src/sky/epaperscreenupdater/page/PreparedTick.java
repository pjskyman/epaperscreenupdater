package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

public class PreparedTick
{
    private final double value;
    private final String name;
    private final Rectangle2D nameDimensions;
    private static final DecimalFormat DECIMAL_FORMAT=new DecimalFormat("0.########E00");

    public PreparedTick(double value,Font font,FontRenderContext fontRenderContext)
    {
        value=Double.parseDouble(DECIMAL_FORMAT.format(value).replace(",","."));
        this.value=value;
        if(value==(double)(int)value)
            name=""+(int)value;
        else
            name=""+value;
        this.nameDimensions=font.getStringBounds(name,fontRenderContext);
    }

    public double getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public Rectangle2D getNameDimensions()
    {
        return nameDimensions;
    }
}
