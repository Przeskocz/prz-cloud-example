package prz.cloud.example.cutting.stock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHomePage(){
        System.out.println("WebController::getHomePage");
        return "index";
    }
}
