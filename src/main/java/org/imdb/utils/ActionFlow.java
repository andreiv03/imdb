package org.imdb.utils;

import org.imdb.actions.Action;
import org.imdb.actions.AddDeleteActorsProductionsAction;
import org.imdb.actions.AddDeleteFavoritesAction;
import org.imdb.actions.AddDeleteReviewsAction;
import org.imdb.actions.AddDeleteUsersAction;
import org.imdb.actions.CreateDeleteRequestsAction;
import org.imdb.actions.SearchActorProductionAction;
import org.imdb.actions.UpdateActorsProductionsAction;
import org.imdb.actions.ViewActorsAction;
import org.imdb.actions.ViewNotificationsAction;
import org.imdb.actions.ViewProductionsAction;
import org.imdb.actions.ViewSolveRequestsAction;
import org.imdb.enumerations.AccountType;
import org.imdb.exceptions.InvalidCommandException;
import org.imdb.user.User;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.imdb.enumerations.AccountType.ADMIN;
import static org.imdb.enumerations.AccountType.CONTRIBUTOR;
import static org.imdb.enumerations.AccountType.REGULAR;

public class ActionFlow {
  private static final List<AbstractMap.SimpleEntry<String, Set<AccountType>>> ACTIONS =
    new ArrayList<>();

  static {
    addAction("View productions details", REGULAR, CONTRIBUTOR, ADMIN);
    addAction("View actors details", REGULAR, CONTRIBUTOR, ADMIN);
    addAction("View notifications", REGULAR, CONTRIBUTOR, ADMIN);
    addAction("Search for actor/movie/series", REGULAR, CONTRIBUTOR, ADMIN);
    addAction("Add/Delete actor/movie/series to/from favorites", REGULAR, CONTRIBUTOR, ADMIN);
    addAction("Create/Delete request", REGULAR, CONTRIBUTOR);
    addAction("Add/Delete actor/movie/series from system", CONTRIBUTOR, ADMIN);
    addAction("View/Solve request", CONTRIBUTOR, ADMIN);
    addAction("Update actor/movie/series details", CONTRIBUTOR, ADMIN);
    addAction("Add/Delete review to/from actor/movie/series", REGULAR);
    addAction("Add/Delete user from system", ADMIN);
    addAction("Logout", REGULAR, CONTRIBUTOR, ADMIN);
  }

  private ActionFlow() {
    throw new UnsupportedOperationException("Cannot instantiate utility class.");
  }

  private static void addAction(String name, AccountType... accountTypes) {
    ACTIONS.add(new AbstractMap.SimpleEntry<>(name, EnumSet.copyOf(List.of(accountTypes))));
  }

  private static void executeAction(int index, User currentUser) {
    switch (index) {
      case 0 -> new ViewProductionsAction().execute(currentUser);
      case 1 -> new ViewActorsAction().execute(currentUser);
      case 2 -> new ViewNotificationsAction().execute(currentUser);
      case 3 -> new SearchActorProductionAction().execute(currentUser);
      case 4 -> new AddDeleteFavoritesAction().execute(currentUser);
      case 5 -> new CreateDeleteRequestsAction().execute(currentUser);
      case 6 -> new AddDeleteActorsProductionsAction().execute(currentUser);
      case 7 -> new ViewSolveRequestsAction().execute(currentUser);
      case 8 -> new UpdateActorsProductionsAction().execute(currentUser);
      case 9 -> new AddDeleteReviewsAction().execute(currentUser);
      case 10 -> new AddDeleteUsersAction().execute(currentUser);
      default -> throw new InvalidCommandException("Invalid action.");
    }
  }

  public static boolean startFlow(User currentUser) {
    while (currentUser.isLoggedIn()) {
      Map<Integer, Integer> actionMap = displayActions(currentUser);
      int action = Action.promptForIntInRange(ACTIONS.size());

      if (!actionMap.containsKey(action)) {
        throw new InvalidCommandException("Invalid command.");
      }

      int actionIndex = actionMap.get(action);
      if (actionIndex == ACTIONS.size() - 1) {
        return Auth.getInstance().logout(currentUser);
      }

      executeAction(actionIndex, currentUser);
    }

    return true;
  }

  private static Map<Integer, Integer> displayActions(User currentUser) {
    System.out.printf("Your current experience: %s%n", currentUser.getExperience());
    System.out.println("What would you like to do next?");

    Map<Integer, Integer> actionMap = new HashMap<>();
    int displayedIndex = 1;

    for (int index = 0; index < ACTIONS.size(); index++) {
      if (ACTIONS.get(index).getValue().contains(currentUser.getUserType())) {
        actionMap.put(displayedIndex, index);
        System.out.printf("\t%d) %s%n", displayedIndex, ACTIONS.get(index).getKey());
        displayedIndex++;
      }
    }

    return actionMap;
  }
}
