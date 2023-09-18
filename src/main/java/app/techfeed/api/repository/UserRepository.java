package app.techfeed.api.repository;

import app.techfeed.api.model.Feed;
import app.techfeed.api.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {

    Users findByEmailId(String emailId);
}
