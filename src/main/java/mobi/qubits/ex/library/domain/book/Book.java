package mobi.qubits.ex.library.domain.book;

import mobi.qubits.ex.library.domain.commands.MarkBookHotCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewBookCommand;
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

	private Boolean isHot = false;

	Book() {

	}

	@CommandHandler
	public Book(RegisterNewBookCommand cmd) {
		apply(new NewBookRegisteredEvent(cmd.getId(), cmd.getTitle(),
				cmd.getAuthor()));
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
	void on(MarkBookHotEvent event) {
		this.isHot = true;
	}		
	
}
