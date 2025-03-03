package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.enumerations.RequestStatus;
import org.imdb.enumerations.RequestType;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;
import org.imdb.strategies.RequestApprovalStrategy;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewSolveRequestsAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();

      System.out.println("What do you want to do?");
      System.out.println("\t1) View");
      System.out.println("\t2) Solve");
      System.out.println("\t3) Exit");

      int action = promptForIntInRange(3);

      if (action == 3)
        return;

      List<Request> adminRequests = RequestsHolder.getRequests();
      System.out.println("Admin requests: " + adminRequests);
      System.out.println();

      ArrayList<Request> responsibleRequests = Database.getInstance()
        .getRequests()
        .stream()
        .filter(request -> request.getTo().equals(currentUser.getUsername()))
        .collect(Collectors.toCollection(ArrayList::new));
      System.out.println("Requests you are responsible for: " + responsibleRequests);
      System.out.println();

      switch (action) {
        case 1:
          break;

        case 2:
          String id = promptForString("Enter request id: ");
          Request request = responsibleRequests.stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .orElse(null);

          if (request == null)
            request = adminRequests.stream()
              .filter(r -> r.getId().equals(id))
              .findFirst()
              .orElse(null);

          if (request == null) {
            System.out.println("Request not found!");
            System.out.println();
            return;
          }

          System.out.println("What do you want to do?");
          System.out.println("\t1) Approve");
          System.out.println("\t2) Reject");
          System.out.println("\t3) Exit");

          int actionTwo = promptForIntInRange(3);

          if (actionTwo == 3)
            return;

          if (actionTwo == 1) {
            request.setStatus(RequestStatus.APPROVED);

            if (request.getType().equals(RequestType.ACTOR_ISSUE) || request.getType()
              .equals(RequestType.MOVIE_ISSUE)) {
              Request finalRequest = request;
              Database.getInstance()
                .getUsers()
                .stream()
                .filter(user -> user.getUsername().equals(finalRequest.getUsername()))
                .findFirst()
                .ifPresent(creatorUser -> creatorUser.updateExperience(new RequestApprovalStrategy()));
            }
          }

          if (actionTwo == 2)
            request.setStatus(RequestStatus.REJECTED);

          request.notifyObservers();
          Database.getInstance().getRequests().remove(request);

          if (request.getTo().equals("ADMIN"))
            RequestsHolder.removeRequest(request);
      }
    } catch (InvalidOptionException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
