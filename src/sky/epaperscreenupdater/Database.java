package sky.epaperscreenupdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database
{
    private Database()
    {
    }

    public static Connection getConnection() throws NotAvailableDatabaseException
    {
        String serverAddress="";
        String serverPort="";
        String databaseName="";
        String user="";
        String password="";
        try(BufferedReader reader=new BufferedReader(new FileReader(new File("database.ini"))))
        {
            serverAddress=reader.readLine();
            serverPort=reader.readLine();
            databaseName=reader.readLine();
            user=reader.readLine();
            password=reader.readLine();
        }
        catch(IOException e)
        {
            Logger.LOGGER.error("Unable to read database connection infos from the config file ("+e.toString()+")");
        }
        Connection connection=null;
        try
        {
            connection=DriverManager.getConnection("jdbc:mariadb://"+serverAddress+":"+serverPort+"/"+databaseName+"?user="+user+"&password="+password);
//            Logger.LOGGER.info("Connection to SQLite has been established.");
        }
        catch(SQLException e)
        {
            try
            {
                if(connection!=null)
                    connection.close();
            }
            catch(SQLException ex)
            {
            }
            Logger.LOGGER.error(e.toString());
        }
        return connection;
    }
}
