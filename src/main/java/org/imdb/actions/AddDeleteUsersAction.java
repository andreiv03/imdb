package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.enumerations.AccountType;
import org.imdb.exceptions.InformationIncompleteException;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.interfaces.Observer;
import org.imdb.production.Production;
import org.imdb.request.Request;
import org.imdb.user.Credentials;
import org.imdb.user.User;
import org.imdb.user.UserFactory;
import org.imdb.utils.Database;
import org.imdb.utils.Generator;

public class AddDeleteUsersAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();

      System.out.print("Users: [");

      for (User user : Database.getInstance().getUsers()) {
        System.out.print(user.getUsername());

        if (user != Database.getInstance()
          .getUsers()
          .get(Database.getInstance().getUsers().size() - 1))
          System.out.print(", ");
      }

      System.out.println("]");
      System.out.println();

      System.out.println("What do you want to do?");
      System.out.println("\t1) Add");
      System.out.println("\t2) Delete");
      System.out.println("\t3) Exit");

      int action = promptForIntInRange(3);

      if (action == 3)
        return;

      String name = promptForString("Enter the " + (action == 1 ? "name" : "username") + ": ");

      switch (action) {
        case 1:
          String firstName = name.split(" ")[0];
          String lastName = name.split(" ")[1];
          String username = Generator.generateUsername(firstName, lastName);
          String email = Generator.generateEmail(firstName, lastName);
          String password = Generator.generateStrongPassword(12);

          User newUser = UserFactory.createUser(AccountType.REGULAR);

          if (email.isEmpty() || password.isEmpty() || name.isEmpty())
            throw new InformationIncompleteException("Information is incomplete!");

          User.Information.InformationBuilder informationBuilder =
            new User.Information.InformationBuilder().credentials(new Credentials(email, password))
            .name(name);

          newUser.setUsername(username);
          newUser.setInformation(informationBuilder.build());
          Database.getInstance().getUsers().add(newUser);

          System.out.println("The credentials are:");
          System.out.println("\temail: " + email);
          System.out.println("\tpassword: " + password);
          System.out.println();
          break;

        case 2:
          if (name.equals(currentUser.getUsername())) {
            System.out.println("You cannot delete yourself!");
            System.out.println();
            return;
          }

          User user = Database.getInstance()
            .getUsers()
            .stream()
            .filter(u -> u.getUsername().equals(name))
            .findFirst()
            .orElse(null);

          if (user == null) {
            System.out.println("User not found!");
            System.out.println();
            return;
          }

          if (user.getUserType() == AccountType.REGULAR)
            for (Production production : Database.getInstance().getProductions())
              production.getRatings()
                .removeIf(rating -> rating.getUsername().equals(user.getUsername()));

          if (user.getUserType() == AccountType.CONTRIBUTOR) {
            for (Actor actor : Database.getInstance().getActors())
              if (actor.getResponsible().equals(user.getUsername()))
                actor.setResponsible("ADMIN");

            for (Production production : Database.getInstance().getProductions())
              if (production.getResponsible().equals(user.getUsername()))
                production.setResponsible("ADMIN");
          }

          if (user.getUserType() == AccountType.ADMIN) {
            System.out.println("You cannot delete an admin!");
            System.out.println();
            return;
          }

          Database.getInstance()
            .getRequests()
            .removeIf(request -> request.getUsername().equals(user.getUsername()));

          for (Production production : Database.getInstance().getProductions())
            production.removeObserver(user);

          for (Request request : Database.getInstance().getRequests())
            request.removeObserver(user);

          Database.getInstance().getUsers().remove(user);
          break;
      }
    } catch (InvalidOptionException | InformationIncompleteException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
