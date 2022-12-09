package be.brigandze;

import be.brigandze.control.EventController;
import be.brigandze.control.Match;
import io.quarkus.scheduler.Scheduled;

import javax.inject.Singleton;

@Singleton
public class Scheduler {

    EventController eventController = EventController.getInstance();

    private int currentEventId;

    @Scheduled( every = "60s")
    public void updateActiveMatch(){
        Match currentMatch = eventController.getCurrentMatch();


    }
}
