package org.imdb.actor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.imdb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Actor implements Comparable<Actor> {
  private String name;
  private String biography;
  private List<Performance> performances;
  @JsonIgnore
  private String responsible;

  public Actor() {
    this(null, null);
  }

  public Actor(String name, String biography) {
    this.name = name;
    this.performances = new ArrayList<>();
    this.biography = biography;
    this.responsible = "ADMIN";
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }

  @Override
  public int compareTo(@NonNull Actor other) {
    return name.compareToIgnoreCase(other.name);
  }

  @Getter
  @Setter
  public static class Performance {
    private String title;
    private String type;

    public Performance() {
      this(null, null);
    }

    public Performance(String title, String type) {
      this.title = title;
      this.type = type;
    }

    @Override
    public String toString() {
      return JsonUtils.toJson(this);
    }
  }
}
