package org.imdb.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.imdb.enumerations.AccountType;
import org.imdb.interfaces.Observer;
import org.imdb.interfaces.RequestsManager;
import org.imdb.request.Request;
import org.imdb.request.RequestsHolder;

@JsonTypeName("Regular")
public class Regular extends User implements RequestsManager, Observer {
	public Regular() {
		super();
		this.setUserType(AccountType.Regular);
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
		this.getNotifications().add(notification);
	}
}
