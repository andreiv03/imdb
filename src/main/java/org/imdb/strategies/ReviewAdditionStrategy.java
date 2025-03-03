package org.imdb.strategies;

public class ReviewAdditionStrategy implements ExperienceStrategy {
  private static final int REVIEW_ADDITION_EXPERIENCE = 5;

  @Override
  public int getExperiencePoints() {
    return REVIEW_ADDITION_EXPERIENCE;
  }
}
