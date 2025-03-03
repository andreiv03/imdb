package org.imdb.actions;

import org.imdb.actor.Actor;
import org.imdb.production.Production;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.Optional;

public class SearchActorProductionAction extends Action {
  @Override
  public void execute(User currentUser) {
    System.out.println("What are you searching for?");
    System.out.println("\t1) Actor");
    System.out.println("\t2) Movie");
    System.out.println("\t3) Series");
    System.out.println("\t4) Exit");

    int option = promptForIntInRange("option", 4);
    if (option == 4) {
      return;
    }

    String name = promptForString("Enter the name: ");
    switch (option) {
      case 1 -> searchActor(name);
      case 2 -> searchProduction(name, "Movie");
      case 3 -> searchProduction(name, "Series");
    }
  }

  private void searchActor(String name) {
    Optional<Actor> actor = Database.getInstance()
      .getActors()
      .stream()
      .filter(a -> a.getName().equalsIgnoreCase(name))
      .findFirst();

    if (actor.isEmpty()) {
      System.out.printf("Actor %s not found.%n%n", name);
      return;
    }

    System.out.printf("%s%n%n", actor.get());
  }

  private void searchProduction(String title, String type) {
    Optional<Production> production = Database.getInstance()
      .getProductions()
      .stream()
      .filter(p -> p.getType().equalsIgnoreCase(type))
      .filter(p -> p.getTitle().equalsIgnoreCase(title))
      .findFirst();

    if (production.isEmpty()) {
      System.out.printf("Actor %s not found.%n%n", title);
      return;
    }

    System.out.printf("%s%n%n", production.get());
  }
}
