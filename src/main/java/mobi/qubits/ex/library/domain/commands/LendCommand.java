package mobi.qubits.ex.library.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * Lends a book to a reader.
 * 
 * @author yizhuan
 *
 */
public class LendCommand implements BookCommand{

	@TargetAggregateIdentifier
	private String bookId;
	
	private String borrowerId;	
	
	public LendCommand(String borrowerId, String bookId) {
		super();
		this.bookId = bookId;
	
		this.borrowerId = borrowerId;
	}

	public String getId() {
		return bookId;
	}
	
	public String getBookId() {
		return bookId;
	}


	public String getBorrowerId() {
		return borrowerId;
	}

	
}
