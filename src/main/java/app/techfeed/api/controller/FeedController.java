package app.techfeed.api.controller;

import app.techfeed.api.model.Feed;
import app.techfeed.api.repository.FeedRepository;
import app.techfeed.api.service.FeedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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

    @RequestMapping(value = "by-tag/{tag}", method = RequestMethod.GET)
    public Page<Feed> getFeed(@PathVariable String tag, @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size) throws InterruptedException {
        if(tag.equalsIgnoreCase("All")){
            return feedService.findAll(page,size);
        }
        return feedService.findFeedsByTag(tag,page,size);
    }

    @RequestMapping(value = "/{feedId}", method = RequestMethod.GET)
    public Feed findByFeedId(@PathVariable String feedId) throws InterruptedException {
        return feedService.findByFeedId(feedId);
    }

    @GetMapping("/feedDetail/{feedId}")
    public Object findByFeedIdForCrowler(@PathVariable String feedId, HttpServletRequest request) {
        Feed feed= feedService.findByFeedId(feedId);
        String userAgent = request.getHeader("User-Agent");

        if (userAgent != null && userAgent.contains("facebookexternalhit")) {
            String customHtml = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta property=\"og:title\" content=\""+feed.getTitle()+"\" >"+
                    "<meta property=\"og:image\" content=\"https://techfeed.app/feed/images/"+feed.getFeedId()+"\">"+
                    "  <!-- iOS meta tags & icons -->\n" +
                    "  <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                    "  <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                    "  <meta name=\"apple-mobile-web-app-title\" content=\"tech_feed\">\n" +
                    "  <link rel=\"apple-touch-icon\" href=\"icons/Icon-192.png\">\n" +
                    "  <meta name=\"google-signin-client_id\" content=\"638998345431-j3es9tc2rkl8uhp42n0m4r7p6m5rljue.apps.googleusercontent.com\">\n" +
                    "  <!-- Favicon -->\n" +
                    "  <!-- Favicon -->\n" +
                    "  <link rel=\"apple-touch-icon\" sizes=\"180x180\" href=\"apple-touch-icon.png\">\n" +
                    "  <link rel=\"icon\" type=\"image/png\" sizes=\"32x32\" href=\"favicon-32x32.png\">\n" +
                    "  <link rel=\"icon\" type=\"image/png\" sizes=\"16x16\" href=\"favicon-16x16.png\">\n" +
                    "  <link rel=\"manifest\" href=\"site.webmanifest\">\n" +
                    "  <link rel=\"mask-icon\" href=\"safari-pinned-tab.svg\" color=\"#5bbad5\">\n" +
                    "  <meta name=\"msapplication-TileColor\" content=\"#da532c\">\n" +
                    "  <meta name=\"theme-color\" content=\"#f6f6f6\">\n" +
                    "\n" +
                    "  <title>Tech Feed</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "</body>\n" +
                    "</html>";
            return customHtml;
        }
        return new RedirectView("https://www.techfeed.app?id="+feed.getFeedId());

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

