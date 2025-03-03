package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Production;
import org.imdb.production.Rating;
import org.imdb.strategies.ReviewAdditionStrategy;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AddDeleteReviewsAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();
      ArrayList<Rating> ratings = new ArrayList<>();

      for (Production production : Database.getInstance().getProductions())
        ratings.addAll(production.getRatings()
          .stream()
          .filter(rating -> rating.getUsername().equals(currentUser.getUsername()))
          .collect(Collectors.toCollection(ArrayList::new)));

      System.out.println("Your ratings: " + ratings);
      System.out.println();

      System.out.println("What do you want to do?");
      System.out.println("\t1) Add");
      System.out.println("\t2) Delete");
      System.out.println("\t3) Exit");

      int action = promptForIntInRange(3);

      if (action == 3)
        return;

      String name = promptForString("Enter the name: ");

      switch (action) {
        case 1:
          if (Database.getInstance()
            .getProductions()
            .stream()
            .noneMatch(production -> production.getTitle().equals(name))) {
            System.out.println("Production not found!");
            System.out.println();
            return;
          }

          if (ratings.stream().anyMatch(rating -> rating.getProductionTitle().equals(name))) {
            System.out.println("You have already reviewed this production!");
            System.out.println();
            return;
          }

          String rating = promptForString("Enter the rating (1-10): ");

          if (Integer.parseInt(rating) < 1 || Integer.parseInt(rating) > 10) {
            System.out.println("Invalid rating!");
            System.out.println();
            return;
          }

          String review = promptForString("Enter the review: ");

          for (Production production : Database.getInstance().getProductions()) {
            if (production.getTitle().equals(name)) {
              Rating newRating = new Rating();
              newRating.setUsername(currentUser.getUsername());
              newRating.setRating(Integer.parseInt(rating));
              newRating.setComment(review);
              newRating.setProductionTitle(name);
              production.getRatings().add(newRating);
              production.notifyObservers();
              production.registerObserver(currentUser);

              if (!currentUser.getReviewedProductions().contains(production.getTitle()))
                currentUser.updateExperience(new ReviewAdditionStrategy());

              currentUser.getReviewedProductions().add(production.getTitle());
            }
          }

          break;

        case 2:
          if (ratings.stream().noneMatch(r -> r.getProductionTitle().equals(name))) {
            System.out.println("You have not reviewed this production!");
            System.out.println();
            return;
          }

          for (Production production : Database.getInstance().getProductions())
            if (production.getTitle().equals(name))
              production.getRatings()
                .removeIf(r -> r.getUsername().equals(currentUser.getUsername()));
      }
    } catch (InvalidOptionException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
