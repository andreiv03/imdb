package org.imdb.actions;

import org.imdb.exceptions.InvalidOptionException;
import org.imdb.user.User;

import java.util.Scanner;

public abstract class Action {
  private static final Scanner scanner = new Scanner(System.in);

  public static int promptForIntInRange(int maximum) {
    System.out.print("Enter your option: ");
    int option = scanner.hasNextInt() ? scanner.nextInt() : -1;
    scanner.nextLine();
    System.out.println();

    if (option < 1 || option > maximum) {
      throw new InvalidOptionException("Invalid option.");
    }

    return option;
  }

  public static String promptForString(String message) {
    System.out.print(message);
    String option = scanner.nextLine();
    System.out.println();
    return option;
  }

  abstract public void execute(User currentUser);
}
