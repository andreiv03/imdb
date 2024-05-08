package imdb.strategies;

import imdb.interfaces.ExperienceStrategy;

public class ReviewAdditionStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 5;
	}
}
