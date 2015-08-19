package mobi.qubits.ex.library.domain.events;


/**
 * 
 * @author yizhuan
 *
 */
public class MakeReservationEvent implements LibraryEvent{

	private final String borrowerId;//reader
	
	private String bookId;

	public MakeReservationEvent(String borrowerId,  String bookId) {
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
