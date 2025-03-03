package org.imdb.actions;

import org.imdb.enumerations.AccountType;
import org.imdb.exceptions.InformationIncompleteException;
import org.imdb.user.Credentials;
import org.imdb.user.User;
import org.imdb.user.UserFactory;
import org.imdb.utils.Database;
import org.imdb.utils.Generator;
import org.imdb.utils.JsonUtils;

import java.util.Optional;

public class AddDeleteUsersAction extends Action {
  @Override
  public void execute(User currentUser) {
    System.out.printf("Users: %s%n%n", JsonUtils.toJson(Database.getInstance().getUsers()));

    System.out.println("What do you want to do?");
    System.out.println("\t1) Add");
    System.out.println("\t2) Delete");
    System.out.println("\t3) Exit");

    int action = promptForIntInRange("option", 3);
    if (action == 3) {
      return;
    }

    String name = promptForString("Enter the " + (action == 1 ? "full name" : "username") + ": ");

    if (action == 1) {
      addUser(name);
    }

    if (action == 2) {
      deleteUser(currentUser, name);
    }
  }

  private void addUser(String fullName) {
    String[] nameParts = fullName.split(" ", 2);
    if (nameParts.length < 2) {
      System.out.printf("Invalid name! Please provide both first and last name.%n%n");
      return;
    }

    String firstName = nameParts[0];
    String lastName = nameParts[1];
    String username = Generator.generateUsername(firstName, lastName);
    String email = Generator.generateEmail(firstName, lastName);
    String password = Generator.generateStrongPassword(12);

    if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
      throw new InformationIncompleteException("Incomplete user information.");
    }

    User newUser = UserFactory.createUser(AccountType.REGULAR);
    Credentials credentials = new Credentials(username, password);
    User.Information information =
      new User.Information.InformationBuilder().credentials(credentials)
      .name(fullName)
      .build();

    newUser.setUsername(username);
    newUser.setInformation(information);
    Database.getInstance().getUsers().add(newUser);

    System.out.println("User successfully added.");
    System.out.println("Credentials:");
    System.out.println("\tEmail: " + email);
    System.out.println("\tPassword: " + password);
    System.out.println();
  }

  private void deleteUser(User currentUser, String username) {
    if (username.equalsIgnoreCase(currentUser.getUsername())) {
      System.out.printf("You cannot delete yourself.%n%n");
      return;
    }

    Optional<User> userOptional = findUserByUsername(username);
    if (userOptional.isEmpty()) {
      System.out.printf("User not found.%n%n");
      return;
    }

    User user = userOptional.get();
    if (user.getUserType() == AccountType.ADMIN) {
      System.out.printf("You cannot delete an admin.%n%n");
      return;
    }

    removeUserReferences(user);
    Database.getInstance().getUsers().remove(user);

    System.out.printf("User successfully deleted.%n%n");
  }

  private Optional<User> findUserByUsername(String username) {
    return Database.getInstance()
      .getUsers()
      .stream()
      .filter(user -> user.getUsername().equalsIgnoreCase(username))
      .findFirst();
  }

  private void removeUserReferences(User user) {
    Database database = Database.getInstance();

    if (user.getUserType() == AccountType.REGULAR) {
      database.getProductions()
        .forEach(production -> production.getRatings()
          .removeIf(rating -> rating.getUsername().equalsIgnoreCase(user.getUsername())));
    }

    if (user.getUserType() == AccountType.CONTRIBUTOR) {
      database.getActors()
        .stream()
        .filter(actor -> actor.getResponsible().equalsIgnoreCase(user.getUsername()))
        .forEach(actor -> actor.setResponsible("ADMIN"));

      database.getProductions()
        .stream()
        .filter(production -> production.getResponsible().equalsIgnoreCase(user.getUsername()))
        .forEach(production -> production.setResponsible("ADMIN"));
    }

    database.getRequests()
      .removeIf(request -> request.getUsername().equalsIgnoreCase(user.getUsername()));

    database.getProductions().forEach(production -> production.removeObserver(user));
    database.getRequests().forEach(request -> request.removeObserver(user));
  }
}
