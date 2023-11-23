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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping(value = "/feed")
public class FeedController {
    private static final Pattern LINKEDIN_BOT_USER_AGENT_PATTERN = Pattern.compile("LinkedInBot");
    private static final Pattern LINKEDIN_REFERRER_PATTERN = Pattern.compile(".*linkedin.com.*");
String base = "/feed_images";
    @Autowired
    private FeedService feedService;

    @RequestMapping(value = "by-tag", method = RequestMethod.POST)
    public Page<Feed> getFeed(@RequestBody Map<String, Object> requestBody) throws InterruptedException {
        List<String> tags = (List<String>) requestBody.get("tags");
        int page = (int)requestBody.get("page");
        int size = 5;
        if(tags==null || tags.size()==0){
            return feedService.findAll(page,size);
        }
        return feedService.findFeedsByTag(tags,page,size);
    }

    @RequestMapping(value = "/{feedId}", method = RequestMethod.GET)
    public Feed findByFeedId(@PathVariable String feedId) throws InterruptedException {
        return feedService.findByFeedId(feedId);
    }

    @GetMapping("/sitemap.xml")
    @ResponseBody
    public String generateSitemap() {
        System.out.println("Generating sitemap...");
        // Generate your sitemap content dynamically or load from a file
        // For simplicity, let's assume a static sitemap

        List<Feed> feeds = feedService.findAll();
        LocalDate currentDate = LocalDate.now();

        // Format the date as a string in "yyyy-MM-dd" format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        String siteMap="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n " +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n";
        for (Feed feed : feeds) {
            siteMap+= "<url>\n" +
                    "<loc>https://www.techfeed.app/feed/feedDetail/"+feed.getFeedId()+"</loc>\n" +
                    "<lastmod>"+formattedDate+"</lastmod>\n" +
                    "<changefreq>weekly</changefreq>\n" +
                    "<priority>1.0</priority>\n" +
                    "</url>\n";
        }
        siteMap+= "</urlset>";
        return siteMap;

    }

    @GetMapping("/feedDetail/{feedId}")
    public Object findByFeedIdForCrowler(@PathVariable String feedId, HttpServletRequest request) {
        Feed feed= feedService.findByFeedId(feedId);
        String userAgent = request.getHeader("User-Agent");

        if ((userAgent != null && userAgent.contains("facebookexternalhit")) ||userAgent.contains("WhatsApp/") ) {
            String customHtml = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta property=\"og:title\" content=\""+feed.getTitle()+"\" >"+
                    "<meta property=\"og:url\" content=\"https://www.techfeed.app/feed/feedDetail/"+feed.getFeedId()+"\">"+
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
                    "  <title>"+feed.getTitle()+"</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "</body>\n" +
                    "</html>";
            return customHtml;
        }
        if(userAgent.toLowerCase().contains("googlebot")){
            String customHtml = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
                    "<meta property=\"og:title\" content=\""+feed.getTitle()+"\" >"+
                    "<meta property=\"og:url\" content=\"https://www.techfeed.app/feed/feedDetail/"+feed.getFeedId()+"\">"+
                    "<meta property=\"og:image\" content=\"https://techfeed.app/feed/images/"+feed.getFeedId()+"\">"+
                    "<meta property=\"og:description\" content=/"+feed.getDetail()+"\">"+
                    "<title>"+feed.getTitle()+"</title>\n" +
                    "<meta property=\"og:type\" content=\"website\">\n" +
                    "<meta property=\"og:locale\" content=\"en_US\">\n" +
                    "<meta property=\"og:site_name\" content=\"Tech Feed\">"+
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
    public static boolean isLinkedInCrawlerRequest(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        Matcher userAgentMatcher = LINKEDIN_BOT_USER_AGENT_PATTERN.matcher(userAgent);

        return userAgentMatcher.find() ;
    }
}

