package com.thkoeln.passwordskey.image.application;


import com.thkoeln.passwordskey.image.domain.Image;
import com.thkoeln.passwordskey.image.domain.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;


    public String save(MultipartFile file) throws NullPointerException {
        Image image = Image.buildImage(file);
        if (image == null)
            throw new NullPointerException("Image Data NULL");
        return imageRepository.save(image).getId();
    }

    public void deleteFile(String fileId) {
        imageRepository.deleteById(fileId);
    }

    public Image findById(String id) {
        return this.imageRepository.findById(id).orElse(null);
    }


}
