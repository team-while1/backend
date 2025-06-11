package while1.kunnect.dto.post;

import lombok.Data;
import org.hibernate.mapping.List;
import while1.kunnect.entity.PostStatus;

import java.time.LocalDate;

@Data
public class CreatePostRequest {
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalSlots;
    private String categoryId;
    //private Boolean archived; // 변수 변경 -> status
}

