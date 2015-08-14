package mobi.qubits.ex.library;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import mobi.qubits.ex.library.domain.BookCommandGateway;
import mobi.qubits.ex.library.domain.commands.BorrowCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewBookCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewReaderCommand;
import mobi.qubits.ex.library.domain.commands.ReturnCommand;
import mobi.qubits.ex.library.domain.reader.Reader;
import mobi.qubits.ex.library.query.BookEntry;
import mobi.qubits.ex.library.query.BookEntryRepository;
import mobi.qubits.ex.library.query.ReaderEntry;
import mobi.qubits.ex.library.query.ReaderEntryRepository;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.DefaultIdentifierFactory;
import org.axonframework.domain.IdentifierFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ExtraTest {

	private final IdentifierFactory identifierFactory = new DefaultIdentifierFactory();
	
	@Autowired
	private BookEntryRepository bookEntryRepository;

	@Autowired
	private ReaderEntryRepository readerEntryRepository;

	@Autowired
	private BookCommandGateway bookCommandGateway;
	
	@Autowired
	private CommandGateway cmdGateway;	
	
	@Test 
	public void testHotBook() throws Exception{
		
		String reader1 = identifierFactory.generateIdentifier();
		cmdGateway.send(new RegisterNewReaderCommand(reader1, "John"));
		
		String book1 = identifierFactory.generateIdentifier();
		cmdGateway.send(new RegisterNewBookCommand(book1, "Game of Thrones", "George R. R. Martin"));
		
		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book1), 3000, TimeUnit.MILLISECONDS );		
		cmdGateway.send( new ReturnCommand(reader1, book1) );

		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book1), 3000, TimeUnit.MILLISECONDS );		
		cmdGateway.send( new ReturnCommand(reader1, book1) );

		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book1), 3000, TimeUnit.MILLISECONDS );		
		cmdGateway.send( new ReturnCommand(reader1, book1) );

		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book1), 3000, TimeUnit.MILLISECONDS );		
		cmdGateway.send( new ReturnCommand(reader1, book1) );

		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book1), 3000, TimeUnit.MILLISECONDS );		
		cmdGateway.send( new ReturnCommand(reader1, book1) );

		Thread.sleep(2000);
		
		BookEntry book = bookEntryRepository.findOne(book1);
		assertTrue(book.getIsHot());
		
		//TODO: clean-up needed
	}	
	
	@Test 
	public void testBorrow2Books() throws Exception{
		
		String reader1 = identifierFactory.generateIdentifier();
		cmdGateway.send(new RegisterNewReaderCommand(reader1, "John"));
		
		String book1 = identifierFactory.generateIdentifier();
		cmdGateway.send(new RegisterNewBookCommand(book1, "Game of Thrones", "George R. R. Martin"));
		
		String book2 = identifierFactory.generateIdentifier();
		cmdGateway.send(new RegisterNewBookCommand(book2, "The Importance of Living", "Lin Yutang"));

		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book1), 3000, TimeUnit.MILLISECONDS );		
		bookCommandGateway.sendAndWait( new BorrowCommand(reader1, book2), 3000, TimeUnit.MILLISECONDS );		

		Thread.sleep(2000);
		
		BookEntry book_1 = bookEntryRepository.findOne(book1);
		assertTrue(book_1.getBorrowerId().equals(reader1) );
		
		ReaderEntry reader_1 = readerEntryRepository.findOne(reader1);
		assertTrue(reader_1.getBorrowedBookIds().size()==2);
		
		//TODO: clean-up needed
	}		
}
