package app.techfeed.api.repository;

import app.techfeed.api.model.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends MongoRepository<Feed, String> {
    Page<Feed> findByTagsContaining(String tag,Pageable pageable);

    List<Feed> findFeedByFeedId(String feedId);

    Page<Feed> findAll(Pageable pageable);

}
