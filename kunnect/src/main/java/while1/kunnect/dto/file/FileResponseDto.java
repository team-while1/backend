package while1.kunnect.dto.file;

public class FileResponseDto {
    private Long file_id;
    private Long post_id;
    private String file_name;
    private String file_path;

    public FileResponseDto(Long fileId, Long postId, String fileName, String filePath) {
        this.file_id = fileId;
        this.post_id = postId;
        this.file_name = fileName;
        this.file_path = filePath;
    }

    public Long getFile_id() {
        return file_id;
    }

    public Long getPost_id() {
        return post_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_path() {
        return file_path;
    }
}
