package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sky.epaperscreenupdater.Logger;
import sky.epaperscreenupdater.RefreshType;
import sky.program.Duration;

public class RERCPage extends AbstractSinglePage
{
    public RERCPage(Page parentPage)
    {
        super(parentPage);
    }

    public String getName()
    {
        return "RER C";
    }

    protected RefreshType getRefreshType()
    {
        return RefreshType.PARTIAL_REFRESH;
    }

    protected long getMinimalRefreshDelay()
    {
        return Duration.of(1).minutePlus(3).secondMinus(200).millisecond();
    }

    @SuppressWarnings("unchecked")
    protected void populateImage(Graphics2D g2d) throws VetoException,Exception
    {
        List<String> nextTrains=new ArrayList<>();
        HttpURLConnection connection=null;
        try
        {
            String login="";
            String password="";
            try(BufferedReader reader=new BufferedReader(new FileReader(new File("transilien.ini"))))
            {
                login=reader.readLine();
                password=reader.readLine();
            }
            catch(IOException e)
            {
                Logger.LOGGER.error("Unable to read Transilien access informations from the config file ("+e.toString()+")");
                e.printStackTrace();
            }
            String depart="";
            String arrivee="";
            try(BufferedReader reader=new BufferedReader(new FileReader(new File("transilien_config.ini"))))
            {
                depart=reader.readLine();
                arrivee=reader.readLine();
            }
            catch(IOException e)
            {
                Logger.LOGGER.error("Unable to read Transilien configuration from the config file ("+e.toString()+")");
                e.printStackTrace();
            }
            connection=(HttpURLConnection)new URL("https://api.transilien.com/gare/"+depart+"/depart/"+arrivee).openConnection();
            connection.setRequestProperty("Authorization","Basic "+Base64.getEncoder().encodeToString((login+":"+password).getBytes()));
            connection.setRequestProperty("Accept","application/vnd.sncf.transilien.od.depart+xml;vers=1");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setAllowUserInteraction(false);
            connection.setDoOutput(true);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder stringBuilder=new StringBuilder();
            while((line=bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            connection.disconnect();
//            System.out.println("requestResponse="+stringBuilder.toString());
            Element passagesElement=new SAXBuilder().build(new StringReader(stringBuilder.toString())).getRootElement();
            ((List<Element>)passagesElement.getChildren("train")).forEach(trainElement->
            {
                String date=trainElement.getChild("date").getText();
                String nextTrain=date.substring(date.length()-5,date.length());
                Element etatElement=trainElement.getChild("etat");
                if(etatElement!=null&&etatElement.getText().toLowerCase().contains("retar"))
                    nextTrain+="*";
                nextTrain+=" "+trainElement.getChild("miss").getText();
                if(etatElement!=null&&etatElement.getText().toLowerCase().contains("suppr"))
                    nextTrain+="_";
                nextTrains.add(nextTrain);
            });
        }
        catch(IOException|JDOMException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connection!=null)
                connection.disconnect();
        }
        Font baseFont=FREDOKA_ONE_FONT.deriveFont(24f);
        g2d.setFont(baseFont);
        g2d.drawString("Direction Austerlitz",5,24);
        for(int i=0;i<Math.min(nextTrains.size(),4);i++)
        {
            String string=nextTrains.get(i).replace("_","");
            int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
            g2d.drawString(string,5,24*(i+2));
            if(nextTrains.get(i).contains("_"))
            {
                g2d.drawLine(1,24*(i+2)-9,stringWidth+9,24*(i+2)-9);
                g2d.drawLine(0,24*(i+2)-10,stringWidth+10,24*(i+2)-10);
                g2d.drawLine(1,24*(i+2)-11,stringWidth+9,24*(i+2)-11);
            }
        }
        if(nextTrains.size()>4)
        {
            g2d.drawLine(145,31,145,118);
            g2d.drawLine(146,30,146,119);
            g2d.drawLine(147,31,147,118);
            for(int i=4;i<Math.min(nextTrains.size(),8);i++)
            {
                String string=nextTrains.get(i).replace("_","");
                int stringWidth=(int)Math.ceil(baseFont.getStringBounds(string,g2d.getFontRenderContext()).getWidth());
                g2d.drawString(string,150+5,24*(i-2));
                if(nextTrains.get(i).contains("_"))
                {
                    g2d.drawLine(150+1,24*(i-2)-9,150+stringWidth+9,24*(i-2)-9);
                    g2d.drawLine(150+0,24*(i-2)-10,150+stringWidth+10,24*(i-2)-10);
                    g2d.drawLine(150+1,24*(i-2)-11,150+stringWidth+9,24*(i-2)-11);
                }
            }
        }
    }

    protected String getDebugImageFileName()
    {
        return "rerc.png";
    }

    public static void main(String[] args)
    {
        new RERCPage(null).potentiallyUpdate();
    }
}
