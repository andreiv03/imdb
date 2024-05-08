package imdb.strategies;

import imdb.interfaces.ExperienceStrategy;

public class RequestApprovalStrategy implements ExperienceStrategy {
	@Override
	public int calculateExperience() {
		return 25;
	}
}
