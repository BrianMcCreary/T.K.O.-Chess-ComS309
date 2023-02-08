package coms309.demo1Chess;
import org.springframework.web.bind.annotation.*;
@RestController
public class WelcomeController {
    @GetMapping("/")
    public @ResponseBody String welcome() {
        return "Welcome to chess.";
    }
}
