package org.imdb.production;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import org.imdb.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeName("Series")
@Getter
@Setter
public class Series extends Production {
  private final Map<String, List<Episode>> seasons;
  private int releaseYear;
  private int numSeasons;

  public Series() {
    this(null, null, 0, 0, 0);
  }

  public Series(String title, String plot, double averageRating, int releaseYear, int numSeasons) {
    super(title, "Series", plot, averageRating);
    this.releaseYear = releaseYear;
    this.numSeasons = numSeasons;
    this.seasons = new HashMap<>();
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
