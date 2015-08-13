package mobi.qubits.ex.library.domain.reader;

import java.util.ArrayList;
import java.util.List;

import mobi.qubits.ex.library.domain.BorrowingSameBookException;
import mobi.qubits.ex.library.domain.MaxAllowanceExceededException;
import mobi.qubits.ex.library.domain.commands.BorrowCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewReaderCommand;
import mobi.qubits.ex.library.domain.commands.RejectCommand;
import mobi.qubits.ex.library.domain.commands.ReturnCommand;
import mobi.qubits.ex.library.domain.events.BorrowEvent;
import mobi.qubits.ex.library.domain.events.BorrowingRejectedEvent;
import mobi.qubits.ex.library.domain.events.NewReaderRegisteredEvent;
import mobi.qubits.ex.library.domain.events.ReturnEvent;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

/**
 * 
 * @author yizhuan
 *
 */
public class Reader extends AbstractAnnotatedAggregateRoot<String> {
	
	private static final long serialVersionUID = -3279834774069841308L;

	@AggregateIdentifier
	private String id;

	private String name;
	
	private int booksBorrowed = 0;
	
	private List<String> borrowedBookIds = new ArrayList<String>();

	Reader() {

	}

	@CommandHandler
	public Reader(RegisterNewReaderCommand cmd) {
		apply(new NewReaderRegisteredEvent(cmd.getId(),cmd.getName()));
	}	
	
	@CommandHandler
	public void on(BorrowCommand cmd) 
			throws MaxAllowanceExceededException, BorrowingSameBookException{

		if (hasBook(cmd.getBookId())){			
			throw new BorrowingSameBookException();
		}		
		
		if (booksBorrowed>=3){			
			 throw new MaxAllowanceExceededException();
		}
		
		apply(new BorrowEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}
	
	@CommandHandler
	public void on(RejectCommand cmd){
		apply(new BorrowingRejectedEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}		
	
	
	@CommandHandler
	public void on(ReturnCommand cmd){
		apply(new ReturnEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}	
		
	@EventSourcingHandler
	void on(NewReaderRegisteredEvent event) {
		this.id = event.getId();
		this.name = event.getName();
	}	
	
	
	@EventSourcingHandler
	void on(BorrowEvent event) {
		booksBorrowed++;
		borrowedBookIds.add(event.getBookId());
	}
	
	@EventSourcingHandler
	void on(BorrowingRejectedEvent event) {
		booksBorrowed--;
		borrowedBookIds.add(event.getBookId());
	}	


	@EventSourcingHandler
	void on(ReturnEvent event) {
		booksBorrowed--;
	}
	
	private boolean hasBook(String bookId){
		return borrowedBookIds.contains(bookId);
	}	

}