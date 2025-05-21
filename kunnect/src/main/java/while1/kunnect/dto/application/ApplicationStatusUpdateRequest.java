package while1.kunnect.dto.application;

import while1.kunnect.entity.ApplicationStatus;

public record ApplicationStatusUpdateRequest(
        ApplicationStatus status
) {}