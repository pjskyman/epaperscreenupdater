package sky.epaperscreenupdater;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WasherProfiles
{
    private WasherProfiles()
    {
    }

    public static List<WasherGenericConsumption> getLV50()
    {
        return getConsumptions("lv50.profile");
    }

    public static List<WasherGenericConsumption> getLV4565()
    {

        return getConsumptions("lv4565.profile");
    }

    public static List<WasherGenericConsumption> getLV70()
    {
        return getConsumptions("lv70.profile");
    }

    public static List<WasherGenericConsumption> getLL40()
    {
        return getConsumptions("ll40.profile");
    }

    public static List<WasherGenericConsumption> getLL60()
    {
        return getConsumptions("ll60.profile");
    }

    private static List<WasherGenericConsumption> getConsumptions(String filename)
    {
        try(DataInputStream inputStream=new DataInputStream(new BufferedInputStream(new FileInputStream(new File(filename)))))
        {
            int size=inputStream.readInt();
            List<WasherGenericConsumption> list=new ArrayList<>(size);
            for(int i=0;i<size;i++)
                list.add(new WasherGenericConsumption(inputStream.readLong(),inputStream.readInt()));
            return list;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return new ArrayList<>(0);
        }
    }

    public static void main(String[] args)
    {
        System.out.println("coucou");
    }
}
