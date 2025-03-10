import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@SpringBootApplication
public class PetrolBusinessWebsite {
    public static void main(String[] args) {
        SpringApplication.run(PetrolBusinessWebsite.class, args);
    }
}

@Controller
class WebController {
    private static final String UPLOAD_DIR = "uploads/";

    public WebController() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

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
    
    @GetMapping("/upload")
    public String uploadPage(Model model) {
        model.addAttribute("title", "Upload Files");
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "upload";
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            model.addAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        return "upload";
    }

    @GetMapping("/files")
    public String listUploadedFiles(Model model) {
        try (Stream<Path> paths = Files.walk(Paths.get(UPLOAD_DIR), 1)) {
            model.addAttribute("files", paths.filter(path -> !path.equals(Paths.get(UPLOAD_DIR)))
                    .map(path -> path.getFileName().toString()).toArray(String[]::new));
        } catch (IOException e) {
            model.addAttribute("message", "Failed to list files");
        }
        return "files";
    }

    @GetMapping("/files/{filename}")
    @ResponseBody
    public Resource getFile(@PathVariable String filename) throws IOException {
        Path file = Paths.get(UPLOAD_DIR).resolve(filename);
        return new UrlResource(file.toUri());
    }
}
