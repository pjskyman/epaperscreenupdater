package sky.epaperscreenupdater.page;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class DailyWeatherForecast1Page extends AbstractSinglePage
{
    public DailyWeatherForecast1Page(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Prévisions météo 1/2";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(4).minutePlus(9).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        List<Daily> dailies=WeatherForecastUtils.getLastDailies();
        for(int x=51;x<296;x+=35)
            g2d.drawLine(x,0,x,127);
        for(int y=20;y<128;y+=12)
            g2d.drawLine(0,y,295,y);
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(12f);

        g2d.setFont(baseFont);

        String string="Date";
        int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,14);

        string="Icône";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,31);

        string="°CMin";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,43);

        string="°CMax";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,55);

        string="%Hum";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,67);

        string="°CRosée";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,79);

        string="Vent";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,91);

        string="mmPluie";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,103);

        string="%Pluie";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,115);

        string="hPa";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,127);

        Stroke largeStroke=new BasicStroke(2.5f);
        Stroke normalStroke=new BasicStroke();
        g2d.setStroke(normalStroke);

        for(int i=0;i<dailies.size();i++)
        {
            int baseX=51+i*35;
            Daily daily=dailies.get(i);

            GregorianCalendar calendar=new GregorianCalendar();
            calendar.setTimeInMillis(daily.getTime());
            string=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.FRANCE);
            string=string.substring(0,1).toUpperCase()+string.substring(1);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,9);

            string=SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(new Date(daily.getTime())).substring(0,2);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,19);

            g2d.drawImage(Icons.getIcon(daily.getIcon()),baseX+1,21,null);

            string=DECIMAL_0_FORMAT.format(daily.getTemperatureMin());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,43);

            string=DECIMAL_0_FORMAT.format(daily.getTemperatureMax());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,55);

            string=INTEGER_FORMAT.format(daily.getHumidity()*100d);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,67);
            g2d.fillOval(baseX+18+stringWidth/2-12/2-4,67-12/2-5,12,12);
            g2d.setColor(Color.WHITE);
            g2d.fillArc(baseX+18+stringWidth/2-10/2-4,67-10/2-5,10,10,90,(int)(360d-daily.getHumidity()*360d));
            g2d.setColor(Color.BLACK);

            string=DECIMAL_0_FORMAT.format(daily.getDewPoint());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,79);

            string=INTEGER_FORMAT.format(daily.getWindGust());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,91);
            g2d.setStroke(largeStroke);
            g2d.drawLine(
                    baseX+18+stringWidth/2-4+(int)(Math.sin((double)daily.getWindBearing()*Math.PI/180d)*6d),
                    91-5-(int)(Math.cos((double)daily.getWindBearing()*Math.PI/180d)*6d),
                    baseX+18+stringWidth/2-4-(int)(Math.sin((double)daily.getWindBearing()*Math.PI/180d)*4d),
                    91-5+(int)(Math.cos((double)daily.getWindBearing()*Math.PI/180d)*4d)
            );
            g2d.setStroke(normalStroke);
            Path2D path=new Path2D.Double();
            path.moveTo(
                    (double)baseX+18d+(double)stringWidth/2d-4d-Math.sin((double)daily.getWindBearing()*Math.PI/180d)*6d,
                    91d-5d+Math.cos((double)daily.getWindBearing()*Math.PI/180d)*6d
            );
            path.lineTo(
                    (double)baseX+18d+(double)stringWidth/2d-4d-Math.sin((double)(daily.getWindBearing()+90)*Math.PI/180d)*5d,
                    91d-5d+Math.cos((double)(daily.getWindBearing()+90)*Math.PI/180d)*5d
            );
            path.lineTo(
                    (double)baseX+18d+(double)stringWidth/2d-4d-Math.sin((double)(daily.getWindBearing()-90)*Math.PI/180d)*5d,
                    91d-5d+Math.cos((double)(daily.getWindBearing()-90)*Math.PI/180d)*5d
            );
            path.closePath();
            g2d.fill(path);

            string=DECIMAL_0_FORMAT.format(daily.getPrecipIntensity()*24d);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,103);

            string=INTEGER_FORMAT.format(daily.getPrecipProbability()*100d);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,115);
            g2d.fillOval(baseX+18+stringWidth/2-12/2-4,115-12/2-5,12,12);
            g2d.setColor(Color.WHITE);
            g2d.fillArc(baseX+18+stringWidth/2-10/2-4,115-10/2-5,10,10,90,(int)(360d-daily.getPrecipProbability()*360d));
            g2d.setColor(Color.BLACK);

            string=INTEGER_FORMAT.format(daily.getPressure());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,127);
        }
    }

    protected String getDebugImageFileName()
    {
        return "weather1.png";
    }

    public static void main(String[] args)
    {
        new DailyWeatherForecast1Page(null).potentiallyUpdate();
    }
}
