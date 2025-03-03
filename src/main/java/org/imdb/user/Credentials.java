package org.imdb.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credentials {
  private String email;
  private String password;

  public Credentials() {
    this(null, null);
  }

  public Credentials(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
