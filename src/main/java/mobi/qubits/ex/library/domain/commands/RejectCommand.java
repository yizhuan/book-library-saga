package mobi.qubits.ex.library.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * 
 * @author yizhuan
 *
 */
public class RejectCommand implements BookCommand{

	@TargetAggregateIdentifier
	private String borrowerId;
	
	private String bookId;
	
	public RejectCommand(String borrowerId, String bookId) {		
		this.bookId = bookId;
	
		this.borrowerId = borrowerId;
	}

	public String getId() {
		return borrowerId;
	}
	
	public String getBookId() {
		return bookId;
	}

	public String getBorrowerId() {
		return borrowerId;
	}

	
}
