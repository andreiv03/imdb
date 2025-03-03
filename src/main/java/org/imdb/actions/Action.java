package org.imdb.actions;

import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Production;
import org.imdb.user.User;

import java.util.Scanner;

public abstract class Action {
  private static final Scanner scanner = new Scanner(System.in);

  public static int promptForIntInRange(String item, int maximum) {
    System.out.printf("Enter your %s: ", item);
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
    String option = scanner.nextLine().trim();
    System.out.println();
    return option;
  }

  public static String promptForConfirmation(String message) {
    System.out.println(message);
    System.out.println("\t1) Yes");
    System.out.println("\t2) No");
    System.out.println("\t3) Exit");

    int option = promptForIntInRange("option", 3);
    return switch (option) {
      case 1 -> "Yes";
      case 2 -> "No";
      default -> "Exit";
    };
  }

  abstract public void execute(User currentUser);

  @FunctionalInterface
  protected interface NumericFilter {
    boolean test(Production production, int value);
  }
}
