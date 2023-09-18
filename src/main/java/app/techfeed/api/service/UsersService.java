package app.techfeed.api.service;

import app.techfeed.api.model.Users;
import app.techfeed.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    public Users updateUserCategories(String emailID, String categories[]){
        Users user=userRepository.findByEmailId(emailID);
        System.out.println("user==>"+user);
        if(user == null){
           Users newUser= new Users();
            newUser.setEmailId(emailID);
            newUser.setCategories(categories);
            userRepository.save(newUser);
        }else{
            user.setCategories(categories);
            return userRepository.save(user);
        }
        return null;

    }

    public Users isExist(String emailID){
        Users user=userRepository.findByEmailId(emailID);
        if(user != null){
            return user;
        }
        return null;
    }

}
