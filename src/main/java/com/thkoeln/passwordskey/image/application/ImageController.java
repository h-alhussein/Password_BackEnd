package com.thkoeln.passwordskey.image.application;


import com.thkoeln.passwordskey.image.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable String id) {
        Image image = imageService.findById(id);
        if (image==null){
            return new ResponseEntity<>("noPhoto", HttpStatus.NOT_FOUND);
        }


        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }


    @GetMapping("/{width}/{height}/{id:.+}")
    public ResponseEntity<byte[]> getScaledImage(@PathVariable int width, @PathVariable int height,
                                                 @PathVariable String id) throws Exception {
        Image image = imageService.findById(id);
        image.scale(width, height);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }


}
