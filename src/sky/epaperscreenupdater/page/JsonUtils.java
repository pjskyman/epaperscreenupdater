package sky.epaperscreenupdater.page;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import sun.security.ssl.SSLSocketFactoryImpl;

public class JsonUtils
{
    private JsonUtils()
    {
    }

    public static JsonObject getJsonResponse(String url) throws IOException,JsonSyntaxException
    {
        URL urlObject=new URL(url);
        HttpsURLConnection httpsConnection=null;
        try
        {
            httpsConnection=(HttpsURLConnection)urlObject.openConnection();
            httpsConnection.setConnectTimeout(5000);
            httpsConnection.setReadTimeout(5000);
            httpsConnection.setRequestMethod("GET");
            StringBuilder response=new StringBuilder();
            String inputLine;
            try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpsConnection.getInputStream())))
            {
                while((inputLine=bufferedReader.readLine())!=null)
                    response.append(inputLine);
            }
            return new JsonParser().parse(response.toString()).getAsJsonObject();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new JsonObject();
        }
        finally
        {
            if(httpsConnection!=null)
                httpsConnection.disconnect();
        }
    }

    public static JsonObject getLaxJsonResponse(String url) throws IOException,JsonSyntaxException
    {
        URL urlObject=new URL(url);
        HttpsURLConnection httpsConnection=null;
        try
        {
            httpsConnection=(HttpsURLConnection)urlObject.openConnection();
            httpsConnection.setConnectTimeout(5000);
            httpsConnection.setReadTimeout(5000);
            httpsConnection.setRequestMethod("GET");
            httpsConnection.setSSLSocketFactory(new SSLSocketFactoryImpl());
            httpsConnection.setHostnameVerifier((String string,SSLSession ssls)->true);
            StringBuilder response=new StringBuilder();
            String inputLine;
            try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpsConnection.getInputStream())))
            {
                while((inputLine=bufferedReader.readLine())!=null)
                    response.append(inputLine);
            }
            return new JsonParser().parse(response.toString()).getAsJsonObject();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new JsonObject();
        }
        finally
        {
            if(httpsConnection!=null)
                httpsConnection.disconnect();
        }
    }
}
