package hello;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/")
public class ErrController implements ErrorController{

private static final String PATH = "/error";
//
//@GetMapping(value=PATH)
//    public String error(Model model) {
//		System.out.println("========OMEGA=========");
//        return "200";//"error/error404";
//    }
//
//@Override
//    public String getErrorPath() {
//		System.out.println("========OMEGALUL=========");
//        return PATH;
//    }

@Override
public String getErrorPath() {
    return "/error";
}

@RequestMapping("/error")
public String handleError(HttpServletRequest request) {
    // get error status
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.OK);
    //request.setAttrubute(RequestDispatcher.ERROR_MESSAGE, "F");
    //request.setAttribute("STATUS", );
    
    // TODO: log error details here
    return "200";
//    if (status != null) {
//        int statusCode = Integer.parseInt(status.toString());
//
//        // display specific error page
//        if (statusCode == HttpStatus.NOT_FOUND.value()) {
//            return "404";
//        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//            return "500";
//        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
//            return "403";
//        }
//    }
//
//    // display generic error
//    return "error";
}
	

}