package mobi.qubits.ex.library.domain;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mobi.qubits.ex.library.domain.commands.LibraryCommand;

import org.axonframework.commandhandling.gateway.Timeout;
import org.axonframework.common.annotation.MetaData;

/**
 * 
 * @author yizhuan
 *
 */
public interface BookCommandGateway {

	void send(LibraryCommand command) throws BookAlreadyTakenException,
			BorrowingSameBookException, MaxAllowanceExceededException;

	@Timeout(value = 3, unit = TimeUnit.SECONDS)
	void sendAndWait(LibraryCommand command, @MetaData("bookId") String bookId)
			throws BookAlreadyTakenException, BorrowingSameBookException,
			MaxAllowanceExceededException;

	@Timeout(value = 3, unit = TimeUnit.SECONDS)
	void sendAndWait(LibraryCommand command) throws TimeoutException,
			InterruptedException, BookAlreadyTakenException,
			BorrowingSameBookException, MaxAllowanceExceededException;

	void sendAndWait(LibraryCommand command, long timeout, TimeUnit unit)
			throws TimeoutException, InterruptedException,
			BookAlreadyTakenException, BorrowingSameBookException,
			MaxAllowanceExceededException;
}
