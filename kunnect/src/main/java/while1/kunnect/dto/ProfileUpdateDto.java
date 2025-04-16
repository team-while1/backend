package while1.kunnect.dto;

import org.springframework.web.multipart.MultipartFile;

public record ProfileUpdateDto(
        MultipartFile image
) {}