package mobi.qubits.ex.library.query;

import mobi.qubits.ex.library.domain.events.MarkBookHotEvent;
import mobi.qubits.ex.library.domain.events.NewBookRegisteredEvent;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author yizhuan
 *
 */
@Component
public class BookEventListener {

	@Autowired
	private BookEntryRepository bookEntryRepository;

	@Autowired
	private ReaderEntryRepository readerRepository;

	@EventHandler
	void on(NewBookRegisteredEvent event) {
		BookEntry book = new BookEntry();
		book.setId(event.getId());
		book.setTitle(event.getTitle());
		book.setAuthor(event.getAuthor());
		book.setBorrowerId(null);
		bookEntryRepository.save(book);
	}
	
	@EventHandler
	void on(MarkBookHotEvent event) {
		BookEntry book = bookEntryRepository.findOne(event.getId());
		book.setIsHot(true);
		bookEntryRepository.save(book);
	}	

}
