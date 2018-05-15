package sky.epaperscreenupdater;

public class ClockMenuPage extends AbstractMenuPage
{
    public ClockMenuPage(Page parentPage)
    {
        super(parentPage);
        subpages.add(new DigitalClockPage(this).potentiallyUpdate());
        subpages.add(new AnalogClockPage(this).potentiallyUpdate());
        subpages.add(new BinaryClockPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Horloges";
    }
}
