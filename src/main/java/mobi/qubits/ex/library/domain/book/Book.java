package mobi.qubits.ex.library.domain.book;

import mobi.qubits.ex.library.domain.BookAlreadyTakenException;
import mobi.qubits.ex.library.domain.BorrowingSameBookException;
import mobi.qubits.ex.library.domain.MaxAllowanceExceededException;
import mobi.qubits.ex.library.domain.commands.LendCommand;
import mobi.qubits.ex.library.domain.commands.MarkBookHotCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewBookCommand;
import mobi.qubits.ex.library.domain.events.LendEvent;
import mobi.qubits.ex.library.domain.events.MarkBookHotEvent;
import mobi.qubits.ex.library.domain.events.NewBookRegisteredEvent;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

/**
 * 
 * @author yizhuan
 *
 */
public class Book extends AbstractAnnotatedAggregateRoot<String> {

	private static final long serialVersionUID = 4245371056740478404L;

	@AggregateIdentifier
	private String id;

	private String title;
	private String author;

	private Boolean isAvailable = true;

	private Boolean isHot = false;

	//no need
	private int popularityCount = 0;

	Book() {

	}

	@CommandHandler
	public Book(RegisterNewBookCommand cmd) {
		apply(new NewBookRegisteredEvent(cmd.getId(), cmd.getTitle(),
				cmd.getAuthor()));
	}	
	
	@CommandHandler
	void on(LendCommand cmd) throws BookAlreadyTakenException,
			BorrowingSameBookException, MaxAllowanceExceededException{
		
		if (this.isAvailable ){
			apply(new LendEvent(cmd.getBorrowerId(), cmd.getBookId()));			
		}
		else{
			//apply(new BookNotAvailableEvent(cmd.getBookId()));
			throw new BookAlreadyTakenException();
		}		
	}			
		
	@CommandHandler
	public void on(MarkBookHotCommand cmd) {
		apply(new MarkBookHotEvent(cmd.getId()));
	}	
	
	@EventSourcingHandler
	void on(NewBookRegisteredEvent event) {
		this.id = event.getBorrowerId();
		this.title = event.getTitle();
		this.author = event.getAuthor();
		this.isAvailable = true;
		this.popularityCount = 0;
	}

	@EventSourcingHandler
	void on(MarkBookHotEvent event) {
		this.isHot = true;
	}		
	
	@EventSourcingHandler
	void on(LendEvent event) {
		this.isAvailable = false;
		this.popularityCount++;
	}
	
	/*
	@EventSourcingHandler
	void on(BookNotAvailableEvent event) {
		//what can I do with this event?
	}
	*/
}
