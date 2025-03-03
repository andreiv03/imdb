package org.imdb.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.imdb.enumerations.AccountType;
import org.imdb.interfaces.Observer;

@JsonTypeName("Admin")
public class Admin extends Staff implements Observer {
  public Admin() {
    super();
    this.setUserType(AccountType.ADMIN);
  }

  public Admin(User user) {
    super(user);
  }
}
