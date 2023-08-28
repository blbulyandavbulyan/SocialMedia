package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.FileCreatedResponse;
import com.blbulyandavbulyan.socialmedia.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileCreatedResponse uploadFile(@RequestParam("file") MultipartFile multipartFile, Principal principal){
        return new FileCreatedResponse(fileService.save(multipartFile, principal.getName()));
    }
    @GetMapping("/{file-uuid}")
    public ResponseEntity<Resource> getFile(@PathVariable("file-uuid") UUID fileUIID) throws IOException {
        FileService.FoundFile foundFile = fileService.getFile(fileUIID);
        ContentDisposition contentDisposition = ContentDisposition.inline().
                filename(foundFile.fileName()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        headers.setContentType(MediaType.asMediaType(MimeType.valueOf(foundFile.contentType())));
        headers.setContentLength(foundFile.resource().contentLength());
        return ResponseEntity.ok().headers(headers).body(foundFile.resource());
    }
}
