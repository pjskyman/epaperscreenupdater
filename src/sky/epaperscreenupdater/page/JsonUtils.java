package sky.epaperscreenupdater.page;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
            SSLContext sslContext=SSLContext.getInstance("SSL");
            sslContext.init(null,new TrustManager[]{new TrustAnyTrustManager()},new SecureRandom());
            httpsConnection=(HttpsURLConnection)urlObject.openConnection();
            httpsConnection.setConnectTimeout(5000);
            httpsConnection.setReadTimeout(5000);
            httpsConnection.setRequestMethod("GET");
            httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
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

    private static class TrustAnyTrustManager implements X509TrustManager
    {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
        {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
        {
        }

        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[]{};
        }
    }
}
