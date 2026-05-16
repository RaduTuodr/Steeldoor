package org.example.steeldoor.controller;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.service.GcsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final GcsService gcsService;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file)
            throws IOException {

        return gcsService.upload(file);
    }
}
