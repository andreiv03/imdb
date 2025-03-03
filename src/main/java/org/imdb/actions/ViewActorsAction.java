package org.imdb.actions;

import org.imdb.actor.Actor;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.ArrayList;
import java.util.List;

public class ViewActorsAction extends Action {
  @Override
  public void execute(User currentUser) {
    List<Actor> actors = new ArrayList<>(Database.getInstance().getActors());

    String sortOption = promptForConfirmation("Do you want to sort the actors?");
    if (sortOption.equals("Exit")) {
      return;
    }

    if (sortOption.equals("Yes")) {
      actors.sort(null);
    }

    System.out.printf("Productions: %s%n%n", actors);
  }
}
