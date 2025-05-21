package while1.kunnect.dto.member;

import org.springframework.web.multipart.MultipartFile;

public record ProfileUpdateDto(
        MultipartFile image
) {}