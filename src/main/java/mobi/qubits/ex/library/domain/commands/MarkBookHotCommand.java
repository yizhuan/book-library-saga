package mobi.qubits.ex.library.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * 
 * @author yizhuan
 *
 */
public class MarkBookHotCommand implements LibraryCommand{

	@TargetAggregateIdentifier
	private String id;

	public MarkBookHotCommand(String id) {
		super();
		this.id = id;

	}
	public String getId() {
		return id;
	}
	
	
}
