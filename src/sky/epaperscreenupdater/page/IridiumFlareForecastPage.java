package sky.epaperscreenupdater.page;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sky.program.Duration;

public class IridiumFlareForecastPage extends AbstractSinglePage
{
    private static final boolean IRIDIUM_FLARE_FORECAST_WEBSITE_ENABLED=true;

    public IridiumFlareForecastPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "Flash Iridium";
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).hourPlus(2).minutePlus(36).second();
    }

    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        List<IridiumFlare> nextIridiumFlares=new ArrayList<>();
        HttpURLConnection connection=null;
        try
        {
            StringBuilder stringBuilder=new StringBuilder();
            if(IRIDIUM_FLARE_FORECAST_WEBSITE_ENABLED)
            {
                connection=(HttpURLConnection)new URL("https://www.heavens-above.com/IridiumFlares.aspx?lat=48.5322&lng=2.2953&loc=91770+Saint-Vrain%2c+France&alt=83&tz=CET").openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setAllowUserInteraction(false);
                connection.setDoOutput(true);
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line=bufferedReader.readLine())!=null)
                    stringBuilder.append(line);
                connection.disconnect();
            }
            else
                try(BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File("iridium.html")),Charset.forName("Unicode"))))
                {
                    String line;
                    while((line=reader.readLine())!=null)
                        stringBuilder.append(line);
                }
//            System.out.println("requestResponse="+stringBuilder.toString());
            Pattern pattern1=Pattern.compile(".*<tbody>(.*)</tbody>.*");
            Matcher matcher1=pattern1.matcher(stringBuilder.toString());
            if(matcher1.matches())
            {
                String tableContent=matcher1.group(1).trim();
//                System.out.println(tableContent);
                List<int[]> list1=new ArrayList<>();
                int index=-1;
                while(true)
                {
                    int[] array=new int[2];
                    index=tableContent.indexOf("<tr",index);
                    if(index==-1)
                        break;
                    index=tableContent.indexOf(">",index);
                    if(index==-1)
                        break;
                    array[0]=index+">".length();
                    index=tableContent.indexOf("</tr>",index);
                    if(index==-1)
                        break;
                    array[1]=index;
                    index+="</tr>".length();
                    list1.add(array);
                }
//                System.out.println(list1.size());
//                list1.forEach(array->System.out.println(Arrays.toString(array)));
                for(int i=0;i<list1.size();i++)
                {
                    String lineContent=tableContent.substring(list1.get(i)[0],list1.get(i)[1]);
//                    System.out.println("    "+lineContent);
                    List<int[]> list2=new ArrayList<>();
                    index=-1;
                    while(true)
                    {
                        int[] array=new int[2];
                        index=lineContent.indexOf("<td",index);
                        if(index==-1)
                            break;
                        index=lineContent.indexOf(">",index);
                        if(index==-1)
                            break;
                        array[0]=index+">".length();
                        index=lineContent.indexOf("</td>",index);
                        if(index==-1)
                            break;
                        array[1]=index;
                        index+="</td>".length();
                        list2.add(array);
                    }
//                    System.out.println(list2.size());
//                    list2.forEach(array->System.out.println(Arrays.toString(array)));
                    String string=lineContent.substring(list2.get(0)[0],list2.get(0)[1]).trim();
                    string=string.substring(string.indexOf(">")+">".length(),string.indexOf("</a>"));
                    GregorianCalendar calendar=new GregorianCalendar();
                    int year=calendar.get(Calendar.YEAR);
                    Date date;
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMMM d, HH:mm:ss",Locale.ENGLISH);
                    String string2=year+" "+string;
                    try
                    {
                        date=dateFormat.parse(string2);
                    }
                    catch(ParseException e)
                    {
                        dateFormat=new SimpleDateFormat("yyyy MMMM d, HH:mm:ss",Locale.FRENCH);
                        date=dateFormat.parse(string2);
                    }
                    if(Math.abs(date.getTime()-calendar.getTimeInMillis())>Duration.of(180).day())
                    {
                        string2=(year-1)+" "+string;
                        date=dateFormat.parse(string2);
                        if(Math.abs(date.getTime()-calendar.getTimeInMillis())>Duration.of(180).day())
                        {
                            string2=(year+1)+" "+string;
                            date=dateFormat.parse(string2);
                            if(Math.abs(date.getTime()-calendar.getTimeInMillis())>Duration.of(180).day())
                            {
                                string2=year+" "+string;
                                date=dateFormat.parse(string2);
                            }
                        }
                    }
                    string=lineContent.substring(list2.get(1)[0],list2.get(1)[1]).trim();
                    double magnitude=Double.parseDouble(string.replace(",","."));
                    string=lineContent.substring(list2.get(2)[0],list2.get(2)[1]).trim();
                    string=string.substring(0,string.indexOf("°"));
                    int altitude=Integer.parseInt(string);
                    string=lineContent.substring(list2.get(3)[0],list2.get(3)[1]).trim();
                    string=string.substring(0,string.indexOf("°"));
                    int azimuth=Integer.parseInt(string);
                    string=lineContent.substring(list2.get(4)[0],list2.get(4)[1]).trim();
                    String name=string.trim();
                    string=lineContent.substring(list2.get(5)[0],list2.get(5)[1]).trim();
                    string=string.substring(0,string.indexOf(" km"));
                    int centerDistance=Integer.parseInt(string);
                    string=lineContent.substring(list2.get(6)[0],list2.get(6)[1]).trim();
                    double centerMagnitude=Double.parseDouble(string.replace(",","."));
                    string=lineContent.substring(list2.get(7)[0],list2.get(7)[1]).trim();
                    string=string.substring(0,string.indexOf("°"));
                    int sunAltitude=Integer.parseInt(string);
                    if(date.getTime()>=System.currentTimeMillis())
                        nextIridiumFlares.add(new IridiumFlare(date,magnitude,altitude,azimuth,name,centerDistance,centerMagnitude,sunAltitude));
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connection!=null)
                connection.disconnect();
        }
