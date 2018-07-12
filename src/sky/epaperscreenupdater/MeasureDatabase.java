package sky.epaperscreenupdater;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import sky.netatmo.Measure;
import sky.program.Duration;

public class MeasureDatabase
{
    private final Map<MeasureDatabaseKey,Measure> measures;

    public MeasureDatabase()
    {
        measures=new HashMap<>();
    }

    public void addMeasures(String measureKind,Measure[] measures)
    {
        Arrays.stream(measures)
                .forEach(measure->this.measures.put(new MeasureDatabaseKey(measure.getDate().getTime(),measureKind),measure));
    }

    public void clean()
    {
        int sizeBefore=measures.size();
        long now=System.currentTimeMillis();
        measures.keySet().stream()
                .filter(measure->now-measure.getTime()>Duration.of(1).dayPlus(3).hour())
                .forEach(measures::remove);
        Logger.LOGGER.info("Database cleaned in "+(System.currentTimeMillis()-now)+" ms, "+(sizeBefore-measures.size())+" measures deleted");
    }

    public Measure[] getMeasures(String measureKind,long from,long to)
    {
        return measures.entrySet().stream()
                .filter(entry->entry.getKey().getMeasureKind().equals(measureKind)&&entry.getKey().getTime()>=from&&entry.getKey().getTime()<=to)
                .map(Entry::getValue)
                .sorted()
                .toArray(Measure[]::new);
    }
}
