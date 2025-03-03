package org.imdb;

import org.imdb.user.User;
import org.imdb.utils.ActionFlow;
import org.imdb.utils.Auth;
import org.imdb.utils.Database;

public class IMDB {
  private static volatile IMDB instance = null;

  private IMDB() {
    if (instance != null) {
      throw new IllegalStateException("Instance already exists.");
    }
  }

  public static synchronized IMDB getInstance() {
    if (instance == null) {
      instance = new IMDB();
    }

    return instance;
  }

  public static void main(String[] args) {
    IMDB.getInstance().run();
  }

  public void run() {
    try {
      Database.getInstance().readFromDatabase();

      while (true) {
        User currentUser = Auth.getInstance().login();
        if (currentUser == null) {
          continue;
        }

        System.out.printf("%nWelcome back, %s!%n", currentUser.getUsername());
        System.out.printf("You are logged in as %s.%n%n", currentUser.getUserType());

        if (!ActionFlow.startFlow(currentUser)) {
          break;
        }
      }
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }
}
