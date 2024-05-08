package imdb.interfaces;

import imdb.request.Request;

public interface RequestsManager {
	void createRequest(Request request);

	void removeRequest(Request request);
}
