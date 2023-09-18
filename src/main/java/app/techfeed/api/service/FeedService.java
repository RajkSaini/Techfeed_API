package app.techfeed.api.service;

import app.techfeed.api.model.Feed;
import app.techfeed.api.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    private final FeedRepository feedRepository;

    @Autowired
    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public Page<Feed> findFeedsByTag(String tag,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedRepository.findByTagsContaining(tag,pageable);
    }
    public Page<Feed> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedRepository.findAll(pageable);
    }


}