package NewsPortal.com.example.NewsPortalApp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateNewsRequestDto {
    private MultipartFile image;
    private String title;
    private String description;
}
