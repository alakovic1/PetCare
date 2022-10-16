package ba.unsa.etf.nwt.pet_category_service.service;

import ba.unsa.etf.nwt.pet_category_service.exception.ResourceNotFoundException;
import ba.unsa.etf.nwt.pet_category_service.exception.WrongInputException;
import ba.unsa.etf.nwt.pet_category_service.model.Category;
import ba.unsa.etf.nwt.pet_category_service.model.Rase;
import ba.unsa.etf.nwt.pet_category_service.repository.CategoryRepository;
import ba.unsa.etf.nwt.pet_category_service.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    private CommunicationsService communicationsService;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public ResponseMessage addCategory(Category category) {
        try {
            Category c = findCategoryByName(category.getName());
            throw new WrongInputException("Category with name " + c.getName() + " already exists!");
        } catch (ResourceNotFoundException e) {
            Category category1 = categoryRepository.save(category);

            Rase rase = new Rase();
            rase.setName("Other");
            rase.setDescription("");
            rase.setCategory(category1);
            communicationsService.saveRase(rase);

            return new ResponseMessage(true, HttpStatus.OK,"Category added successfully!");
        }
    }

    public void saveCategory(Category c) {
        categoryRepository.save(c);
    }


    public Category getCategory(Long categoryID) {
        Category category = getCategoryById(categoryID);
        return category;

    }

    public Category getCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No category with ID " + id));
    }


    public ResponseMessage deleteCategory(Long id) {
        Category c = getCategoryById(id);

        communicationsService.deleteRaseCascade(id);

        categoryRepository.deleteById(id);
        return new ResponseMessage(true, HttpStatus.OK,"Category successfully deleted!");
    }

    public Category findCategoryByName(String name) {
        return categoryRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("No category with name " + name));
    }

    public Category getCategoryByName(String name) {
        //if(name == null) return new CategoryResponse(null, "Add a name for search!", "BAD_REQUEST", false);
        Category c = findCategoryByName(name);
        return c;
    }

    public Category updateCategory(Long id, Category newCategory) {
        Category c = getCategory(id);
        try {
            Category cr1 = getCategoryByName(newCategory.getName());
            //ako vec postoji kateogrija sa tim imenom vracamo response da postoji vec ta kategorija
            throw new WrongInputException("Category with that name already exists!");
        } catch (ResourceNotFoundException e){
            //ako nije nadjena kategorija sa tim imenom
            c.setDescription(newCategory.getDescription());
            c.setName(newCategory.getName());
            categoryRepository.save(c);
            return c;
        }
    }

    public List<Category> filterByNameContains(String name){

        List<Category> categories = new ArrayList<>();
        List<Category> allcategories = getCategories();

        for(Category category : allcategories){
            if(category.getName().toLowerCase().contains(name.toLowerCase())){
                categories.add(category);
            }
        }

        return categories;
    }
}
