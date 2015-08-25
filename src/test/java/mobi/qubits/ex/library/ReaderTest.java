package mobi.qubits.ex.library;

import mobi.qubits.ex.library.domain.Reader;
import mobi.qubits.ex.library.domain.commands.ReaderBorrowCommand;
import mobi.qubits.ex.library.domain.commands.ReaderReturnCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewReaderCommand;
import mobi.qubits.ex.library.domain.events.BorrowEvent;
import mobi.qubits.ex.library.domain.events.NewBookRegisteredEvent;
import mobi.qubits.ex.library.domain.events.NewReaderRegisteredEvent;
import mobi.qubits.ex.library.domain.events.ReaderBorrowEvent;
import mobi.qubits.ex.library.domain.events.ReaderReturnEvent;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author yizhuan
 *
 */
public class ReaderTest {

	private FixtureConfiguration<Reader> fixture;

	@Before
	public void setUp() {
		fixture = Fixtures.newGivenWhenThenFixture(Reader.class);
	}
	
	@Test
	public void testRegisterReader() {

		fixture.given()
				.when(new RegisterNewReaderCommand("1", "John"))
				.expectEvents(
						new NewReaderRegisteredEvent("1", "John"));

	}
	
	
	@Test
	public void testBorrowBook() {
		
		fixture.given(new NewBookRegisteredEvent("1", "Book100", "Albert"),
				new NewReaderRegisteredEvent("1", "John"))						
				.when(new ReaderBorrowCommand("1", "1"))
				.expectEvents(new ReaderBorrowEvent("1", "1"));
	}		
	
	@Test
	public void testReturnBook() {
		
		fixture.given(
				new NewBookRegisteredEvent("1", "Book100", "Albert"),
				new NewReaderRegisteredEvent("1", "John"),
				new ReaderBorrowEvent("1", "1")
				)				
				.when(new ReaderReturnCommand("1", "1"))
				.expectEvents(new ReaderReturnEvent("1", "1"));

	}
				
}
