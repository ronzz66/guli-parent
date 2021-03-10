package com.ron.oss.Service;

import org.springframework.web.multipart.MultipartFile;

public interface Ossservice {
    String uploadFile(MultipartFile file);
}
