package sky.epaperscreenupdater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import sky.netatmo.Measure;
import sky.program.Duration;

public class InstantaneousConsumptionPage extends AbstractNetatmoPage
{
    private long lastConsumptionTime;
    private long lastTomorrowVerificationTime;
    private String tomorrow;

    public InstantaneousConsumptionPage(Page parentPage)
    {
        super(parentPage);
        lastConsumptionTime=0L;
        lastTomorrowVerificationTime=0L;
        tomorrow="ND";
    }

    public String getName()
    {
        return "Puissances instantanées";
    }

    public synchronized Page potentiallyUpdate()
    {
        List<InstantaneousConsumption> list=EpaperScreenUpdater.loadInstantaneousConsumptions(1);
        if(list.isEmpty())
            return this;
        InstantaneousConsumption instantaneousConsumption=list.get(0);
        long consumptionTime=instantaneousConsumption.getTime();
        if(consumptionTime!=lastConsumptionTime)
        {
            lastConsumptionTime=consumptionTime;
            try
            {
                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
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

                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(20f);
                Font bigFont=baseFont.deriveFont(34f).deriveFont(AffineTransform.getScaleInstance(.7d,1d));
                Font unitFont=baseFont.deriveFont(14f);
                Font verticalUnitFont=unitFont.deriveFont(AffineTransform.getQuadrantRotateInstance(3));
                Font consumerNameFont1=baseFont.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(1.3d,1d));
                Font consumerNameFont2=baseFont.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(.85d,1d));
                Font consumerNameFont3=baseFont.deriveFont(15f).deriveFont(AffineTransform.getScaleInstance(.6d,1d));
                Font verticalConsumptionFont=baseFont.deriveFont(15f).deriveFont(AffineTransform.getQuadrantRotateInstance(3));

                Map<String,Measure[]> lastMeasures=getLastMeasures();
                Measure[] array=lastMeasures.get(JARDIN_TEMPERATURE);
                Measure temperature=array!=null&&array.length>=1?array[array.length-1]:null;
                array=lastMeasures.get(SALON_PRESSURE);
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
                array=lastMeasures.get(SALON_CARBON_DIOXYDE);
                Measure carbonDioxyde=array!=null&&array.length>=1?array[array.length-1]:null;
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
                String temperatureString=temperature!=null?""+temperature.getValue():"?";
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

                g2d.drawLine(1,109,19,109);
                Rectangle rectangle=new Rectangle(2,105,5,4);
                for(int x=rectangle.x;x<rectangle.x+rectangle.width;x++)
                    for(int y=rectangle.y;y<rectangle.y+rectangle.height;y++)
                        if((x+y)%2==1||instantaneousConsumption.getPricingPeriod().isBlueDay())
                            g2d.drawRect(x,y,0,0);
                rectangle=new Rectangle(8,103,5,6);
                for(int x=rectangle.x;x<rectangle.x+rectangle.width;x++)
                    for(int y=rectangle.y;y<rectangle.y+rectangle.height;y++)
                        if((x+y)%2==1||instantaneousConsumption.getPricingPeriod().isWhiteDay())
                            g2d.drawRect(x,y,0,0);
                rectangle=new Rectangle(14,99,5,10);
                for(int x=rectangle.x;x<rectangle.x+rectangle.width;x++)
                    for(int y=rectangle.y;y<rectangle.y+rectangle.height;y++)
                        if((x+y)%2==1||instantaneousConsumption.getPricingPeriod().isRedDay())
                            g2d.drawRect(x,y,0,0);
//                Logger.LOGGER.info("Current pricing period: "+instantaneousConsumption.getPricingPeriod().name());
                long now=System.currentTimeMillis();
                if(now-lastTomorrowVerificationTime>Duration.of(5).minutePlus(3).second())
                    try
                    {
                        GregorianCalendar calendar=new GregorianCalendar(Locale.FRANCE);
                        calendar.setTimeInMillis(now);
                        int hour=calendar.get(Calendar.HOUR_OF_DAY);
                        int minute=calendar.get(Calendar.MINUTE);
                        if(hour>=7||hour==6&&minute>=2)//on est en journée, après la bascule de jour du matin à 6h02 et avant minuit
                            calendar.setTimeInMillis(now+Duration.of(1).day());//pour avoir demain
                        String day=""+calendar.get(Calendar.DAY_OF_MONTH);
                        if(day.length()==1)
                            day="0"+day;
                        String month=""+(calendar.get(Calendar.MONTH)+1);
                        if(month.length()==1)
                            month="0"+month;
                        JsonObject tomorrowObject=getJsonResponse("https://particulier.edf.fr/bin/edf_rc/servlets/ejptemponew?Date_a_remonter="+calendar.get(Calendar.YEAR)+"-"+month+"-"+day+"&TypeAlerte=TEMPO");
                        String oldTomorrow=tomorrow;
                        if(tomorrowObject!=null)
                        {
                            tomorrow=tomorrowObject.getAsJsonObject("JourJ").getAsJsonPrimitive("Tempo").getAsString();
                            if(tomorrow.equals(oldTomorrow))
                                Logger.LOGGER.info("Tomorrow's color verified, no change, always "+oldTomorrow);
                            else
                                Logger.LOGGER.info("Tomorrow's color updated: "+oldTomorrow+" -> "+tomorrow);
                            lastTomorrowVerificationTime=now;
                        }
                    }
                    catch(Exception e)
                    {
                        Logger.LOGGER.error("Unable to get tomorrow's color ("+e.toString()+")");
                    }
                if(tomorrow.equals("BLEU"))
                    g2d.drawLine(2,110,6,110);
                else
                    if(tomorrow.equals("BLANC"))
                        g2d.drawLine(8,110,12,110);
                    else
                        if(tomorrow.equals("ROUGE"))
                            g2d.drawLine(14,110,18,110);
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
                    int powerBarHeight=calculatePowerBarHeight(powers[i]);
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
                g2d.dispose();
    //            try(OutputStream outputStream=new FileOutputStream(new File("powers.png")))
    //            {
    //                ImageIO.write(sourceImage,"png",outputStream);
    //            }
                pixels=new Pixels(RefreshType.PARTIAL_REFRESH).writeImage(sourceImage);
                Logger.LOGGER.info("Page \""+getName()+"\" updated successfully");
            }
            catch(Exception e)
            {
                Logger.LOGGER.error("Unknown error ("+e.toString()+")");
            }
        }
        return this;
    }

    private static int calculatePowerBarHeight(int power)
    {
        if(power==0)
            return 0;
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
            return (int)Math.max(13d,ordinate1)-13+1;
        else
            if(power>200)
                return (int)Math.min(128d,ordinate2)-13+1;
            else
            {
                double factor=((double)power-180d)/20d;
                return (int)((1d-factor)*ordinate1+factor*ordinate2)-13+1;
            }
    }

    public static JsonObject getJsonResponse(String url) throws IOException,JsonSyntaxException
    {
        URL urlObject=new URL(url);
        HttpURLConnection httpConnection=null;
        try
        {
            httpConnection=(HttpURLConnection)urlObject.openConnection();
            httpConnection.setConnectTimeout(5000);
            httpConnection.setReadTimeout(5000);
            httpConnection.setRequestMethod("GET");
            StringBuilder response=new StringBuilder();
            String inputLine;
            try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpConnection.getInputStream())))
            {
                while((inputLine=bufferedReader.readLine())!=null)
                    response.append(inputLine);
            }
            return new JsonParser().parse(response.toString()).getAsJsonObject();
        }
        finally
        {
            if(httpConnection!=null)
                httpConnection.disconnect();
        }
    }
}
