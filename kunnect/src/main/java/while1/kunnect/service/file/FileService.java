package while1.kunnect.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import while1.kunnect.entity.FileEntity;
import while1.kunnect.repository.file.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private static final String UPLOAD_PRE = "/var/www";
    private static final String UPLOAD_DIR = "/images/postfile";  // post 용 경로
    private static final String FULL_UPLOAD_PATH = UPLOAD_PRE + UPLOAD_DIR;

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileEntity> uploadFiles(Long postId, List<MultipartFile> files) throws IOException {
        List<FileEntity> uploadedFiles = new ArrayList<>();

        File directory = new File(FULL_UPLOAD_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : files) {
            String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();  // 파일명만 추출
            String filePath = Paths.get(FULL_UPLOAD_PATH, fileName).toString();

            File dest = new File(filePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();  // 중간 경로까지 생성
            }

            file.transferTo(dest);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setPostId(postId);
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath(UPLOAD_DIR + "/" + fileName);

            uploadedFiles.add(fileRepository.save(fileEntity));
        }

        return uploadedFiles;
    }

    public Optional<FileEntity> getFile(Long fileId) {
        return fileRepository.findById(fileId);
    }

    public boolean isValidFile(Long fileId) {
        return fileRepository.existsById(fileId);
    }
}
