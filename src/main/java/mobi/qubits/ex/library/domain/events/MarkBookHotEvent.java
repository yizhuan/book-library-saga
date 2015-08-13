package mobi.qubits.ex.library.domain.events;

import java.io.Serializable;

/**
 * 
 * @author yizhuan
 *
 */
public class MarkBookHotEvent implements Serializable{

	private final String id;

	public MarkBookHotEvent(String id) {
		super();
		this.id = id;

	}


	public String getId() {
		return id;
	}


	
}
