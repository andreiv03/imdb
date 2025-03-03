package org.imdb.actions;

import org.imdb.enumerations.RequestStatus;
import org.imdb.enumerations.RequestType;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;
import org.imdb.strategies.RequestApprovalStrategy;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ViewSolveRequestsAction extends Action {
  @Override
  public void execute(User currentUser) {
    System.out.println("What do you want to do?");
    System.out.println("\t1) View");
    System.out.println("\t2) Solve");
    System.out.println("\t3) Exit");

    int option = promptForIntInRange("option", 3);
    if (option == 3) {
      return;
    }

    List<Request> adminRequests = RequestsHolder.getRequests();
    List<Request> responsibleRequests = Database.getInstance()
      .getRequests()
      .stream()
      .filter(request -> request.getTo().equals(currentUser.getUsername()))
      .toList();

    if (option == 1) {
      displayRequests("Admin requests", adminRequests);
      displayRequests("Requests you are responsible for", responsibleRequests);
    }

    if (option == 2) {
      solveRequest(adminRequests, responsibleRequests);
    }
  }

  private void displayRequests(String title, List<Request> requests) {
    System.out.printf("%s: %s%n%n", title, requests);
  }

  private void solveRequest(List<Request> adminRequests, List<Request> responsibleRequests) {
    String requestId = promptForString("Enter request ID: ");
    Optional<Request> requestOptional = findRequestById(requestId, responsibleRequests,
      adminRequests);

    if (requestOptional.isEmpty()) {
      System.out.printf("Request not found.%n%n");
      return;
    }

    Request request = requestOptional.get();
    System.out.println("What do you want to do?");
    System.out.println("\t1) Approve");
    System.out.println("\t2) Reject");
    System.out.println("\t3) Exit");

    int option = promptForIntInRange("option", 3);
    if (option == 3) {
      return;
    }

    if (option == 1) {
      approveRequest(request);
    }

    if (option == 2) {
      rejectRequest(request);
    }

    request.notifyObservers();
    Database.getInstance().getRequests().remove(request);

    if (request.getTo().equals("ADMIN")) {
      RequestsHolder.removeRequest(request);
    }
  }

  @SafeVarargs
  private Optional<Request> findRequestById(String requestId, List<Request>... requestLists) {
    return Stream.of(requestLists)
      .flatMap(List::stream)
      .filter(request -> request.getId().equals(requestId))
      .findFirst();
  }

  private void approveRequest(Request request) {
    request.setStatus(RequestStatus.APPROVED);
    if (request.getType() == RequestType.ACTOR_ISSUE || request.getType() == RequestType.MOVIE_ISSUE) {
      Database.getInstance()
        .getUsers()
        .stream()
        .filter(user -> user.getUsername().equals(request.getUsername()))
        .findFirst()
        .ifPresent(user -> user.updateExperience(new RequestApprovalStrategy()));
    }
  }

  private void rejectRequest(Request request) {
    request.setStatus(RequestStatus.REJECTED);
  }
}
