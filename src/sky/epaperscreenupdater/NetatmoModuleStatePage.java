package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class NetatmoModuleStatePage extends AbstractNetatmoPage
{
    public NetatmoModuleStatePage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "État des modules Netatmo";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(3).minutePlus(46).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        Map<String,Measure[]> lastMeasures=getLastMeasures();
        Measure _70ee50000deaWifi=HomeWeatherPage.getLastMeasure(lastMeasures,_70ee50000dea_WIFI);
        Measure _70ee50000deaFirmware=HomeWeatherPage.getLastMeasure(lastMeasures,_70ee50000dea_FIRMWARE);
        Measure _030000000216Battery=HomeWeatherPage.getLastMeasure(lastMeasures,_030000000216_BATTERY);
        Measure _030000000216Radio=HomeWeatherPage.getLastMeasure(lastMeasures,_030000000216_RADIO);
        Measure _03000000076eBattery=HomeWeatherPage.getLastMeasure(lastMeasures,_03000000076e_BATTERY);
        Measure _03000000076eRadio=HomeWeatherPage.getLastMeasure(lastMeasures,_03000000076e_RADIO);
        Measure _03000003fe8eBattery=HomeWeatherPage.getLastMeasure(lastMeasures,_03000003fe8e_BATTERY);
        Measure _03000003fe8eRadio=HomeWeatherPage.getLastMeasure(lastMeasures,_03000003fe8e_RADIO);
        Measure _0200000010baBattery=HomeWeatherPage.getLastMeasure(lastMeasures,_0200000010ba_BATTERY);
        Measure _0200000010baRadio=HomeWeatherPage.getLastMeasure(lastMeasures,_0200000010ba_RADIO);
        Measure _05000004152cBattery=HomeWeatherPage.getLastMeasure(lastMeasures,_05000004152c_BATTERY);
        Measure _05000004152cRadio=HomeWeatherPage.getLastMeasure(lastMeasures,_05000004152c_RADIO);
        Measure _06000000729aBattery=HomeWeatherPage.getLastMeasure(lastMeasures,_06000000729a_BATTERY);
        Measure _06000000729aRadio=HomeWeatherPage.getLastMeasure(lastMeasures,_06000000729a_RADIO);
        Font percentageFont=Main.FREDOKA_ONE_FONT.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(.65d,1d));
        Font nameFont=Main.FREDOKA_ONE_FONT.deriveFont(18f).deriveFont(AffineTransform.getScaleInstance(.8d,1d));
        Font infoFont=Main.FREDOKA_ONE_FONT.deriveFont(12f).deriveFont(AffineTransform.getScaleInstance(1.9d,1d));

        String[] names=new String[]
        {
            "Jardin",
            "Chamb",
            "SDB",
            "SS",
            "Anémo",
            "Pluvio",
        };
        double[] batteries=new double[]
        {
            ((_0200000010baBattery==null?0d:_0200000010baBattery.getValue())-4000d)/(5500d-4000d),
            ((_030000000216Battery==null?0d:_030000000216Battery.getValue())-4560d)/(5640d-4560d),
            ((_03000000076eBattery==null?0d:_03000000076eBattery.getValue())-4560d)/(5640d-4560d),
            ((_03000003fe8eBattery==null?0d:_03000003fe8eBattery.getValue())-4560d)/(5640d-4560d),
            ((_06000000729aBattery==null?0d:_06000000729aBattery.getValue())-4360d)/(5590d-4360d),
            ((_05000004152cBattery==null?0d:_05000004152cBattery.getValue())-4000d)/(5500d-4000d),
        };
        double[] radios=new double[]
        {
            (96d-(_0200000010baRadio==null?100d:_0200000010baRadio.getValue()))/(96d-59d),
            (96d-(_030000000216Radio==null?100d:_030000000216Radio.getValue()))/(96d-59d),
            (96d-(_03000000076eRadio==null?100d:_03000000076eRadio.getValue()))/(96d-59d),
            (96d-(_03000003fe8eRadio==null?100d:_03000003fe8eRadio.getValue()))/(96d-59d),
            (96d-(_06000000729aRadio==null?100d:_06000000729aRadio.getValue()))/(96d-59d),
            (96d-(_05000004152cRadio==null?100d:_05000004152cRadio.getValue()))/(96d-59d),
        };
        double wifi=(86d-(_70ee50000deaWifi==null?100d:_70ee50000deaWifi.getValue()))/(86d-56d);
        int firmware=_70ee50000deaFirmware==null?0:(int)_70ee50000deaFirmware.getValue();

        for(int i=0;i<names.length;i++)
        {
            int baseX=i*49+2;
            g2d.drawLine(baseX+19,1,baseX+26,1);
            g2d.drawLine(baseX+17,2,baseX+28,2);
            g2d.drawLine(baseX+17,3,baseX+28,3);
            g2d.drawLine(baseX+16,4,baseX+29,4);
            g2d.drawLine(baseX+16,5,baseX+29,5);
            g2d.drawLine(baseX+16,6,baseX+29,6);
            g2d.drawLine(baseX+5,7,baseX+40,7);
            g2d.drawLine(baseX+3,8,baseX+42,8);
            g2d.drawLine(baseX+2,9,baseX+43,9);
            g2d.drawLine(baseX+2,10,baseX+43,10);
            g2d.drawLine(baseX+1,11,baseX+44,11);
            g2d.drawLine(baseX+1,84,baseX+44,84);
            g2d.drawLine(baseX+2,85,baseX+43,85);
            g2d.drawLine(baseX+2,86,baseX+43,86);
            g2d.drawLine(baseX+2,86,baseX+43,86);
            g2d.drawLine(baseX+3,87,baseX+42,87);
            g2d.drawLine(baseX+5,88,baseX+40,88);
            g2d.drawLine(baseX+1,12,baseX+1,83);
            g2d.drawLine(baseX+2,12,baseX+2,83);
            g2d.drawLine(baseX+3,12,baseX+3,83);
            g2d.drawLine(baseX+4,12,baseX+4,83);
            g2d.drawLine(baseX+5,12,baseX+5,83);
            g2d.drawLine(baseX+40,12,baseX+40,83);
            g2d.drawLine(baseX+41,12,baseX+41,83);
            g2d.drawLine(baseX+42,12,baseX+42,83);
            g2d.drawLine(baseX+43,12,baseX+43,83);
            g2d.drawLine(baseX+44,12,baseX+44,83);
            g2d.drawLine(baseX+6,12,baseX+6,12);
            g2d.drawLine(baseX+39,12,baseX+39,12);
            g2d.drawLine(baseX+6,83,baseX+6,83);
            g2d.drawLine(baseX+39,83,baseX+39,83);

            double bornedBattery=Math.max(0d,Math.min(1d,batteries[i]));
            int up=80-(int)(bornedBattery*64d);
            for(int j=79;j>=up;j--)
                g2d.drawLine(baseX+10,j,baseX+35,j);

            g2d.setFont(percentageFont);
            String percentage=(int)(bornedBattery*100d)+"%";
            int percentageWidth=(int)Math.ceil(percentageFont.getStringBounds(percentage,g2d.getFontRenderContext()).getWidth());
            if(up<66)
            {
                g2d.setColor(Color.WHITE);
                g2d.drawString(percentage,baseX+23-(int)percentageWidth/2,(79+up)/2+6);
                g2d.setColor(Color.BLACK);
            }
            else
                g2d.drawString(percentage,baseX+23-(int)percentageWidth/2,(16+up)/2+6);

            g2d.setFont(nameFont);
            String name=names[i];
            int nameWidth=(int)Math.ceil(nameFont.getStringBounds(name,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(name,baseX+22-(int)nameWidth/2,104);

            double bornedRadio=Math.max(0d,Math.min(1d,radios[i]));
            g2d.drawLine(baseX+4,117,baseX+40,117);
            for(int j=1;j<=(int)(bornedRadio*12d);j++)
            {
                g2d.drawLine(baseX+2+3*j,116,baseX+2+3*j,116-j+1);
                g2d.drawLine(baseX+3+3*j,116,baseX+3+3*j,116-j+1);
            }
        }

        g2d.setFont(infoFont);
        String infoString="Wifi : "+(int)(wifi*100d)+"%  Firmware : "+firmware;
        int infoStringWidth=(int)Math.ceil(infoFont.getStringBounds(infoString,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(infoString,148-infoStringWidth/2,128);
    }

    protected String getDebugImageFileName()
    {
        return "module_state.png";
    }

    public static void main(String[] args)
    {
        new NetatmoModuleStatePage(null).potentiallyUpdate();
    }
}
