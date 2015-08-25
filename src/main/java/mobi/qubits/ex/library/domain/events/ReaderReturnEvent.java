package mobi.qubits.ex.library.domain.events;


/**
 * 
 * @author yizhuan
 *
 */
public class ReaderReturnEvent implements LibraryEvent{

	private final String borrowerId;//reader
	
	private String bookId;

	public ReaderReturnEvent(String borrowerId,  String bookId) {
		super();
		this.borrowerId = borrowerId;
		
		this.bookId = bookId;
	}


	public String getBorrowerId() {
		return borrowerId;
	}


	public String getBookId() {
		return bookId;
	}
	
}
