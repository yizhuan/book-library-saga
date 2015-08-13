package mobi.qubits.ex.library.domain;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mobi.qubits.ex.library.domain.commands.BookCommand;

import org.axonframework.commandhandling.gateway.Timeout;
import org.axonframework.common.annotation.MetaData;

/**
 * 
 * @author yizhuan
 *
 */
public interface BookCommandGateway {

	void send(BookCommand command) throws BookAlreadyTakenException,
			BorrowingSameBookException, MaxAllowanceExceededException;

	@Timeout(value = 3, unit = TimeUnit.SECONDS)
	void sendAndWait(BookCommand command, @MetaData("bookId") String bookId)
			throws BookAlreadyTakenException, BorrowingSameBookException,
			MaxAllowanceExceededException;

	@Timeout(value = 3, unit = TimeUnit.SECONDS)
	void sendAndWait(BookCommand command) throws TimeoutException,
			InterruptedException, BookAlreadyTakenException,
			BorrowingSameBookException, MaxAllowanceExceededException;

	void sendAndWait(BookCommand command, long timeout, TimeUnit unit)
			throws TimeoutException, InterruptedException,
			BookAlreadyTakenException, BorrowingSameBookException,
			MaxAllowanceExceededException;
}
