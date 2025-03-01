package org.imdb.strategies;

import org.imdb.interfaces.ExperienceStrategy;

public class ActorAdditionStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 10;
	}
}
