package sky.epaperscreenupdater.page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sky.epaperscreenupdater.Logger;

public class WeatherUtils
{
    private WeatherUtils()
    {
    }

    public static String convertWindAngle(int angle)
    {
        return convertWindAngle((double)angle);
    }

    public static String convertWindAngle(double angle)
    {
        if(angle<11.25d)
            return "N";
        else
            if(angle<33.75d)
                return "NNE";
            else
                if(angle<56.25d)
                    return "NE";
                else
                    if(angle<78.75d)
                        return "ENE";
                    else
                        if(angle<101.25d)
                            return "E";
                        else
                            if(angle<123.75d)
                                return "ESE";
                            else
                                if(angle<146.25d)
                                    return "SE";
                                else
                                    if(angle<168.75d)
                                        return "SSE";
                                    else
                                        if(angle<191.25d)
                                            return "S";
                                        else
                                            if(angle<213.75d)
                                                return "SSO";
                                            else
                                                if(angle<236.25d)
                                                    return "SO";
                                                else
                                                    if(angle<258.75d)
                                                        return "OSO";
                                                    else
                                                        if(angle<281.25d)
                                                            return "O";
                                                        else
                                                            if(angle<303.75d)
                                                                return "ONO";
                                                            else
                                                                if(angle<326.25d)
                                                                    return "NO";
                                                                else
                                                                    if(angle<348.75d)
                                                                        return "NNO";
                                                                    else
                                                                        return "N";
    }

    public static List<Temperature> loadTemperatures(int number)
    {
        try
        {
            try(Connection connection=Database.getToilettesConnection())
            {
//                long startTime=System.currentTimeMillis();
                List<Temperature> temperatures=new ArrayList<>();
                try(Statement statement=connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY))
                {
                    try(ResultSet resultSet=statement.executeQuery("SELECT * FROM temperature ORDER BY time DESC LIMIT "+number+";"))
                    {
                        while(resultSet.next())
                        {
                            long time=resultSet.getLong("time");
                            double temperature=resultSet.getDouble("temperature");
                            double setPoint=resultSet.getDouble("setPoint");
                            double ratio=resultSet.getDouble("ratio");
                            boolean heaterOn=resultSet.getInt("heaterOn")==1;
                            temperatures.add(new Temperature(time,temperature,setPoint,ratio,heaterOn));
                        }
                    }
                }
                Collections.reverse(temperatures);
//                Logger.LOGGER.info(temperatures.size()+" rows fetched in "+(System.currentTimeMillis()-startTime)+" ms");
                return temperatures;
            }
        }
        catch(NotAvailableDatabaseException|SQLException e)
        {
            Logger.LOGGER.error(e.toString());
            e.printStackTrace();
            return new ArrayList<>(0);
        }
    }
}
