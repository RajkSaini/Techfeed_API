package app.techfeed.api.controller;

import app.techfeed.api.model.Categories;
import app.techfeed.api.model.Feed;
import app.techfeed.api.service.CategoryService;
import app.techfeed.api.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/categories")
public class CategoriesController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Categories> getCategories(){
        return categoryService.getCategories();

    }

}
