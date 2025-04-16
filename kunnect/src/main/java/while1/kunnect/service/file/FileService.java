package while1.kunnect.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import while1.kunnect.entity.FileEntity;
import while1.kunnect.repository.file.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileEntity uploadFile(Long postId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = Paths.get(uploadDir, fileName).toString();

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();  // 하위까지 생성
        }

        File dest = new File(filePath);
        file.transferTo(dest);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setPostId(postId);
        fileEntity.setFileName(fileName);
        fileEntity.setFilePath(filePath);

        return fileRepository.save(fileEntity);
    }


    public Optional<FileEntity> getFile(Long fileId) {
        return fileRepository.findById(fileId);
    }

    public boolean isValidFile(Long fileId) {
        return fileRepository.existsById(fileId);
    }
}
