package compsci290.edu.duke.myeveryday.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import compsci290.edu.duke.myeveryday.util.Constants;

/**
 * Created by yx78 on 4/15/17.
 * This class store some intial tags that we provide to users
 */

public class SampleData {

    public static List<String> getSampleCategories() {
        List<String> Tagnames = new ArrayList<>();

        Tagnames.add("Family");
        Tagnames.add("Food");
        Tagnames.add("Music");
        Tagnames.add("Dream");
        Tagnames.add("Sports");
        Tagnames.add("Travel");
        Tagnames.add("Productivity");
        Tagnames.add("Personal");
        Tagnames.add("Finance");
        Tagnames.add("Fitness");
        Tagnames.add("Blog Posts");
        Tagnames.add("Social Media");


        return Tagnames;

    }


}

