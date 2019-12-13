package zeljko.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * HomeController
 */
@RestController
 public class HomeController {

    // kad hocemo samo obican string da vratimo korisniku na browser
    
    @GetMapping("/")
    public String homeString() {                
        return "Welcome to App !!!";
    }
    
    /* kad hocemo string formatiran sa html da vratimo korisniku na browser

    @GetMapping("/")
    public String homeStringHtml() {                
        return "<h1>Welcome to App !!!</h1>";
    }
    
    */

    /* kad hocemo neku html stranicu da vratimo korisniku na browser (stranica po defaultu treba da je na /resources/static)
    
    @GetMapping("/")
    public ModelAndView homeHtml() {
        ModelAndView pagename = new ModelAndView();
        pagename.setViewName("index.html");              
        return pagename;
    }

    */
    
}