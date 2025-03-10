import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class PetrolBusinessWebsite {
    public static void main(String[] args) {
        SpringApplication.run(PetrolBusinessWebsite.class, args);
    }
}

@Controller
class WebController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Welcome to Petrol Corp");
        return "home";
    }
    
    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("title", "Our Services");
        return "services";
    }
    
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact Us");
        return "contact";
    }
}