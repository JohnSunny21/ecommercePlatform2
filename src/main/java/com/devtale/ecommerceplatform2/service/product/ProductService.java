package com.devtale.ecommerceplatform2.service.product;

import com.devtale.ecommerceplatform2.dto.ImageDto;
import com.devtale.ecommerceplatform2.dto.ProductDto;
import com.devtale.ecommerceplatform2.exceptions.AlreadyExistsException;
import com.devtale.ecommerceplatform2.exceptions.ProductNotFoundException;
import com.devtale.ecommerceplatform2.exceptions.ResourceNotFoundException;
import com.devtale.ecommerceplatform2.model.Category;
import com.devtale.ecommerceplatform2.model.Image;
import com.devtale.ecommerceplatform2.model.Product;
import com.devtale.ecommerceplatform2.repository.CategoryRepository;
import com.devtale.ecommerceplatform2.repository.ImageRepository;
import com.devtale.ecommerceplatform2.repository.ProductRepository;
import com.devtale.ecommerceplatform2.request.AddProductRequest;
import com.devtale.ecommerceplatform2.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Override
    public Product addProduct(AddProductRequest productRequest) {
        if(productExists(productRequest.getName(),productRequest.getBrand())){
            throw new AlreadyExistsException(productRequest.getName() + " "+productRequest.getBrand()+ " already exists, you may update this product instead!");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(productRequest.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        productRequest.setCategory(category);
        return productRepository.save(createProduct(productRequest,category));
    }

    private boolean productExists(String name,String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest request,Category category){
        return  Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .price(request.getPrice())
                .inventory(request.getInventory())
                .description(request.getDescription())
                .category(category)
                .build();

    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,() -> {throw new ProductNotFoundException("Product not found!");});

    }

    @Override
    public Product updateProduct(ProductUpdateRequest productUpdateRequest, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,productUpdateRequest))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct,ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
                .map(this::covertToDto)
                .toList();
    }

    @Override
    public ProductDto covertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
