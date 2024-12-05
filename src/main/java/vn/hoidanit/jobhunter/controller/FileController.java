package vn.hoidanit.jobhunter.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.FileException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseUri;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam("folder") String folder,
                             @RequestParam("file") MultipartFile file) throws URISyntaxException, IOException, FileException {
        // validate
        if(file == null || file.isEmpty()) {
            throw new FileException("File is empty");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedUploadFile = List.of("png", "jpeg", "jpg", "gif", "pdf", "doc");
        boolean isValid = allowedUploadFile.stream().anyMatch(fileName::endsWith);
        if(!isValid) {
            throw new FileException("File is not valid. Only accept " + allowedUploadFile.toString());
        }
        // create directory
        this.fileService.createDirectory(baseUri + folder);
        // store file and return
        return ResponseEntity.ok().body(this.fileService.convertToUploadFileDTO(this.fileService.storeFile(file, folder)));
    }
}
