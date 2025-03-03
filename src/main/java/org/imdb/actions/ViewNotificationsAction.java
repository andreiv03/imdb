package org.imdb.actions;

import org.imdb.user.User;

import java.util.List;

public class ViewNotificationsAction extends Action {
  @Override
  public void execute(User currentUser) {
    List<String> notifications = currentUser.getNotifications();
    System.out.println("Notifications:");

    if (notifications == null || notifications.isEmpty()) {
      System.out.printf("You have no notifications.%n%n");
      return;
    }

    for (int index = 0; index < notifications.size(); index++) {
      System.out.printf("%d) %s%n", index + 1, notifications.get(index));
    }

    System.out.println();
  }
}
