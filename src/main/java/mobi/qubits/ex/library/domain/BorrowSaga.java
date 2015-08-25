package mobi.qubits.ex.library.domain;

import mobi.qubits.ex.library.domain.commands.BorrowCommand;
import mobi.qubits.ex.library.domain.events.ReaderBorrowEvent;

import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author yizhuan
 *
 */
public class BorrowSaga extends AbstractAnnotatedSaga {


	private static final long serialVersionUID = 4666095068846218249L;
	
	
	@Autowired
	private transient BookCommandGateway bookCmdGateway;	
		
	@StartSaga
	@SagaEventHandler(associationProperty = "bookId")
	public void handle(ReaderBorrowEvent event) {
		
		bookCmdGateway.send(new BorrowCommand(event.getBorrowerId(),event.getBookId()));
		end();
		
	}

}
