package org.imdb.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.imdb.actor.Actor;
import org.imdb.enumerations.AccountType;
import org.imdb.interfaces.Observer;
import org.imdb.production.Production;
import org.imdb.strategies.ExperienceStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @formatter:off
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "userType")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Regular.class, name = "Regular"),
		@JsonSubTypes.Type(value = Contributor.class, name = "Contributor"),
		@JsonSubTypes.Type(value = Admin.class, name = "Admin")
})
// @formatter:on
@Getter
@Setter
public abstract class User implements Observer {
  private final List<String> productionsContribution;
  private final List<String> actorsContribution;
  private final List<String> favoriteProductions;
  private final List<String> favoriteActors;
  private final List<String> notifications;
  private final List<String> reviewedProductions;
  private String username;
  private Information information;
  private AccountType userType;
  private String experience;
  private boolean loggedIn;

  public User() {
    this(null, null, null, null, List.of(), List.of(), List.of(), List.of());
  }

  public User(String username, String experience, Information information, AccountType userType,
    List<String> productionsContribution, List<String> actorsContribution,
    List<String> favoriteProductions, List<String> favoriteActors) {
    this.username = username;
    this.experience = (experience != null) ? experience : "0";
    this.information = information;
    this.userType = userType;
    this.productionsContribution = productionsContribution;
    this.actorsContribution = actorsContribution;
    this.favoriteProductions = favoriteProductions;
    this.favoriteActors = favoriteActors;
    this.notifications = new ArrayList<>();
    this.reviewedProductions = new ArrayList<>();
    this.loggedIn = false;
  }

  public User(User user) {
    this(user.username, user.experience, user.information, user.userType,
      user.productionsContribution, user.actorsContribution, user.favoriteProductions,
      user.favoriteActors);
    this.notifications.addAll(user.notifications);
    this.reviewedProductions.addAll(user.reviewedProductions);
    this.loggedIn = user.loggedIn;
  }

  public String getExperience() {
    if (userType == AccountType.ADMIN) {
      return "INFINITE";
    }

    return experience;
  }

  public void updateExperience(ExperienceStrategy experienceStrategy) {
    if (userType == AccountType.ADMIN) {
      return;
    }

    int experience = Integer.parseInt(this.experience) + experienceStrategy.getExperiencePoints();
    this.experience = String.valueOf(experience);
  }

  public void addProductionToFavorites(Production production) {
    if (production == null) {
      throw new IllegalArgumentException("Production cannot be null.");
    }

    favoriteProductions.add(production.getTitle());
  }

  public void removeProductionFromFavorites(Production production) {
    favoriteProductions.removeIf(title -> title.equals(production.getTitle()));
  }

  public void addActorToFavorites(Actor actor) {
    if (actor == null) {
      throw new IllegalArgumentException("Actor cannot be null.");
    }

    favoriteActors.add(actor.getName());
  }

  public void removeActorFromFavorites(Actor actor) {
    favoriteActors.removeIf(name -> name.equals(actor.getName()));
  }

  public void addNotification(String notification) {
    if (notification == null) {
      throw new IllegalArgumentException("Notification cannot be null.");
    }

    notifications.add(notification);
  }

  @Getter
  public static class Information {
    private final Credentials credentials;
    private final String name;
    private final String country;
    private final int age;
    private final String gender;
    private final LocalDate birthDate;

    public Information() {
      this.credentials = null;
      this.name = null;
      this.country = null;
      this.age = 0;
      this.gender = null;
      this.birthDate = null;
    }

    private Information(InformationBuilder informationBuilder) {
      this.credentials = informationBuilder.credentials;
      this.name = informationBuilder.name;
      this.country = informationBuilder.country;
      this.age = informationBuilder.age;
      this.gender = informationBuilder.gender;
      this.birthDate = informationBuilder.birthDate;
    }

    public static class InformationBuilder {
      private Credentials credentials;
      private String name;
      private String country;
      private int age;
      private String gender;
      private LocalDate birthDate;

      public InformationBuilder credentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
      }

      public InformationBuilder name(String name) {
        this.name = name;
        return this;
      }

      public InformationBuilder country(String country) {
        this.country = country;
        return this;
      }

      public InformationBuilder age(int age) {
        this.age = age;
        return this;
      }

      public InformationBuilder gender(String gender) {
        this.gender = gender;
        return this;
      }

      public InformationBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
      }

      public Information build() {
        return new Information(this);
      }
    }
  }
}
