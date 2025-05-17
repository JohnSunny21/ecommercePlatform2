package com.devtale.ecommerceplatform2.service.product;

import com.devtale.ecommerceplatform2.dto.ProductDto;
import com.devtale.ecommerceplatform2.model.Product;
import com.devtale.ecommerceplatform2.request.AddProductRequest;
import com.devtale.ecommerceplatform2.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest productRequest);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest productUpdateRequest,Long productId);
    List<Product> getAllProducts();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category,String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand,String name);
    Long countProductByBrandAndName(String brand,String name);
    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto covertToDto(Product product);

}