//        nextIridiumFlares.stream()
//                .forEach(iridiumFlare->System.out.println(iridiumFlare.toString()));
        for(int x=51;x<296;x+=35)
            g2d.drawLine(x,0,x,127);
        for(int y=20;y<128;y+=12)
            g2d.drawLine(0,y,295,y);
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(12f);
        Font compactFont=FREDOKA_ONE_FONT.deriveFont(12f)
                .deriveFont(AffineTransform.getScaleInstance(.8d,1d));

        g2d.setFont(baseFont);

        String string="Date";
        int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,14);

        string="Heure";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,31);

        string="Magntd";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,43);

        string="Alt°";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,55);

        string="Az°";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,67);

        string="Nom";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,79);

        string="DistKm";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,91);

        string="MagMax";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,103);

        string="SolAlt°";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,115);

        string="PJIndex";
        stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
        g2d.drawString(string,26-stringWidth/2,127);

        Stroke largeStroke=new BasicStroke(2.5f);
        Stroke normalStroke=new BasicStroke();
        g2d.setStroke(normalStroke);

        for(int i=0;i<nextIridiumFlares.size();i++)
        {
            int baseX=51+i*35;
            IridiumFlare iridiumFlare=nextIridiumFlares.get(i);

            GregorianCalendar calendar=new GregorianCalendar();
            calendar.setTime(iridiumFlare.getDate());
            string=calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.FRANCE);
            string=string.substring(0,1).toUpperCase()+string.substring(1);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,9);

            string=SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(iridiumFlare.getDate()).substring(0,2);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,19);

            string=new SimpleDateFormat("HH:mm").format(iridiumFlare.getDate());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,31);

            string=DECIMAL_0_FORMAT.format(iridiumFlare.getMagnitude());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,43);

            string=""+iridiumFlare.getAltitude();
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,55);
            g2d.fillArc(baseX+4+stringWidth/2-12/2-4,55-22/2,25,22,0,iridiumFlare.getAltitude());

            g2d.setFont(compactFont);

            string=WeatherUtils.convertWindAngle(iridiumFlare.getAzimuth());
            stringWidth=(int)Math.ceil(compactFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,67);
            g2d.setStroke(largeStroke);
            g2d.drawLine(
                    baseX+16+stringWidth/2-4+(int)(Math.sin((double)iridiumFlare.getAzimuth()*Math.PI/180d+Math.PI)*6d),
                    67-5-(int)(Math.cos((double)iridiumFlare.getAzimuth()*Math.PI/180d+Math.PI)*6d),
                    baseX+16+stringWidth/2-4-(int)(Math.sin((double)iridiumFlare.getAzimuth()*Math.PI/180d+Math.PI)*4d),
                    67-5+(int)(Math.cos((double)iridiumFlare.getAzimuth()*Math.PI/180d+Math.PI)*4d)
            );
            g2d.setStroke(normalStroke);
            Path2D path=new Path2D.Double();
            path.moveTo(
                    (double)baseX+16d+(double)stringWidth/2d-4d-Math.sin((double)iridiumFlare.getAzimuth()*Math.PI/180d+Math.PI)*6d,
                    67d-5d+Math.cos((double)iridiumFlare.getAzimuth()*Math.PI/180d+Math.PI)*6d
            );
            path.lineTo(
                    (double)baseX+16d+(double)stringWidth/2d-4d-Math.sin((double)(iridiumFlare.getAzimuth()+90)*Math.PI/180d+Math.PI)*5d,
                    67d-5d+Math.cos((double)(iridiumFlare.getAzimuth()+90)*Math.PI/180d+Math.PI)*5d
            );
            path.lineTo(
                    (double)baseX+16d+(double)stringWidth/2d-4d-Math.sin((double)(iridiumFlare.getAzimuth()-90)*Math.PI/180d+Math.PI)*5d,
                    67d-5d+Math.cos((double)(iridiumFlare.getAzimuth()-90)*Math.PI/180d+Math.PI)*5d
            );
            path.closePath();
            g2d.fill(path);

            string=iridiumFlare.getName();
            string=string.substring(0,4)+string.substring(string.indexOf(" "));
            stringWidth=(int)Math.ceil(compactFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,79);

            g2d.setFont(baseFont);

            string=""+iridiumFlare.getCenterDistance();
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,91);

            string=DECIMAL_0_FORMAT.format(iridiumFlare.getCenterMagnitude());
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,baseX+18-stringWidth/2,103);

            string=""+iridiumFlare.getSunAltitude();
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,115);
            g2d.fillArc(baseX+8+stringWidth/2-12/2-4,106-18/2,19,18,0,iridiumFlare.getSunAltitude());

            string=INTEGER_FORMAT.format(iridiumFlare.getPJIndex()*100d);
            stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth())+12;
            g2d.drawString(string,baseX+18-stringWidth/2,127);
            g2d.fillOval(baseX+18+stringWidth/2-12/2-4,127-12/2-5,12,12);
            g2d.setColor(Color.WHITE);
            g2d.fillArc(baseX+18+stringWidth/2-10/2-4,127-10/2-5,10,10,90,(int)(360d-iridiumFlare.getPJIndex()*360d));
            g2d.setColor(Color.BLACK);
        }
    }

    protected String getDebugImageFileName()
    {
        return "iridium.png";
    }

    public static void main(String[] args)
    {
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMMM d, HH:mm:ss");
//        Stream.of("juil. 31, 23:44:51","août 2, 23:42:21","août 4, 04:58:14","août 7, 04:49:06")
//                .map(string->"2018 "+string)
//                .map(string->
//                {
//                    try
//                    {
//                        return dateFormat.parse(string);
//                    }
//                    catch(ParseException e)
//                    {
//                        return new Date();
//                    }
//                })
//                .forEach(date->System.out.println(date.toString()));
        new IridiumFlareForecastPage(null).potentiallyUpdate();
    }
}
