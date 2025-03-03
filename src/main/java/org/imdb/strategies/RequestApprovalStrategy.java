package org.imdb.strategies;

public class RequestApprovalStrategy implements ExperienceStrategy {
  private static final int REQUEST_APPROVAL_EXPERIENCE = 25;

  @Override
  public int getExperiencePoints() {
    return REQUEST_APPROVAL_EXPERIENCE;
  }
}
