package com.devtale.ecommerceplatform2.service.image;

import com.devtale.ecommerceplatform2.dto.ImageDto;
import com.devtale.ecommerceplatform2.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file,Long imageId);
}
