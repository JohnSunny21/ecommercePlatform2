package com.devtale.ecommerceplatform2.service.category;

import com.devtale.ecommerceplatform2.exceptions.AlreadyExistsException;
import com.devtale.ecommerceplatform2.exceptions.ResourceNotFoundException;
import com.devtale.ecommerceplatform2.model.Category;
import com.devtale.ecommerceplatform2.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;


    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * ✅ .filter() works on Optional<T>, even without streams.
     * ✅ It applies conditional checks before proceeding with .map().
     * ✅ No need for streams because we're handling a single object instead of a list.
     * ✅ This approach makes the code cleaner and null-safe, avoiding explicit checks.
     */
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter( c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName()+ " already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}
