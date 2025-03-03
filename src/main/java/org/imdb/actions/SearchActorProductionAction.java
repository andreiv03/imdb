package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Production;
import org.imdb.user.User;
import org.imdb.utils.Database;

public class SearchActorProductionAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();

      System.out.println("What are you searching for?");
      System.out.println("\t1) Actor");
      System.out.println("\t2) Movie");
      System.out.println("\t3) Series");
      System.out.println("\t4) Exit");

      int searchFor = promptForIntInRange(4);

      if (searchFor == 4)
        return;

      String name = promptForString("Enter the name: ");

      switch (searchFor) {
        case 1:
          for (Actor actor : Database.getInstance().getActors()) {
            if (actor.getName().equals(name)) {
              System.out.println(actor);
              return;
            }
          }

          System.out.println("Actor not found!");
          System.out.println();
          break;

        case 2:
          for (Production production : Database.getInstance().getProductions()) {
            if (production.getType().equals("Movie") && production.getTitle().equals(name)) {
              System.out.println(production);
              return;
            }
          }

          System.out.println("Movie not found!");
          System.out.println();
          break;

        case 3:
          for (Production production : Database.getInstance().getProductions()) {
            if (production.getType().equals("Series") && production.getTitle().equals(name)) {
              System.out.println(production);
              return;
            }
          }

          System.out.println("Series not found!");
          System.out.println();
          break;
      }
    } catch (InvalidOptionException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
