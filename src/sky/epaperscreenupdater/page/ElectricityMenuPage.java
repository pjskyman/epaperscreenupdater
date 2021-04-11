package sky.epaperscreenupdater.page;

public class ElectricityMenuPage extends AbstractMenuPage
{
    public ElectricityMenuPage(Page parentPage)
    {
        super(parentPage);
//        subpages.add(new InstantaneousConsumptionGraphPage(this).potentiallyUpdate());
        subpages.add(new EnergyConsumptionPage(this).potentiallyUpdate());
//        subpages.add(new TodayPricePage(this).potentiallyUpdate());
        subpages.add(new OffPeakHourPeriodEfficiencyPage(this).potentiallyUpdate());
//        subpages.add(new DelayedStartTablePage(this).potentiallyUpdate());
        subpages.add(new WasherSupervisionPage(this).potentiallyUpdate());
        subpages.add(new TempoCalendarPage(this).potentiallyUpdate());
    }

    public String getName()
    {
        return "Électricité";
    }
}
