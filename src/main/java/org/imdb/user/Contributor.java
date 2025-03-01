package org.imdb.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.imdb.enumerations.AccountType;
import org.imdb.interfaces.Observer;
import org.imdb.interfaces.RequestsManager;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;

@JsonTypeName("Contributor")
public class Contributor extends Staff implements RequestsManager, Observer {
	public Contributor() {
		super();
		this.setUserType(AccountType.Contributor);
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

	@Override
	public void receiveNotification(String notification) {
		this.getNotifications().add(notification);
	}
}
