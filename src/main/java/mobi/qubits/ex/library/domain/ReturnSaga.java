package mobi.qubits.ex.library.domain;

import mobi.qubits.ex.library.domain.commands.ReturnCommand;
import mobi.qubits.ex.library.domain.events.ReaderReturnEvent;

import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author yizhuan
 *
 */
public class ReturnSaga extends AbstractAnnotatedSaga {

	private static final long serialVersionUID = 4562223328964280018L;
	
	@Autowired
	private transient BookCommandGateway bookCmdGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "bookId")
	public void handle(ReaderReturnEvent event) {

		bookCmdGateway.send(new ReturnCommand(event.getBorrowerId(), event.getBookId()));
		end();

	}

}
