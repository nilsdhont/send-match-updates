package be.brigandze.control;

import be.brigandze.entity.Event;
import be.brigandze.entity.TeamEventList;
import be.brigandze.entity.Teams;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.brigandze.control.EventPredicates.eventDoesntHaveEndTime;
import static be.brigandze.control.EventPredicates.eventHasOpponents;
import static be.brigandze.control.EventPredicates.eventIsAtHome;
import static be.brigandze.control.EventPredicates.eventIsToday;
import static be.brigandze.control.EventPredicates.eventNotEnded;
import static be.brigandze.sporteasy.SportEasyResource.getSportEasyInstance;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;

public class EventController {

    private static final Logger LOG = Logger.getLogger(EventController.class);

    private static EventController instance;

    private Match currentMatch;

    private EventController() {
    }

    public static EventController getInstance() {
        if (instance == null) {
            instance = new EventController();
        }
        return instance;
    }

    void updateCurrentMatch() {
        List<Event> eventsToday = getEventsToday();
        Optional<Event> event = getCorrectEvent(eventsToday); // Selects the first event that has not yet finished
        if (event.isPresent()) {
            Match newMatch = createMatch(event);
            if (currentMatch == null || !currentMatch.equals(newMatch)) {
                this.currentMatch = newMatch;
                currentMatch.printScore();
            }
        } else {
            LOG.info("NO MATCH AVAILABLE");
        }
    }

    private static Match createMatch(Optional<Event> event) {
        return Match.builder()
                .id(event.get().getId())
                .teamId(event.get().getOpponent_left().getId())
                .nameBrigandZe(event.get().getOpponent_left().getFull_name())
                .nameVisitors(event.get().getOpponent_right().getFull_name())
                .build();
    }

    private List<Event> getEventsToday() {
        return stream(Teams.values()) // Get all the relevant teams
                .map(Teams::getId)
                .map(getSportEasyInstance()::getEvents) // Call to sportEasy to get all the events of the teams selected
                .filter(Objects::nonNull)
                .map(TeamEventList::getResults)
                .flatMap(List::stream)
                .filter(eventHasOpponents) // Only events with 2 teams playing each other
//                .filter(eventIsAtHome) // Only home events
                .filter(eventIsToday) // Only the events of the day
                .sorted(comparing(Event::getStart_at)) // Sort them on start time
                .collect(Collectors.toList());
    }

    private Optional<Event> getCorrectEvent(List<Event> eventsNextMatchDay) {
        if (eventsNextMatchDay == null || eventsNextMatchDay.isEmpty()) {
            return Optional.empty();
        } else if (eventsNextMatchDay.size() == 1) {
            return Optional.of(eventsNextMatchDay.get(0));
        }
        eventsNextMatchDay.stream() // Set default duration for an event
            .filter(eventDoesntHaveEndTime)
            .forEach(event -> event.setEnd_atFromLocalDateTime(event.getStart_at().plusHours(2)));
        return eventsNextMatchDay.stream()
                .filter(eventNotEnded)
                .findFirst();
    }

    public Match getCurrentMatch() {
        updateCurrentMatch();
        return currentMatch;
    }
}
