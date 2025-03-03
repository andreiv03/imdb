package org.imdb.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import org.imdb.actor.Actor;
import org.imdb.production.Production;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;
import org.imdb.user.User;

import java.util.List;

import static org.imdb.utils.JsonUtils.readArrayFromJson;

@Getter
@Setter
public class Database {
  private static volatile Database instance = null;

  private List<User> users;
  private List<Actor> actors;
  private List<Production> productions;
  private List<Request> requests;

  private Database() {
    if (instance != null) {
      throw new IllegalStateException("Instance already exists.");
    }
  }

  public static synchronized Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }

    return instance;
  }

  public void readFromDatabase() {
    String inputPath = "src/main/resources/";

    // @formatter:off
    setUsers(readArrayFromJson(inputPath + "accounts.json", new TypeReference<>() {}));
    setActors(readArrayFromJson(inputPath + "actors.json", new TypeReference<>() {}));
    setProductions(readArrayFromJson(inputPath + "productions.json", new TypeReference<>() {}));
    setRequests(readArrayFromJson(inputPath + "requests.json", new TypeReference<>() {}));
    // @formatter:on

    if (!users.isEmpty()) {
      users.forEach(this::assignContributions);
    }

    if (!productions.isEmpty()) {
      productions.forEach(this::processProductionData);
    }

    if (!requests.isEmpty()) {
      requests.stream()
        .filter(request -> request.getTo().equals("ADMIN"))
        .forEach(RequestsHolder::addRequest);
    }
  }

  private void assignContributions(User user) {
    if (user.getActorsContribution() != null) {
      user.getActorsContribution()
        .forEach(actorName -> actors.stream()
          .filter(actor -> actor.getName().equals(actorName))
          .findFirst()
          .ifPresent(actor -> actor.setResponsible(user.getUsername())));
    }

    if (user.getProductionsContribution() != null) {
      user.getProductionsContribution()
        .forEach(productionTitle -> productions.stream()
          .filter(production -> production.getTitle().equals(productionTitle))
          .findFirst()
          .ifPresent(production -> {
            production.setResponsible(user.getUsername());
            production.registerObserver(user);
          }));
    }
  }

  private void processProductionData(Production production) {
    addMissingActors(production);
    assignPerformances(production);
    updateUserReviewedProductions(production);
  }

  private void addMissingActors(Production production) {
    production.getActors().forEach(actorName -> {
      if (actors.stream().noneMatch(actor -> actor.getName().equals(actorName))) {
        Actor newActor = new Actor();
        newActor.setName(actorName);
        actors.add(newActor);
      }
    });
  }

  private void assignPerformances(Production production) {
    production.getActors()
      .forEach(actorName -> actors.stream()
        .filter(actor -> actor.getName().equals(actorName))
        .forEach(actor -> {
          Actor.Performance performance = new Actor.Performance();
          performance.setTitle(production.getTitle());
          performance.setType(production.getType());
          actor.getPerformances().add(performance);
        }));
  }

  private void updateUserReviewedProductions(Production production) {
    production.getRatings().forEach(rating -> {
      users.stream()
        .filter(user -> user.getUsername().equals(rating.getUsername()))
        .forEach(user -> user.getReviewedProductions().add(rating));

      rating.setProductionTitle(production.getTitle());
    });
  }
}
