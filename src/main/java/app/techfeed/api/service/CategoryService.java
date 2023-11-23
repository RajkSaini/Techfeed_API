package app.techfeed.api.service;

import app.techfeed.api.model.Categories;
import app.techfeed.api.model.Feed;
import app.techfeed.api.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public List<Categories> getCategories(){
       return categoriesRepository.findAll();
    }

    public List<Categories> findByCategoryIdIn(String[] categoryIds){
        return categoriesRepository.findByCategoryIdIn(categoryIds);
    }
}
