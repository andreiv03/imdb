package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Production;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.List;

public class AddDeleteFavoritesAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();
      List<String> favoriteActors = currentUser.getFavoriteActors();
      List<String> favoriteProductions = currentUser.getFavoriteProductions();

      System.out.println("Favorites:");
      System.out.println("\tActors: " + (favoriteActors != null ? favoriteActors : "[]"));
      System.out.println("\tProductions: " + (favoriteProductions != null ? favoriteProductions :
        "[]"));
      System.out.println();

      System.out.println("What do you want to do?");
      System.out.println("\t1) Add");
      System.out.println("\t2) Delete");
      System.out.println("\t3) Exit");

      int action = promptForIntInRange(3);

      if (action == 3)
        return;

      System.out.println("What do you want to " + (action == 1 ? "add" : "delete") + "?");
      System.out.println("\t1) Actor");
      System.out.println("\t2) Production");
      System.out.println("\t3) Exit");

      int choice = promptForIntInRange(3);

      if (choice == 3)
        return;

      String name = promptForString("Enter the name: ");

      switch (choice) {
        case 1:
          if (action == 1 && favoriteActors != null && favoriteActors.contains(name)) {
            System.out.println("Actor already in favorites!");
            System.out.println();
            return;
          }

          for (Actor actor : Database.getInstance().getActors()) {
            if (actor.getName().equals(name)) {
              if (action == 1) {
                currentUser.addActorToFavorites(actor);
                return;
              }

              currentUser.removeActorFromFavorites(actor);
              return;
            }
          }

          System.out.println("Actor not found!");
          System.out.println();
          break;

        case 2:
          if (action == 1 && favoriteProductions != null && favoriteProductions.contains(name)) {
            System.out.println("Production already in favorites!");
            System.out.println();
            return;
          }

          for (Production production : Database.getInstance().getProductions()) {
            if (production.getTitle().equals(name)) {
              if (action == 1) {
                currentUser.addProductionToFavorites(production);
                return;
              }

              currentUser.removeProductionFromFavorites(production);
              return;
            }
          }

          System.out.println("Production not found!");
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
