package org.imdb.strategies;

import org.imdb.interfaces.ExperienceStrategy;

public class ReviewAdditionStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 5;
	}
}
