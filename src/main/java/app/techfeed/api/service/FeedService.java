package app.techfeed.api.service;

import app.techfeed.api.model.Categories;
import app.techfeed.api.model.Feed;
import app.techfeed.api.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {
    private final FeedRepository feedRepository;

    @Autowired
    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public Page<Feed> findFeedsByTag(List<String> tags,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedRepository.findByTagsInOrderByFeedIdDesc(tags,pageable);
    }
    public Page<Feed> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedRepository.findAll(pageable);
    }

    public List<Feed> findAll() {
        return feedRepository.findAll();
    }

    public Feed findByFeedId(String feedId) {

        List<Feed> feeds= feedRepository.findFeedByFeedId(feedId);
        System.out.println("feed =?" +feeds);
        if(feeds.size()==0){
            return null;
        }
        return feeds.get(0);
    }


}