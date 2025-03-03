package org.imdb.strategies;

public class ProductionAdditionStrategy implements ExperienceStrategy {
  private static final int PRODUCTION_ADDITION_EXPERIENCE = 15;


  @Override
  public int getExperiencePoints() {
    return PRODUCTION_ADDITION_EXPERIENCE;
  }
}
