package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.enumerations.RequestType;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Production;
import org.imdb.request.Request;
import org.imdb.user.Regular;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CreateDeleteRequestsAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();

      System.out.println("What do you want to do?");
      System.out.println("\t1) Create");
      System.out.println("\t2) Delete");
      System.out.println("\t3) Exit");

      int action = promptForIntInRange(3);

      if (action == 3)
        return;

      switch (action) {
        case 1:
          System.out.println("What type of request do you want to create?");
          System.out.println("\t1) Delete account");
          System.out.println("\t2) Actor issue");
          System.out.println("\t3) Production issue");
          System.out.println("\t4) Others");
          System.out.println("\t5) Exit");

          int requestType = promptForIntInRange(5);

          if (requestType == 5)
            return;

          String reason = promptForString("Enter the reason: ");
          String name;

          if (requestType == 2 || requestType == 3)
            name = promptForString("Enter the name: ");
          else
            name = null;

          Request newRequest = new Request();
          newRequest.setCreatedDate(LocalDateTime.now());
          newRequest.setUsername(currentUser.getUsername());
          newRequest.setDescription(reason);

          switch (requestType) {
            case 1:
              newRequest.setType(RequestType.DELETE_ACCOUNT);
              newRequest.setTo("ADMIN");
              ((Regular) currentUser).createRequest(newRequest);
              break;

            case 2:
              Actor actor = Database.getInstance()
                .getActors()
                .stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);

              if (actor == null) {
                System.out.println("Actor not found!");
                System.out.println();
                return;
              }

              if (actor.getResponsible().equals(currentUser.getUsername())) {
                System.out.println("You are already responsible for this actor!");
                System.out.println();
                return;
              }

              newRequest.setType(RequestType.ACTOR_ISSUE);
              newRequest.setActorName(name);
              newRequest.setTo(actor.getResponsible());

              Database.getInstance()
                .getUsers()
                .stream()
                .filter(user -> user.getUsername().equals(actor.getResponsible()))
                .findFirst()
                .ifPresent(responsibleUser -> responsibleUser.getNotifications()
                  .add("Someone just made a request that you are responsible for!"));

              break;

            case 3:
              Production production = Database.getInstance()
                .getProductions()
                .stream()
                .filter(p -> p.getTitle().equals(name))
                .findFirst()
                .orElse(null);

              if (production == null) {
                System.out.println("Production not found!");
                System.out.println();
                return;
              }

              if (production.getResponsible().equals(currentUser.getUsername())) {
                System.out.println("You are already responsible for this production!");
                System.out.println();
                return;
              }

              newRequest.setType(RequestType.MOVIE_ISSUE);
              newRequest.setMovieTitle(name);
              newRequest.setTo(production.getResponsible());

              Database.getInstance()
                .getUsers()
                .stream()
                .filter(user -> user.getUsername().equals(production.getResponsible()))
                .findFirst()
                .ifPresent(responsibleUser -> responsibleUser.getNotifications()
                  .add("Someone just made a request that you are responsible for!"));

              break;

            case 4:
              newRequest.setType(RequestType.OTHERS);
              newRequest.setTo("ADMIN");
              ((Regular) currentUser).createRequest(newRequest);
              break;
          }

          newRequest.registerObserver(currentUser);
          Database.getInstance().getRequests().add(newRequest);
          break;

        case 2:
          ArrayList<Request> userRequests = Database.getInstance()
            .getRequests()
            .stream()
            .filter(request -> request.getUsername().equals(currentUser.getUsername()))
            .collect(Collectors.toCollection(ArrayList::new));

          if (userRequests.isEmpty()) {
            System.out.println("You have no requests!");
            System.out.println();
            return;
          }

          System.out.println("Requests: " + userRequests);
          System.out.println();

          String id = promptForString("Enter request id: ");
          Request request = userRequests.stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .orElse(null);

          if (request == null) {
            System.out.println("Request not found!");
            System.out.println();
            return;
          }

          Database.getInstance().getRequests().remove(request);

          if (request.getTo().equals("ADMIN"))
            ((Regular) currentUser).removeRequest(request);
      }
    } catch (InvalidOptionException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
