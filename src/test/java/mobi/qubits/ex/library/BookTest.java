package mobi.qubits.ex.library;

import mobi.qubits.ex.library.domain.book.Book;
import mobi.qubits.ex.library.domain.commands.LendCommand;
import mobi.qubits.ex.library.domain.commands.MarkBookHotCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewBookCommand;
import mobi.qubits.ex.library.domain.events.LendEvent;
import mobi.qubits.ex.library.domain.events.MarkBookHotEvent;
import mobi.qubits.ex.library.domain.events.NewBookRegisteredEvent;
import mobi.qubits.ex.library.domain.events.NewReaderRegisteredEvent;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author yizhuan
 *
 */
public class BookTest {

	private FixtureConfiguration<Book> fixture;

	@Before
	public void setUp() {
		fixture = Fixtures.newGivenWhenThenFixture(Book.class);
	}

	@Test
	public void testRegisterBook() {

		fixture.given()
				.when(new RegisterNewBookCommand("1", "Book1", "Albert"))
				.expectEvents(
						new NewBookRegisteredEvent("1", "Book1", "Albert"));

	}


	@Test
	public void testLendBook() {

		fixture.given(	new NewBookRegisteredEvent("1", "Book100", "Albert"),
				new NewReaderRegisteredEvent("1", "John")
				)
				.when(new LendCommand("1", "1"))
				.expectEvents(new LendEvent("1", "1"));

	}	
	
	@Test
	public void testMarkBookHot() {

		fixture.given(	new NewBookRegisteredEvent("1", "Book100", "Albert"),
				new NewReaderRegisteredEvent("1", "John")
				)
				.when(new MarkBookHotCommand("1"))
				.expectEvents(new MarkBookHotEvent("1"));

	}		
}
