package imdb.strategies;

import imdb.interfaces.ExperienceStrategy;

public class ProductionAdditionStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 15;
	}
}
