package org.imdb.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.imdb.enumerations.AccountType;
import org.imdb.interfaces.Observer;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;
import org.imdb.request.RequestsManager;

@JsonTypeName("Regular")
public class Regular extends User implements RequestsManager, Observer {
  public Regular() {
    super();
    this.setUserType(AccountType.REGULAR);
  }

  public Regular(User user) {
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

  @Override
  public void receiveNotification(String notification) {
    addNotification(notification);
  }
}
