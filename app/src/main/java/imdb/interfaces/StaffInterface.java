package imdb.interfaces;

import imdb.actor.Actor;
import imdb.production.Production;

public interface StaffInterface {
	void addProductionSystem(Production production);

	void addActorSystem(Actor actor);

	void removeProductionSystem(String title);

	void removeActorSystem(String name);

	void updateProductionSystem(Production production);

	void updateActorSystem(Actor actor);
}
