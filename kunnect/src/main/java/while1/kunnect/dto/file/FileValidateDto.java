package while1.kunnect.dto.file;

public class FileValidateDto {
    private boolean is_valid;

    public FileValidateDto(boolean isValid) {
        this.is_valid = isValid;
    }

    public boolean isIs_valid() {
        return is_valid;
    }
}