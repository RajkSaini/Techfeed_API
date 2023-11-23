package app.techfeed.api.repository;


import app.techfeed.api.model.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends MongoRepository<Categories, String> {
    List<Categories> findAll();

    List<Categories> findByCategoryIdIn(String[] categoryIds);

}
