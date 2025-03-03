package org.imdb.user;

import org.imdb.actor.Actor;
import org.imdb.production.Production;
import org.imdb.utils.Database;

public class Staff extends User {
  public Staff() {
    super();
  }

  public Staff(User user) {
    super(user);
  }

  public void addProductionSystem(Production production) {
    if (production == null) {
      throw new IllegalArgumentException("Production cannot be null.");
    }

    Database.getInstance().getProductions().add(production);
  }

  public void addActorSystem(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    Database.getInstance().getActors().add(actor);
  }

  public void removeProductionSystem(String title) {
    Database.getInstance()
      .getProductions()
      .removeIf(production -> production.getTitle().equals(title));
  }

  public void removeActorSystem(String name) {
    Database.getInstance().getActors().removeIf(actor -> actor.getName().equals(name));
  }

  public void updateProductionSystem(Production production) {
    if (production == null) {
      throw new IllegalArgumentException("Production cannot be null.");
    }

    Database database = Database.getInstance();
    database.getProductions().removeIf(p -> p.getTitle().equals(production.getTitle()));
    database.getProductions().add(production);
  }

  public void updateActorSystem(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    Database database = Database.getInstance();
    database.getActors().removeIf(a -> a.getName().equals(actor.getName()));
    database.getActors().add(actor);
  }

  @Override
  public void receiveNotification(String notification) {
    addNotification(notification);
  }
}
