package org.imdb.actions;

import org.imdb.actor.Actor;
import org.imdb.production.Production;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.List;
import java.util.Optional;

public class AddDeleteFavoritesAction extends Action {
  @Override
  public void execute(User currentUser) {
    List<String> favoriteActors = currentUser.getFavoriteActors();
    List<String> favoriteProductions = currentUser.getFavoriteProductions();

    System.out.println("Favorites:");
    System.out.printf("\tActors: %s%n", favoriteActors);
    System.out.printf("\tProductions: %s%n%n", favoriteProductions);

    System.out.println("What do you want to do?");
    System.out.println("\t1) Add");
    System.out.println("\t2) Delete");
    System.out.println("\t3) Exit");

    int action = promptForIntInRange("option", 3);
    if (action == 3) {
      return;
    }

    System.out.printf("What do you want to %s?%n", action == 1 ? "add" : "delete");
    System.out.println("\t1) Actor");
    System.out.println("\t2) Production");
    System.out.println("\t3) Exit");

    int item = promptForIntInRange("option", 3);
    if (item == 3) {
      return;
    }

    String name = promptForString("Enter the name: ");

    if (item == 1) {
      modifyFavoriteActors(currentUser, name, action == 1);
    }

    if (item == 2) {
      modifyFavoriteProductions(currentUser, name, action == 1);
    }
  }

  private void modifyFavoriteActors(User currentUser, String name, boolean isAdding) {
    List<String> favoriteActors = currentUser.getFavoriteActors();

    if (isAdding && favoriteActors != null && favoriteActors.contains(name)) {
      System.out.printf("Actor is already in favorites.%n%n");
      return;
    }

    Optional<Actor> actor = Database.getInstance()
      .getActors()
      .stream()
      .filter(a -> a.getName().equalsIgnoreCase(name))
      .findFirst();

    if (actor.isEmpty()) {
      System.out.printf("Actor not found.%n%n");
      return;
    }

    if (isAdding) {
      currentUser.addActorToFavorites(actor.get());
    } else {
      currentUser.removeActorFromFavorites(actor.get());
    }
  }

  private void modifyFavoriteProductions(User currentUser, String name, boolean isAdding) {
    List<String> favoriteProductions = currentUser.getFavoriteProductions();

    if (isAdding && favoriteProductions != null && favoriteProductions.contains(name)) {
      System.out.printf("Production is already in favorites.%n%n");
      return;
    }

    Optional<Production> production = Database.getInstance()
      .getProductions()
      .stream()
      .filter(p -> p.getTitle().equalsIgnoreCase(name))
      .findFirst();

    if (production.isEmpty()) {
      System.out.printf("Production not found.%n%n");
      return;
    }

    if (isAdding) {
      currentUser.addProductionToFavorites(production.get());
    } else {
      currentUser.removeProductionFromFavorites(production.get());
    }
  }
}
