package while1.kunnect.dto.application;

import lombok.Getter;
import lombok.Setter;
import while1.kunnect.entity.Application;
import while1.kunnect.entity.ApplicationStatus;

public record ApplicationRequest (
        String comment,
        ApplicationStatus status)
{
    public ApplicationRequest(Application application) {
        this(
                application.getComment(),
                application.getStatus()
        );
    }

}
