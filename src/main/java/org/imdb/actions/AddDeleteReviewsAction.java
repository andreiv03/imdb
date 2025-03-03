package org.imdb.actions;

import org.imdb.production.Production;
import org.imdb.production.Rating;
import org.imdb.strategies.ReviewAdditionStrategy;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.List;
import java.util.Optional;

public class AddDeleteReviewsAction extends Action {
  @Override
  public void execute(User currentUser) {
    List<Rating> userRatings = currentUser.getReviewedProductions();
    System.out.printf("Your ratings: %s%n%n", userRatings);

    System.out.println("What do you want to do?");
    System.out.println("\t1) Add");
    System.out.println("\t2) Delete");
    System.out.println("\t3) Exit");

    int action = promptForIntInRange("option", 3);
    if (action == 3) {
      return;
    }

    String title = promptForString("Enter the production title: ");

    Optional<Production> productionOptional = findProductionByTitle(title);
    if (productionOptional.isEmpty()) {
      System.out.printf("Production not found.%n%n");
      return;
    }

    if (action == 1) {
      addReview(currentUser, productionOptional.get(), userRatings);
    }

    if (action == 2) {
      deleteReview(currentUser, productionOptional.get(), userRatings);
    }
  }

  private void addReview(User currentUser, Production production, List<Rating> userRatings) {
    if (userRatings.stream()
      .anyMatch(r -> r.getProductionTitle().equalsIgnoreCase(production.getTitle()))) {
      System.out.printf("You have already reviewed this production.%n%n");
      return;
    }

    int ratingValue = promptForIntInRange("rating", 10);
    String review = promptForString("Enter the review: ");

    Rating newRating = new Rating();
    newRating.setUsername(currentUser.getUsername());
    newRating.setRating(ratingValue);
    newRating.setComment(review);
    newRating.setProductionTitle(production.getTitle());

    production.getRatings().add(newRating);
    production.notifyObservers();
    production.registerObserver(currentUser);

    currentUser.updateExperience(new ReviewAdditionStrategy());
    currentUser.getReviewedProductions().add(newRating);

    System.out.printf("Review added successfully.%n%n");
  }

  private void deleteReview(User currentUser, Production production, List<Rating> userRatings) {
    if (userRatings.stream()
      .noneMatch(rating -> rating.getProductionTitle().equalsIgnoreCase(production.getTitle()))) {
      System.out.printf("You have not reviewed this production.%n%n");
      return;
    }

    production.getRatings()
      .removeIf(rating -> rating.getUsername().equals(currentUser.getUsername()));
    userRatings.removeIf(rating -> rating.getProductionTitle()
      .equalsIgnoreCase(production.getTitle()));

    System.out.printf("Review deleted successfully.%n%n");
  }

  private Optional<Production> findProductionByTitle(String title) {
    return Database.getInstance()
      .getProductions()
      .stream()
      .filter(production -> production.getTitle().equalsIgnoreCase(title))
      .findFirst();
  }
}
