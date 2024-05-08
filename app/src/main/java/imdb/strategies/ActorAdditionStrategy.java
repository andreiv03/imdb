package imdb.strategies;

import imdb.interfaces.ExperienceStrategy;

public class ActorAdditionStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 10;
	}
}
