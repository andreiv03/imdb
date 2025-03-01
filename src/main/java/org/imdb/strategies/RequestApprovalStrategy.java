package org.imdb.strategies;

import org.imdb.interfaces.ExperienceStrategy;

public class RequestApprovalStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 25;
	}
}
