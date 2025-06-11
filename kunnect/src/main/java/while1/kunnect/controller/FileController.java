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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<List<FileEntity>> uploadMultipleFiles(
            @RequestParam("postId") Long postId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        List<FileEntity> savedFiles = fileService.uploadFiles(postId, files);
        return ResponseEntity.ok(savedFiles);
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

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<FileResponseDto>> getFilesByPostId(@PathVariable Long postId) {
        List<FileEntity> files = fileService.getFilesByPostId(postId);
        List<FileResponseDto> result = files.stream()
                .map(file -> new FileResponseDto(
                        file.getFileId(),
                        file.getPostId(),
                        file.getFileName(),
                        file.getFilePath()
                ))
                .toList();
        return ResponseEntity.ok(result);
    }

}