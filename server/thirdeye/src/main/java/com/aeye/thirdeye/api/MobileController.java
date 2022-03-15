package com.aeye.thirdeye.api;

import com.aeye.thirdeye.entity.Image;
import com.aeye.thirdeye.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/mo")
public class MobileController {

    @Autowired
    ImageService imageService;

    @PostMapping
    public ResponseEntity<?> insertImage(@RequestPart(value = "file", required = false) MultipartFile file, @RequestBody Image image){

        return null;
    }
}
