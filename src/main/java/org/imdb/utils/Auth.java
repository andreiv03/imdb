package org.imdb.utils;

import org.imdb.actions.Action;
import org.imdb.user.Credentials;
import org.imdb.user.User;
import org.imdb.user.UserFactory;

import java.util.Optional;
import java.util.Scanner;

public class Auth {
  private static volatile Auth instance = null;

  private final Scanner scanner = new Scanner(System.in);

  private Auth() {
    if (instance != null) {
      throw new IllegalStateException("Instance already exists.");
    }
  }

  public static synchronized Auth getInstance() {
    if (instance == null) {
      instance = new Auth();
    }

    return instance;
  }

  public User login() {
    System.out.println("Enter your credentials:");
    System.out.print("\temail: ");
    String email = scanner.nextLine().trim();

    Optional<User> userOptional = Database.getInstance()
      .getUsers()
      .stream()
      .filter(user -> user.getInformation().getCredentials().getEmail().equals(email))
      .findFirst();

    if (userOptional.isEmpty()) {
      System.out.printf("%nNo account found with this email.%n");
      return null;
    }

    User user = userOptional.get();
    Credentials credentials = user.getInformation().getCredentials();

    int attempts = 0;
    final int MAX_ATTEMPTS = 3;

    while (attempts < MAX_ATTEMPTS) {
      System.out.print("\tpassword: ");
      String password = scanner.nextLine().trim();

      if (credentials.getPassword().equals(password)) {
        User currentUser = UserFactory.createUser(user);
        currentUser.setLoggedIn(true);
        return currentUser;
      }

      attempts++;

      if (attempts < MAX_ATTEMPTS) {
        System.out.printf("%nWrong password! Attempts left: %d%n", MAX_ATTEMPTS - attempts);
      }
    }

    System.out.printf("%nToo many failed attempts. Please try again later.%n");
    return null;
  }

  public boolean logout(User currentUser) {
    currentUser.setLoggedIn(false);
    System.out.println("You have been logged out successfully.");

    System.out.println("What would you like to do next?");
    System.out.println("\t1) Log in again");
    System.out.println("\t2) Exit");

    int action = Action.promptForIntInRange("option", 2);
    return action == 1;
  }
}
