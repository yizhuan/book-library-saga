package mobi.qubits.ex.library.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * A reader borrows a book.
 * 
 * @author yizhuan
 *
 */
public class CancelReservationCommand implements LibraryCommand{

	
	private String borrowerId;
	
	@TargetAggregateIdentifier
	private String bookId;
	
	public CancelReservationCommand(String borrowerId, String bookId) {
		super();
		this.bookId = bookId;
	
		this.borrowerId = borrowerId;
	}
	
	public String getBookId() {
		return bookId;
	}

	public String getBorrowerId() {
		return borrowerId;
	}
}
