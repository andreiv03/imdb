package org.imdb.strategies;

import org.imdb.interfaces.ExperienceStrategy;

public class ProductionAdditionStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 15;
	}
}
