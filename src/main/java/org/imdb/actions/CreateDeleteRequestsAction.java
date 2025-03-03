package org.imdb.actions;

import org.imdb.actor.Actor;
import org.imdb.enumerations.RequestType;
import org.imdb.production.Production;
import org.imdb.request.Request;
import org.imdb.user.Regular;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateDeleteRequestsAction extends Action {
  @Override
  public void execute(User currentUser) {
    System.out.println("What do you want to do?");
    System.out.println("\t1) Create");
    System.out.println("\t2) Delete");
    System.out.println("\t3) Exit");

    int action = promptForIntInRange("option", 3);
    if (action == 3) {
      return;
    }

    if (action == 1) {
      createRequest(currentUser);
    }

    if (action == 2) {
      deleteRequest(currentUser);
    }
  }

  private void createRequest(User currentUser) {
    System.out.println("What type of request do you want to create?");
    System.out.println("\t1) Delete Account");
    System.out.println("\t2) Actor Issue");
    System.out.println("\t3) Production Issue");
    System.out.println("\t4) Others");
    System.out.println("\t5) Exit");

    int requestType = promptForIntInRange("option", 5);
    if (requestType == 5) {
      return;
    }

    String reason = promptForString("Enter the reason: ");
    String name = null;

    if (requestType == 2 || requestType == 3) {
      name = promptForString("Enter the name: ");
    }

    Request newRequest = new Request();
    newRequest.setCreatedDate(LocalDateTime.now());
    newRequest.setUsername(currentUser.getUsername());
    newRequest.setDescription(reason);

    switch (requestType) {
      case 1 -> deleteAccountRequest((Regular) currentUser, newRequest);
      case 2 -> actorIssueRequest(currentUser, newRequest, name);
      case 3 -> productionIssueRequest(currentUser, newRequest, name);
      case 4 -> otherRequest((Regular) currentUser, newRequest);
    }

    newRequest.registerObserver(currentUser);
    Database.getInstance().getRequests().add(newRequest);

    System.out.printf("Request created successfully.%n%n");
  }

  private void deleteAccountRequest(Regular user, Request request) {
    request.setType(RequestType.DELETE_ACCOUNT);
    request.setTo("ADMIN");
    user.createRequest(request);
  }

  private void actorIssueRequest(User currentUser, Request request, String actorName) {
    Optional<Actor> actorOptional = findActorByName(actorName);
    if (actorOptional.isEmpty()) {
      System.out.printf("Actor not found.%n%n");
      return;
    }

    Actor actor = actorOptional.get();
    if (actor.getResponsible().equals(currentUser.getUsername())) {
      System.out.printf("You are already responsible for this actor.%n%n");
      return;
    }

    request.setType(RequestType.ACTOR_ISSUE);
    request.setActorName(actorName);
    request.setTo(actor.getResponsible());

    notifyResponsibleUser(actor.getResponsible());
  }

  private void productionIssueRequest(User currentUser, Request request, String productionTitle) {
    Optional<Production> productionOptional = findProductionByTitle(productionTitle);
    if (productionOptional.isEmpty()) {
      System.out.printf("Production not found.%n%n");
      return;
    }

    Production production = productionOptional.get();
    if (production.getResponsible().equals(currentUser.getUsername())) {
      System.out.printf("You are already responsible for this production.%n%n");
      return;
    }

    request.setType(RequestType.MOVIE_ISSUE);
    request.setMovieTitle(productionTitle);
    request.setTo(production.getResponsible());

    notifyResponsibleUser(production.getResponsible());
  }

  private void otherRequest(Regular user, Request request) {
    request.setType(RequestType.OTHERS);
    request.setTo("ADMIN");
    user.createRequest(request);
  }

  private void deleteRequest(User currentUser) {
    List<Request> userRequests = getUserRequests(currentUser);

    if (userRequests.isEmpty()) {
      System.out.printf("You have no requests.%n%n");
      return;
    }

    System.out.printf("Your requests: %s%n%n", userRequests);

    String requestId = promptForString("Enter request ID: ");
    Optional<Request> requestOptional = findRequestById(requestId, userRequests);

    if (requestOptional.isEmpty()) {
      System.out.printf("Request not found.%n%n");
      return;
    }

    Request request = requestOptional.get();
    Database.getInstance().getRequests().remove(request);

    if (request.getTo().equals("ADMIN")) {
      ((Regular) currentUser).removeRequest(request);
    }

    System.out.printf("Request deleted successfully.%n%n");
  }

  private List<Request> getUserRequests(User currentUser) {
    return Database.getInstance()
      .getRequests()
      .stream()
      .filter(request -> request.getUsername().equals(currentUser.getUsername()))
      .collect(Collectors.toList());
  }

  private Optional<Request> findRequestById(String requestId, List<Request> requests) {
    return requests.stream().filter(request -> request.getId().equals(requestId)).findFirst();
  }

  private Optional<Actor> findActorByName(String name) {
    return Database.getInstance()
      .getActors()
      .stream()
      .filter(actor -> actor.getName().equalsIgnoreCase(name))
      .findFirst();
  }

  private Optional<Production> findProductionByTitle(String title) {
    return Database.getInstance()
      .getProductions()
      .stream()
      .filter(production -> production.getTitle().equalsIgnoreCase(title))
      .findFirst();
  }

  private void notifyResponsibleUser(String responsibleUsername) {
    String message = "Someone just made a request that you are responsible for.";
    Database.getInstance()
      .getUsers()
      .stream()
      .filter(user -> user.getUsername().equals(responsibleUsername))
      .findFirst()
      .ifPresent(responsibleUser -> responsibleUser.getNotifications().add(message));
  }
}
