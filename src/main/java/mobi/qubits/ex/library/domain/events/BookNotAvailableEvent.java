package mobi.qubits.ex.library.domain.events;

public class BookNotAvailableEvent {

	private final String id;

	public BookNotAvailableEvent(String id) {
		super();
		this.id = id;

	}

	public String getId() {
		return id;
	}

}
