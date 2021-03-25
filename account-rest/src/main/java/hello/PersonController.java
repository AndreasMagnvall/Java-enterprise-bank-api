
// package hello;

// import java.util.concurrent.atomic.AtomicLong;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class PersonController {

//     private static final String template = "Hello, %s!";
//     private final AtomicLong counter = new AtomicLong();

//     @RequestMapping("/greeting")
//     public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
//         return new Greeting(counter.incrementAndGet(),
//                             String.format(template, name));
//     }

//     @RequestMapping("/list")
//     public ArrayList<Person> list() {
        
//     }

//     @RequestMapping("/find.name")
//     public ArrayList<Person> findByName(@RequestParam(value="name") String name) {
        
//     }
    
//     @RequestMapping("/find.key")
//     public Person findByKey(@RequestParam(value="key") String key) {
        
//     }
// }
