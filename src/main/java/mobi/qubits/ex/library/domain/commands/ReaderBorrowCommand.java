package mobi.qubits.ex.library.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * A reader borrows a book.
 * 
 * @author yizhuan
 *
 */
public class ReaderBorrowCommand implements LibraryCommand{

	@TargetAggregateIdentifier
	private String borrowerId;
		
	private String bookId;
	
	public ReaderBorrowCommand(String borrowerId, String bookId) {
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
