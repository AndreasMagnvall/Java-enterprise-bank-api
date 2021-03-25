package hello;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/*
 * 
 * @RestController
@RequestMapping("/")

 */
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
	
//	Vi kan inte Override:a denna
// @Override
//	protected ResponseEntity<Object> handlerException(Exception ex, WebRequest request) {
//		
//	}
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(final Exception ex, final WebRequest request) {
       return new ResponseEntity<>("SDSDSD", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
//        // prepare responseEntity
//    	System.out.println("========7777=========");
//    	String message = "";
//        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
//        return res;
//    }

    @Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
    	System.out.println("========x=========");
    	String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
	}


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
    	System.out.println("========x=========");
    	
    	String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
	}


	@Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
    		org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
    	System.out.println("========x=========");
    	String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
    }
    
    
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
    		org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
    	System.out.println("========y=========");
    	String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
    }
    

    
    
    @Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
    	System.out.println("========zwwwwww=========");
		return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
	}


	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("========wqeqeeqqe=========");
		return super.handleHttpMessageNotReadable(ex, headers, status, request);
	}


	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("========wqw=========");
		return super.handleHttpMessageNotWritable(ex, headers, status, request);
	}


	@Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
    		org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
    	// TODO Auto-generated method stub
    	System.out.println("========z=========");
        String message = "FAILED";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
    	//return super.handleMissingServletRequestParameter(ex, headers, status, request);
    } 
    
//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
//    		HttpStatus status, WebRequest request) {
// 
//        String message = "";
//        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
//        return res;
//    }


	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("========O=========");
		String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
	}


	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		System.out.println("========Ä=========");
        String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
	}


	@Override
	@ResponseStatus(HttpStatus.OK)
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		System.out.println("========Ö=========");
        String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		System.out.println("========OMEGA=========");
        String message = "";
        ResponseEntity<Object> res = new ResponseEntity<Object>(message, HttpStatus.OK);
        return res;
	}
    
}
