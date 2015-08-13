package mobi.qubits.ex.library.domain;

import mobi.qubits.ex.library.domain.commands.LendCommand;
import mobi.qubits.ex.library.domain.commands.MarkBookHotCommand;
import mobi.qubits.ex.library.domain.commands.RejectCommand;
import mobi.qubits.ex.library.domain.events.BorrowEvent;
import mobi.qubits.ex.library.domain.events.LendEvent;

import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author yizhuan
 *
 */
public class BookAdminSaga extends AbstractAnnotatedSaga {

	private static final long serialVersionUID = -5929174026616232734L;

	private int popularityCount = 0;

	@Autowired
	private transient BookCommandGateway bookCmdGateway;	
		
	@StartSaga
	@SagaEventHandler(associationProperty = "borrowerId")
	public void handle(BorrowEvent event) {

		associateWith("bookId", event.getBookId());

		try {			
			bookCmdGateway.send(new LendCommand(event.getBorrowerId(), event.getBookId()));
			popularityCount++;
		} catch (BookAlreadyTakenException e) {
			//call to roll back
			bookCmdGateway.send( new RejectCommand(event.getBorrowerId(), event.getBookId()));
		}
	}

	@SagaEventHandler(associationProperty = "bookId")
	public void handle(LendEvent event) {

		if (popularityCount == 5) {
			bookCmdGateway.send(new MarkBookHotCommand(event.getBookId()));
			end();
		}
	}

}
