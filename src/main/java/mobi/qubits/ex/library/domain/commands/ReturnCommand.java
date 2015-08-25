package mobi.qubits.ex.library.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

/**
 * 
 * @author yizhuan
 *
 */
public class ReturnCommand implements LibraryCommand{

	
	private String borrowerId;
	
	@TargetAggregateIdentifier
	private String bookId;

	public ReturnCommand(String borrowerId, String bookId) {
		super();
		this.borrowerId = borrowerId;
		this.bookId = bookId;	
	}


	public String getBorrowerId() {
		return borrowerId;
	}

	public String getBookId(){
		return this.bookId;
	}

}
