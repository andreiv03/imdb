package org.imdb.production;

import lombok.Getter;
import lombok.Setter;
import org.imdb.utils.JsonUtils;

@Getter
@Setter
public class Episode {
  private String episodeName;
  private String duration;

  public Episode() {
    this(null, null);
  }

  public Episode(String episodeName, String duration) {
    this.episodeName = episodeName;
    this.duration = duration;
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
