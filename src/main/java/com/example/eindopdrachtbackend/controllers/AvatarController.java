package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.services.AvatarService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;

import static com.example.eindopdrachtbackend.controllers.UserProfileController.getResourceResponseEntity;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/**")
    public ResponseEntity<Resource> getAvatar(HttpServletRequest request) throws IOException {
        // Extract the file path from the request
        String requestUrl = request.getRequestURI();
        String filePath = requestUrl.substring("/avatars/".length());

        Path path = avatarService.getAvatarPath(filePath);

        return getResourceResponseEntity(path);
    }
}
