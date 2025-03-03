package org.imdb.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.imdb.enumerations.AccountType;
import org.imdb.interfaces.Observer;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;
import org.imdb.request.RequestsManager;

@JsonTypeName("Contributor")
public class Contributor extends Staff implements RequestsManager, Observer {
  public Contributor() {
    super();
    this.setUserType(AccountType.CONTRIBUTOR);
  }

  public Contributor(User user) {
    super(user);
  }

  @Override
  public void createRequest(Request request) {
    RequestsHolder.addRequest(request);
  }

  @Override
  public void removeRequest(Request request) {
    RequestsHolder.removeRequest(request);
  }
}
