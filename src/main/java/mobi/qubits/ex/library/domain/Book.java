package mobi.qubits.ex.library.domain;

import mobi.qubits.ex.library.domain.commands.BorrowCommand;
import mobi.qubits.ex.library.domain.commands.MarkBookHotCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewBookCommand;
import mobi.qubits.ex.library.domain.commands.ReturnCommand;
import mobi.qubits.ex.library.domain.events.BorrowEvent;
import mobi.qubits.ex.library.domain.events.MarkBookHotEvent;
import mobi.qubits.ex.library.domain.events.NewBookRegisteredEvent;
import mobi.qubits.ex.library.domain.events.ReaderBorrowEvent;
import mobi.qubits.ex.library.domain.events.ReaderReturnEvent;
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
public class Book extends AbstractAnnotatedAggregateRoot<String> {

	private static final long serialVersionUID = 4245371056740478404L;

	@AggregateIdentifier
	private String id;

	private Boolean isHot = false;
	
	Book() {

	}

	@CommandHandler
	public Book(RegisterNewBookCommand cmd) {
		apply(new NewBookRegisteredEvent(cmd.getId(), cmd.getTitle(),
				cmd.getAuthor()));
	}	
		
	@CommandHandler
	public void on(BorrowCommand cmd) {
		apply(new BorrowEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}		
	
	@CommandHandler
	public void on(ReturnCommand cmd) {
		apply(new ReturnEvent(cmd.getBorrowerId(), cmd.getBookId()));
	}			
	
	@CommandHandler
	public void on(MarkBookHotCommand cmd) {
		apply(new MarkBookHotEvent(cmd.getId()));
	}	
	
	@EventSourcingHandler
	void on(NewBookRegisteredEvent event) {
		this.id = event.getId();
	}
	
	@EventSourcingHandler
	void on(BorrowEvent event) {
		//TODO
	}	
	
	@EventSourcingHandler
	void on(ReturnEvent event) {
		//TODO
	}			
		
	@EventSourcingHandler
	void on(MarkBookHotEvent event) {
		this.isHot = true;
	}		
	
}
