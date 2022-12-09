package be.brigandze;

import be.brigandze.control.EventController;
import be.brigandze.control.Match;
import be.brigandze.entity.Event;
import be.brigandze.entity.Lineups;
import be.brigandze.sporteasy.SportEasyResource;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@QuarkusMain
public class Main {

  public static void main(String... args) {
    System.out.println("Running main method");
    Quarkus.run(OpstellingOphalen.class, args);
  }

  public static class OpstellingOphalen implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
      System.out.println("STARTING MAIN");
      EventController eventController = EventController.getInstance();
      Match currentMatch = eventController.getCurrentMatch();

      SportEasyResource sportEasyInstance = SportEasyResource.getSportEasyInstance();

      String lineUpData =
          sportEasyInstance.getLineUp(currentMatch.getTeamId(), currentMatch.getId());

      Event event = sportEasyInstance.getMatchData(currentMatch.getTeamId(), currentMatch.getId());
      String liveStats = sportEasyInstance.getLiveStats(event);


      Quarkus.asyncExit();
      return 0;
    }
  }
}
