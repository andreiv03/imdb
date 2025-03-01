package org.imdb.actions;

import org.imdb.IMDB;
import org.imdb.actor.Actor;
import org.imdb.exceptions.InvalidOptionException;
import org.imdb.production.Production;
import org.imdb.user.User;

public class SearchActorProductionAction extends Action {
	@Override
	public void execute(User currentUser) {
		try {
			IMDB imdb = IMDB.getInstance();

			System.out.println("What are you searching for?");
			System.out.println("\t1) Actor");
			System.out.println("\t2) Movie");
			System.out.println("\t3) Series");
			System.out.println("\t4) Exit");

			int searchFor = getIntegerUserInput(4);

			if (searchFor == 4)
				return;

			String name = getStringUserInput("Enter the name: ");

			switch (searchFor) {
				case 1:
					for (Actor actor : imdb.getActors()) {
						if (actor.getName().equals(name)) {
							actor.displayInfo();
							return;
						}
					}

					System.out.println("Actor not found!");
					System.out.println();
					break;

				case 2:
					for (Production production : imdb.getProductions()) {
						if (production.getType().equals("Movie") && production.getTitle().equals(name)) {
							production.displayInfo();
							return;
						}
					}

					System.out.println("Movie not found!");
					System.out.println();
					break;

				case 3:
					for (Production production : imdb.getProductions()) {
						if (production.getType().equals("Series") && production.getTitle().equals(name)) {
							production.displayInfo();
							return;
						}
					}

					System.out.println("Series not found!");
					System.out.println();
					break;
			}
		} catch (InvalidOptionException exception) {
			System.out.println(exception.getMessage());
			System.out.println();
		} catch (Exception ignored) {
		}
	}
}
