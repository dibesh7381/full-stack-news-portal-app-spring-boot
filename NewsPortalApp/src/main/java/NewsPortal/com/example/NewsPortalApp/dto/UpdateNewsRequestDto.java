package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateNewsRequestDto {
    private MultipartFile image; // optional
    private String title;
    private String description;
}

