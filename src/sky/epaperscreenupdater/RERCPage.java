package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import sky.program.Duration;

public class RERCPage extends AbstractSinglePage
{
    private long lastRefreshTime;

    public RERCPage(Page parentPage)
    {
        super(parentPage);
        lastRefreshTime=0L;
    }

    public String getName()
    {
        return "RER C";
    }

    @SuppressWarnings("unchecked")
    public synchronized Page potentiallyUpdate()
    {
        long now=System.currentTimeMillis();
        if(now-lastRefreshTime>Duration.of(1).minuteMinus(3).secondPlus(500).millisecond())
        {
            lastRefreshTime=now;
            try
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
                    }
                    connection=(HttpURLConnection)new URL("http://api.transilien.com/gare/"+depart+"/depart/"+arrivee).openConnection();
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
                        String nextTrain="["+trainElement.getChild("miss").getText()+"]";
                        String date=trainElement.getChild("date").getText();
                        nextTrain+=" "+date.substring(date.length()-5,date.length());
                        Element termElement=trainElement.getChild("term");
                        if(termElement!=null)
                            if(termElement.getText().equals("87393843"))
                                nextTrain+=" —› S-Q-e-Y";
                            else
                                if(termElement.getText().equals("87547000")||termElement.getText().equals("87547026"))
                                    nextTrain+=" —› Austerlitz";
                                else
                                    nextTrain+=" —› "+termElement.getText();
                        Element etatElement=trainElement.getChild("etat");
                        if(etatElement!=null)
                            nextTrain+=" ("+etatElement.getText()+")";
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

                BufferedImage sourceImage=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D g2d=sourceImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,0,296,128);
                g2d.setColor(Color.BLACK);
                Font baseFont=EpaperScreenUpdater.FREDOKA_ONE_FONT.deriveFont(16f);
                g2d.setFont(baseFont);
                g2d.drawString("Prochains départs vers Austerlitz :",5,16);
                for(int i=0;i<nextTrains.size();i++)
                    g2d.drawString(nextTrains.get(i),5,16*(i+2));
                g2d.dispose();
//                try(OutputStream outputStream=new FileOutputStream(new File("rerc.png")))
//                {
//                    ImageIO.write(sourceImage,"png",outputStream);
//                }
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

    public static void main(String[] args)
    {
        new RERCPage(null).potentiallyUpdate();
    }
}
