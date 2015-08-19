package mobi.qubits.ex.library.api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import mobi.qubits.ex.library.api.requests.BookRequest;
import mobi.qubits.ex.library.api.requests.RegisterReaderRequest;
import mobi.qubits.ex.library.domain.BookCommandGateway;
import mobi.qubits.ex.library.domain.BorrowingSameBookException;
import mobi.qubits.ex.library.domain.MaxAllowanceExceededException;
import mobi.qubits.ex.library.domain.commands.BorrowCommand;
import mobi.qubits.ex.library.domain.commands.RegisterNewReaderCommand;
import mobi.qubits.ex.library.domain.commands.MakeReservationCommand;
import mobi.qubits.ex.library.domain.commands.ReturnCommand;
import mobi.qubits.ex.library.query.BookEntry;
import mobi.qubits.ex.library.query.BookEntryRepository;
import mobi.qubits.ex.library.query.ReaderEntry;
import mobi.qubits.ex.library.query.ReaderEntryRepository;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.DefaultIdentifierFactory;
import org.axonframework.domain.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * @author yizhuan
 *
 */
@RestController
public class ReaderController {

	private final IdentifierFactory identifierFactory = new DefaultIdentifierFactory();

	@Autowired
	private BookEntryRepository bookEntryRepository;

	@Autowired
	private ReaderEntryRepository readerEntryRepository;

	@Autowired
	private CommandGateway cmdGateway;	
	
	@Autowired
	private 
	BookCommandGateway bookCommandGateway;
	
	@RequestMapping(value = "/api/readers", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<?> registerNewReader(
			@RequestBody @Valid RegisterReaderRequest req, UriComponentsBuilder b) {
		String id = identifierFactory.generateIdentifier();
		cmdGateway.send(new RegisterNewReaderCommand(id, req.getName()));

		UriComponents uriComponents = b.path("/api/readers/{id}")
				.buildAndExpand(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponents.toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/api/readers", method = RequestMethod.GET)
	public @ResponseBody List<ReaderEntry> findAllReaders() {
		return readerEntryRepository.findAll();
	}	
	
	@RequestMapping(value = "/api/readers/{id}", method = RequestMethod.GET)
	public @ResponseBody ReaderEntry findReader(@PathVariable String id) {
		return readerEntryRepository.findOne(id);
	}	

	
	@RequestMapping(value = "/api/readers/{id}/makeReservation", method = RequestMethod.POST)
	public ResponseEntity<?> makeReservation(@RequestBody @Valid BookRequest req, @PathVariable String id) {
		
		BookEntry book = bookEntryRepository.findOne(req.getBookId());
		if (book==null){
			return errorResponse("Book not found.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}

		String reservedByBorrowerId = book.getReservedByBorrowerId();
		
		if (reservedByBorrowerId==null){
			cmdGateway.sendAndWait(new MakeReservationCommand(id, req.getBookId()), 3000, TimeUnit.MILLISECONDS);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		if (id.equals(reservedByBorrowerId)){
			return errorResponse("You've already reserved the book.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}
		else{
			return errorResponse("This book has been reserved by another reader.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}		
	}
	
	@RequestMapping(value = "/api/readers/{id}/cancelReservation", method = RequestMethod.POST)
	public ResponseEntity<?> cancelReservation(@RequestBody @Valid BookRequest req, @PathVariable String id) {
		BookEntry book = bookEntryRepository.findOne(req.getBookId());
		if (book==null){
			return errorResponse("Book not found.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}

		String reservedByBorrowerId = book.getReservedByBorrowerId();
		
		if (reservedByBorrowerId==null){
			return errorResponse("You didn't reserve this book.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}
		else{
			if (id.equals(reservedByBorrowerId)){
				cmdGateway.sendAndWait(new MakeReservationCommand(id, req.getBookId()), 3000, TimeUnit.MILLISECONDS);
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);					
			}
			else{
				return errorResponse("This book has been reserved by another reader.",req.getBookId(), HttpStatus.BAD_REQUEST);
			}					
		}		
	}	
	
	
	@RequestMapping(value = "/api/readers/{id}/borrow", method = RequestMethod.POST)
	public ResponseEntity<?> borrowBook(@RequestBody @Valid BookRequest req, @PathVariable String id) {
		
		BookEntry book = bookEntryRepository.findOne(req.getBookId());
		if (book==null){
			return errorResponse("Book not found.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}
		
		String reservedByBorrowerId = book.getReservedByBorrowerId();
		
		if (reservedByBorrowerId==null){
			return errorResponse("You need to reserve the book first.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}
		
		if (!id.equals(reservedByBorrowerId)){		
			return errorResponse("This book has been reserved by another reader.",req.getBookId(), HttpStatus.BAD_REQUEST);
		}			
		
		if (book.isBorrowed()){
			return errorResponse("This book is not available. It has been taken by another reader.", 
					req.getBookId(), HttpStatus.BAD_REQUEST);
		}
		
		try {			
			bookCommandGateway.sendAndWait(new BorrowCommand(id, req.getBookId()), 3000, TimeUnit.MILLISECONDS);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);			
		}
		catch (BorrowingSameBookException e) {			
			return errorResponse(e.getLocalizedMessage(), req.getBookId(), HttpStatus.BAD_REQUEST);				
		}
		catch (MaxAllowanceExceededException e) {
			return errorResponse(e.getLocalizedMessage(), null, 		   HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);						
		}
		
		
		/*
		
		//Works using a workaround that relies on Axon wraps the exceptions
		
		try {
			cmdGateway.sendAndWait(new BorrowCommand(id, req.getBookId()), 3000, TimeUnit.MILLISECONDS);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (CommandExecutionException ex) {
			
			//e.printStackTrace();
			
			Throwable e = ex.getCause();
			
			if (e instanceof BookAlreadyTakenException){
				return errorResponse(e.getLocalizedMessage(), req.getBookId(), HttpStatus.BAD_REQUEST);
			} 
			else if (e instanceof BorrowingSameBookException){
				return errorResponse(e.getLocalizedMessage(), req.getBookId(), HttpStatus.BAD_REQUEST);
			} else if (e instanceof MaxAllowanceExceededException){
				return errorResponse(e.getLocalizedMessage(), null, HttpStatus.BAD_REQUEST);
			} 
			else {
				return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		*/
				
	}
	
	@RequestMapping(value = "/api/readers/{id}/return", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void returnBook(@RequestBody @Valid BookRequest req, @PathVariable String id) {
		cmdGateway.send(new ReturnCommand(id, req.getBookId()));
	}	

	
	private ResponseEntity<String> errorResponse(String errMsg, String bookId, HttpStatus httpStatus){
		String err = "{"
				+"\"error\":\""+errMsg + "\""
				+ (bookId ==null ? "":", \"bookId\":\""+bookId+"\"")
				+"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(err, headers, httpStatus);			
	}
}
