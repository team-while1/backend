package while1.kunnect.dto.post;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePostRequest {
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalSlots;
    private String categoryId;
    private Boolean archived;
}
