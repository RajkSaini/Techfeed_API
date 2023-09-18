package app.techfeed.api.controller;

import app.techfeed.api.model.Categories;
import app.techfeed.api.model.Users;
import app.techfeed.api.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

     @Autowired
    UsersService usersService;

     @RequestMapping(value = "/exist")
     public ResponseEntity<Users> isExist( @RequestParam String emailId){
        Users user = usersService.isExist(emailId);
         return ResponseEntity.ok(user);
     }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<Users> updateCategories(@RequestBody Map<String, Object> requestBody) {
        String emailId = (String) requestBody.get("emailId");
        ArrayList<String> categories = (ArrayList<String>) requestBody.get("categories");

        if (emailId == null || categories == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Convert ArrayList to an array if needed
        String[] categoriesArray = categories.toArray(new String[0]);
        Users users= usersService.updateUserCategories(emailId, categoriesArray);
         if(users == null){
            return  ResponseEntity.internalServerError().body(null);
         }
         return ResponseEntity.ok(users);
    }
}
