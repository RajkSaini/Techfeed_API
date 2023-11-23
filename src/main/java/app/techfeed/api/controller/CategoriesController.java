package app.techfeed.api.controller;

import app.techfeed.api.model.Categories;
import app.techfeed.api.model.Feed;
import app.techfeed.api.model.Users;
import app.techfeed.api.service.CategoryService;
import app.techfeed.api.service.FeedService;
import app.techfeed.api.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/categories")
public class CategoriesController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Categories> userCategories(@RequestParam String emailId){
        Users user=usersService.isExist(emailId);

        if (user != null && user.getCategories() != null) {
            String userCategories[]=user.getCategories();
            return categoryService.findByCategoryIdIn(userCategories);
        }

        return categoryService.getCategories();

    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Categories> getCategories(){
        return categoryService.getCategories();

    }


}
