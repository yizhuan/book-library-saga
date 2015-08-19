package mobi.qubits.ex.library.domain.reader;

import java.util.ArrayList;
import java.util.List;

import mobi.qubits.ex.library.domain.BorrowingSameBookException;
import mobi.qubits.ex.library.domain.MaxAllowanceExceededException;
import mobi.qubits.ex.library.domain.commands.BorrowCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewReaderCommand;
import mobi.qubits.ex.library.domain.commands.ReturnCommand;
import mobi.qubits.ex.library.domain.events.BorrowEvent;
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
		
		if (borrowedBookIds.size()>=3){			
			 throw new MaxAllowanceExceededException();
		}
		
		apply(new BorrowEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}
		
	
	@CommandHandler
	public void on(ReturnCommand cmd){
		apply(new ReturnEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}	
		
	@EventSourcingHandler
	void on(NewReaderRegisteredEvent event) {
		this.id = event.getId();
	}	
	
	
	@EventSourcingHandler
	void on(BorrowEvent event) {
		borrowedBookIds.add(event.getBookId());
	}	

	@EventSourcingHandler
	void on(ReturnEvent event) {
		borrowedBookIds.remove(event.getBookId());
	}
	
	private boolean hasBook(String bookId){
		return borrowedBookIds.contains(bookId);
	}	

}
