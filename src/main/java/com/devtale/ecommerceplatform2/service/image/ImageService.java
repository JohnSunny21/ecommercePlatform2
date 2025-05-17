package com.devtale.ecommerceplatform2.service.image;

import com.devtale.ecommerceplatform2.dto.ImageDto;
import com.devtale.ecommerceplatform2.exceptions.ResourceNotFoundException;
import com.devtale.ecommerceplatform2.model.Image;
import com.devtale.ecommerceplatform2.model.Product;
import com.devtale.ecommerceplatform2.repository.ImageRepository;
import com.devtale.ecommerceplatform2.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;


    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()-> {
            throw  new ResourceNotFoundException("No image found with id: " + id);
        });

    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(file.getBytes());
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";

                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownlaodUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownlaodUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownlaodUrl());
                savedImageDto.add(imageDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(file.getBytes());
            imageRepository.save(image);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

