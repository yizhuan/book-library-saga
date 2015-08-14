package mobi.qubits.ex.library.query;

import mobi.qubits.ex.library.domain.events.BorrowEvent;
import mobi.qubits.ex.library.domain.events.NewReaderRegisteredEvent;
import mobi.qubits.ex.library.domain.events.ReturnEvent;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author yizhuan
 *
 */
@Component
public class ReaderEventListener {
	@Autowired
	private BookEntryRepository bookEntryRepository;

	@Autowired
	private ReaderEntryRepository readerRepository;

	@EventHandler
	void on(NewReaderRegisteredEvent event) {
		
		ReaderEntry reader = new ReaderEntry();
		reader.setId(event.getId());
		reader.setName(event.getName());
		readerRepository.save(reader);
	}
	
	
	@EventHandler
	void on(BorrowEvent event) {

		BookEntry book = bookEntryRepository.findOne(event.getBookId());
		
		ReaderEntry reader = readerRepository.findOne(event.getBorrowerId());
		
		book.setBorrowerId(reader.getId());
		book.setBorrowed(true);		
		bookEntryRepository.save(book);
				
		reader.addBorrowedBook(book.getId());		
		readerRepository.save(reader);
	}
	
	
	@EventHandler
	void on(ReturnEvent event) {
		
		BookEntry book = bookEntryRepository.findOne(event.getBookId());
		
		ReaderEntry reader = readerRepository.findOne(book.getBorrowerId());
		if (reader!=null){
			reader.removeBorrowedBook(book.getId());		
			readerRepository.save(reader);
		}
		
		book.setBorrowed(false);
		book.setBorrowerId(null);				
		bookEntryRepository.save(book);
				
	}
	

}
