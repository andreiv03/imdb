package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Movie;
import org.imdb.production.Production;
import org.imdb.production.Series;
import org.imdb.user.Staff;
import org.imdb.user.User;
import org.imdb.utils.Database;

public class UpdateActorsProductionsAction extends Action {
  @Override
  public void execute(User currentUser) {
    try {
      IMDB imdb = IMDB.getInstance();

      System.out.println("What do you want to update?");
      System.out.println("\t1) Actor");
      System.out.println("\t2) Movie");
      System.out.println("\t3) Series");
      System.out.println("\t4) Exit");

      int choice = promptForIntInRange(4);

      if (choice == 4)
        return;

      String name = promptForString("Enter the name: ");

      switch (choice) {
        case 1: {
          Actor updatedActor = null;

          for (Actor actor : Database.getInstance().getActors()) {
            if (actor.getName().equals(name)) {
              updatedActor = actor;
              break;
            }
          }

          if (updatedActor == null) {
            System.out.println("Actor not found!");
            System.out.println();
            return;
          }

          System.out.println("What do you want to update?");
          System.out.println("\t1) Name");
          System.out.println("\t2) Biography");
          System.out.println("\t3) Exit");

          int choiceTwo = promptForIntInRange(3);

          if (choiceTwo == 3)
            return;

          switch (choiceTwo) {
            case 1:
              String newName = promptForString("Enter the new name: ");
              updatedActor.setName(newName);
              break;

            case 2:
              String newBiography = promptForString("Enter the new biography: ");
              updatedActor.setBiography(newBiography);
              break;
          }

          ((Staff) currentUser).updateActorSystem(updatedActor);
          break;
        }

        case 2: {
          Movie updatedMovie = null;

          for (Production production : Database.getInstance().getProductions()) {
            if (production.getType().equals("Movie") && production.getTitle().equals(name)) {
              updatedMovie = (Movie) production;
              break;
            }
          }

          if (updatedMovie == null) {
            System.out.println("Movie not found!");
            System.out.println();
            return;
          }

          System.out.println("What do you want to update?");
          System.out.println("\t1) Title");
          System.out.println("\t2) Plot");
          System.out.println("\t3) Exit");

          int choiceTwo = promptForIntInRange(3);

          if (choiceTwo == 3)
            return;

          switch (choiceTwo) {
            case 1:
              String newTitle = promptForString("Enter the new title: ");
              updatedMovie.setTitle(newTitle);
              break;

            case 2:
              String newPlot = promptForString("Enter the new plot: ");
              updatedMovie.setPlot(newPlot);
              break;
          }

          ((Staff) currentUser).updateProductionSystem(updatedMovie);
          break;
        }

        case 3: {
          Series updatedSeries = null;

          for (Production production : Database.getInstance().getProductions()) {
            if (production.getType().equals("Series") && production.getTitle().equals(name)) {
              updatedSeries = (Series) production;
              break;
            }
          }

          if (updatedSeries == null) {
            System.out.println("Series not found!");
            System.out.println();
            return;
          }

          System.out.println("What do you want to update?");
          System.out.println("\t1) Title");
          System.out.println("\t2) Plot");
          System.out.println("\t3) Exit");

          int choiceTwo = promptForIntInRange(3);

          if (choiceTwo == 3)
            return;

          switch (choiceTwo) {
            case 1:
              String newTitle = promptForString("Enter the new title: ");
              updatedSeries.setTitle(newTitle);
              break;

            case 2:
              String newPlot = promptForString("Enter the new plot: ");
              updatedSeries.setPlot(newPlot);
              break;
          }

          ((Staff) currentUser).updateProductionSystem(updatedSeries);
          break;
        }
      }
    } catch (InvalidOptionException exception) {
      System.out.println(exception.getMessage());
      System.out.println();
    } catch (Exception ignored) {
    }
  }
}
