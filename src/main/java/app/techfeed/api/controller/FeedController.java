package app.techfeed.api.controller;

import app.techfeed.api.model.Feed;
import app.techfeed.api.repository.FeedRepository;
import app.techfeed.api.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping(value = "/feed")
public class FeedController {

String base = "/feed_images";
    @Autowired
    private FeedService feedService;

    @RequestMapping(value = "/{tag}", method = RequestMethod.GET)
    public Page<Feed> getFeed(@PathVariable String tag, @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size) throws InterruptedException {
        //TimeUnit.SECONDS.sleep(20);
        if(tag.equalsIgnoreCase("All")){
            return feedService.findAll(page,size);
        }
        return feedService.findFeedsByTag(tag,page,size);
    }

    @RequestMapping(value="/images/{image_id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String image_id) throws IOException{
        File img=null;
        String home = System.getProperty("user.home");
        System.out.println("home==>"+home);
        img= new File(home + File.separator + "techfeed" +File.separator +"feed_images" +File.separator+ image_id+".jpg");
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/jpeg")).body(Files.readAllBytes(img.toPath()));
    }

}

