package while1.kunnect.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import while1.kunnect.dto.file.FileResponseDto;
import while1.kunnect.dto.file.FileValidateDto;
import while1.kunnect.entity.FileEntity;
import while1.kunnect.service.file.FileService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<FileResponseDto> uploadFile(
            @RequestParam("post_id") Long postId,
            @RequestParam("file") MultipartFile file) throws IOException {
        FileEntity saved = fileService.uploadFile(postId, file);
        FileResponseDto response = new FileResponseDto(
                saved.getFileId(),
                saved.getPostId(),
                saved.getFileName(),
                saved.getFilePath()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Object> getFile(@PathVariable Long fileId) {
        return fileService.getFile(fileId)
                .<ResponseEntity<Object>>map(file -> ResponseEntity.ok(new FileResponseDto(
                        file.getFileId(),
                        file.getPostId(),
                        file.getFileName(),
                        file.getFilePath()
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found"));
    }


    @GetMapping("/validate")
    public ResponseEntity<FileValidateDto> validateFile(@RequestParam("file_id") Long fileId) {
        boolean isValid = fileService.isValidFile(fileId);
        return ResponseEntity.ok(new FileValidateDto(isValid));
    }
}