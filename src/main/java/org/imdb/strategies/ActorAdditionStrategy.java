package org.imdb.strategies;

public class ActorAdditionStrategy implements ExperienceStrategy {
  private static final int ACTOR_ADDITION_EXPERIENCE = 10;

  @Override
  public int getExperiencePoints() {
    return ACTOR_ADDITION_EXPERIENCE;
  }
}
