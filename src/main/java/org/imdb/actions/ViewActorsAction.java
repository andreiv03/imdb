package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.ArrayList;

public class ViewActorsAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();
      ArrayList<Actor> actors = new ArrayList<>(Database.getInstance().getActors());

      System.out.println("Do you want to sort the actors?");
      System.out.println("\t1) Yes");
      System.out.println("\t2) No");
      System.out.println("\t3) Exit");

      int canSortActors = promptForIntInRange(3);

      if (canSortActors == 3)
        return;

      if (canSortActors == 1)
        actors.sort(null);

      System.out.println("Actors:");

      for (Actor actor : actors)
        System.out.println(actor);
    } catch (InvalidOptionException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
