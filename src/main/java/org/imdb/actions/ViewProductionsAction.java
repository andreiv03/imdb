package org.imdb.actions;

import org.imdb.enumerations.Genre;
import org.imdb.production.Production;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewProductionsAction extends Action {
  @Override
  public void execute(User currentUser) {
    List<Production> productions = new ArrayList<>(Database.getInstance().getProductions());

    String sortOption = promptForConfirmation("Do you want to sort the productions?");
    if (sortOption.equals("Exit")) {
      return;
    }

    String filterOption = promptForConfirmation("Do you want to filter the productions?");
    if (filterOption.equals("Exit")) {
      return;
    }

    if (filterOption.equals("Yes")) {
      productions = filterProductions(productions);
    }

    if (sortOption.equals("Yes")) {
      productions.sort(null);
    }

    System.out.printf("Productions: %s%n%n", productions);
  }

  private List<Production> filterProductions(List<Production> productions) {
    System.out.println("Select a filter:");
    System.out.println("\t1) Genre");
    System.out.println("\t2) Type");
    System.out.println("\t3) Rating");
    System.out.println("\t4) Number of ratings");
    System.out.println("\t5) Number of directors");
    System.out.println("\t6) Number of actors");
    System.out.println("\t7) Number of genres");
    System.out.println("\t8) Exit");

    int filterOption = promptForIntInRange("option", 8);
    if (filterOption == 8) {
      return productions;
    }

    return switch (filterOption) {
      case 1 -> filterByGenre(productions);
      case 2 -> filterByType(productions);
      case 3 ->
        filterByNumericValue(productions, "Enter the rating [1-10]: ", 10,
          (production, value) -> (int) production.getAverageRating() == value);
      case 4 ->
        filterByNumericValue(productions, "Enter the number of ratings: ", Integer.MAX_VALUE,
          (production, value) -> production.getRatings()
          .size() >= value);
      case 5 ->
        filterByNumericValue(productions, "Enter the number of directors: ", Integer.MAX_VALUE,
          (production, value) -> production.getDirectors()
          .size() >= value);
      case 6 ->
        filterByNumericValue(productions, "Enter the number of actors: ", Integer.MAX_VALUE,
          (production, value) -> production.getActors()
          .size() >= value);
      case 7 ->
        filterByNumericValue(productions, "Enter the number of genres: ", Integer.MAX_VALUE,
          (production, value) -> production.getGenres()
          .size() >= value);
      default -> productions;
    };
  }

  private List<Production> filterByGenre(List<Production> productions) {
    String genreInput = promptForString("Enter the genre: ").toUpperCase();
    if (!Genre.contains(genreInput)) {
      System.out.printf("Invalid genre.%n%n");
      return productions;
    }

    Genre genre = Genre.valueOf(genreInput);
    return productions.stream()
      .filter(production -> !production.getGenres().contains(genre))
      .collect(Collectors.toList());
  }

  private List<Production> filterByType(List<Production> productions) {
    System.out.println("Choose the type:");
    System.out.println("\t1) Movie");
    System.out.println("\t2) Series");
    System.out.println("\t3) Exit");

    int typeOption = promptForIntInRange("option", 3);
    if (typeOption == 3) {
      return productions;
    }

    String typeFilter = (typeOption == 1) ? "Movie" : "Series";
    return productions.stream()
      .filter(production -> !production.getType().equals(typeFilter))
      .collect(Collectors.toList());
  }

  private List<Production> filterByNumericValue(List<Production> productions, String prompt,
    int max, NumericFilter filter) {
    System.out.println(prompt);
    int value = promptForIntInRange("option", max);
    return productions.stream()
      .filter(production -> filter.test(production, value))
      .collect(Collectors.toList());
  }
}
