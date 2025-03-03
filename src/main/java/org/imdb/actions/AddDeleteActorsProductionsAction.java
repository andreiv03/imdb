package org.imdb.actions;

import org.imdb.actor.Actor;
import org.imdb.enumerations.AccountType;
import org.imdb.production.Movie;
import org.imdb.production.Production;
import org.imdb.production.Series;
import org.imdb.strategies.ActorAdditionStrategy;
import org.imdb.strategies.ProductionAdditionStrategy;
import org.imdb.user.Staff;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.List;
import java.util.Optional;

public class AddDeleteActorsProductionsAction extends Action {
  @Override
  public void execute(User currentUser) {
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
    System.out.println("\t2) Movie");
    System.out.println("\t3) Series");
    System.out.println("\t4) Exit");

    int item = promptForIntInRange("option", 4);
    if (item == 4) {
      return;
    }

    if (item == 1) {
      List<Actor> actors = Database.getInstance().getActors();
      System.out.printf("Actors: %s%n%n", actors);
    }

    if (item == 2) {
      List<Production> movies = Database.getInstance()
        .getProductions()
        .stream()
        .filter(production -> production.getType().equals("Movie"))
        .toList();
      System.out.printf("Movies: %s%n%n", movies);
    }

    if (item == 3) {
      List<Production> series = Database.getInstance()
        .getProductions()
        .stream()
        .filter(production -> production.getType().equals("Series"))
        .toList();
      System.out.printf("Series: %s%n%n", series);
    }

    String name = promptForString("Enter the name: ");

    switch (item) {
      case 1 -> processActor(currentUser, name, action == 1);
      case 2 -> processProduction(currentUser, name, "Movie", action == 1);
      case 3 -> processProduction(currentUser, name, "Series", action == 1);
    }
  }

  private void processActor(User currentUser, String name, boolean isAdding) {
    Optional<Actor> actorOptional = findActorByName(name);

    if (isAdding) {
      if (actorOptional.isPresent()) {
        System.out.printf("Actor already exists.%n%n");
        return;
      }

      Actor newActor = new Actor();
      newActor.setName(name);
      newActor.setResponsible(currentUser.getUsername());
      ((Staff) currentUser).addActorSystem(newActor);
      currentUser.updateExperience(new ActorAdditionStrategy());

      System.out.printf("Actor %s has been added.%n%n", name);
      return;
    }

    if (actorOptional.isEmpty()) {
      System.out.printf("Actor not found.%n%n");
      return;
    }

    Actor actor = actorOptional.get();
    if (isUnauthorizedToModify(currentUser, actor.getResponsible())) {
      return;
    }

    ((Staff) currentUser).removeActorSystem(name);
    System.out.printf("Actor %s has been removed.%n%n", name);
  }

  private void processProduction(User currentUser, String name, String type, boolean isAdding) {
    Optional<Production> productionOptional = findProductionByTitleAndType(name, type);

    if (isAdding) {
      if (productionOptional.isPresent()) {
        System.out.printf("%s already exists.%n%n", type);
        return;
      }

      Production newProduction = type.equals("Movie") ? new Movie() : new Series();
      newProduction.setTitle(name);
      newProduction.setResponsible(currentUser.getUsername());
      newProduction.registerObserver(currentUser);
      ((Staff) currentUser).addProductionSystem(newProduction);
      currentUser.updateExperience(new ProductionAdditionStrategy());

      System.out.printf("%s %s has been added.%n%n", type, name);
      return;
    }

    if (productionOptional.isEmpty()) {
      System.out.printf("%s not found.%n%n", type);
      return;
    }

    Production production = productionOptional.get();
    if (isUnauthorizedToModify(currentUser, production.getResponsible())) {
      return;
    }

    ((Staff) currentUser).removeProductionSystem(name);
    System.out.printf("%s %s has been removed.%n%n", type, name);
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

  private boolean isUnauthorizedToModify(User currentUser, String responsible) {
    if (responsible.equals("ADMIN") && !currentUser.getUserType().equals(AccountType.ADMIN)) {
      System.out.printf("You are not an admin.%n%n");
      return true;
    }

    if (!responsible.equals(currentUser.getUsername())) {
      System.out.printf("You are not responsible for this item.%n%n");
      return true;
    }

    return false;
  }
}
