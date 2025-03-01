package org.imdb.interfaces;

import org.imdb.request.Request;

public interface RequestsManager {
	void createRequest(Request request);

	void removeRequest(Request request);
}
