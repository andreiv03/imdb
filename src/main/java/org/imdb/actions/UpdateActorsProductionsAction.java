package org.imdb.actions;

import org.imdb.actor.Actor;
import org.imdb.production.Production;
import org.imdb.user.Staff;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.Optional;

public class UpdateActorsProductionsAction extends Action {
  @Override
  public void execute(User currentUser) {
    System.out.println("What do you want to update?");
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
      case 1 -> updateActor(currentUser, name);
      case 2 -> updateProduction(currentUser, name, "Movie");
      case 3 -> updateProduction(currentUser, name, "Series");
    }
  }

  private void updateActor(User currentUser, String name) {
    Optional<Actor> actorOptional = findActorByName(name);
    if (actorOptional.isEmpty()) {
      System.out.print("Actor not found.%n%n");
      return;
    }

    Actor actor = actorOptional.get();
    System.out.println("What do you want to update?");
    System.out.println("\t1) Name");
    System.out.println("\t2) Biography");
    System.out.println("\t3) Exit");

    int option = promptForIntInRange("option", 3);
    if (option == 3) {
      return;
    }

    switch (option) {
      case 1 -> actor.setName(promptForString("Enter the new name: "));
      case 2 -> actor.setBiography(promptForString("Enter the new biography: "));
    }

    ((Staff) currentUser).updateActorSystem(actor);
    System.out.printf("Actor updated successfully.%n%n");
  }

  private void updateProduction(User currentUser, String title, String type) {
    Optional<Production> productionOptional = findProductionByTitleAndType(title, type);
    if (productionOptional.isEmpty()) {
      System.out.printf("%s not found.%n%n", type);
      return;
    }

    Production production = productionOptional.get();
    System.out.println("What do you want to update?");
    System.out.println("\t1) Title");
    System.out.println("\t2) Plot");
    System.out.println("\t3) Exit");

    int option = promptForIntInRange("option", 3);
    if (option == 3) {
      return;
    }

    switch (option) {
      case 1 -> production.setTitle(promptForString("Enter the new title: "));
      case 2 -> production.setPlot(promptForString("Enter the new plot: "));
    }

    ((Staff) currentUser).updateProductionSystem(production);
    System.out.printf("%s updated successfully.%n%n", type);
  }

  private Optional<Actor> findActorByName(String name) {
    return Database.getInstance()
      .getActors()
      .stream()
      .filter(actor -> actor.getName().equalsIgnoreCase(name))
      .findFirst();
  }

  private Optional<Production> findProductionByTitleAndType(String title, String type) {
    return Database.getInstance()
      .getProductions()
      .stream()
      .filter(production -> production.getType().equalsIgnoreCase(type))
      .filter(production -> production.getTitle().equalsIgnoreCase(title))
      .findFirst();
  }
}
