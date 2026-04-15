package demo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dependency")
public class DependencyController {
    @GetMapping("/data")
    public String getData(@RequestParam(required = false) String fail) throws InterruptedException {
        if ("timeout".equals(fail)) {
            Thread.sleep(5000);
            return "OK after delay";
        }
        if ("error".equals(fail)) {
            throw new RuntimeException("Simulated error");
        }
        return "OK: " + System.currentTimeMillis();
    }
}