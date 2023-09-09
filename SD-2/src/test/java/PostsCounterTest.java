import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;
import com.vk.api.sdk.objects.wall.WallpostFull;

import vk.VkService;

public class PostsCounterTest {

    private static final Log LOG = LogFactory.getLog(PostsCounterTest.class);
    private static final String HASHTAG = "футбол";

    @Mock
    private VkService vkService;

    private PostsCounter postsCounter;

    private UserActor actor;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        postsCounter = new PostsCounterImpl(vkService);

        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/test/resources/user.properties"));
            actor = new UserActor(Integer.parseInt(properties.getProperty("id")), properties.getProperty("token"));
        } catch (IOException e) {
            LOG.error("Cannot load properties file", e);
            throw e;
        }
    }

    @Test
    public void testMockSimple() {
        Mockito.when(
            vkService.executeNewsfeedSearchQuery(actor, HASHTAG,
                TimeUtils.hoursToSeconds(10) + 5 * 60, TimeUtils.hoursToSeconds(12) + 5 * 60)
        ).thenReturn(
            Optional.of(
                getMockSimpleResponse()
            )
        );

        // testing 'last 2 hours'
        int[] result = postsCounter.countPostsWithHashtag(HASHTAG,
            TimeUtils.hoursToDateTime(10).plusMinutes(5), TimeUtils.hoursToDateTime(12).plusMinutes(5), actor);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new int [] { 4, 5 }, result);
    }

    @Test
    public void testMockEmpty() {
        Mockito.when(
            vkService.executeNewsfeedSearchQuery(actor, HASHTAG, TimeUtils.hoursToSeconds(2), TimeUtils.hoursToSeconds(2))
        ).thenReturn(
            Optional.of(
                new SearchResponse().setItems(Collections.emptyList()).setCount(0)
            )
        );

        // testing 'last 0 hours'
        int[] result = postsCounter.countPostsWithHashtag(HASHTAG, TimeUtils.hoursToDateTime(2), TimeUtils.hoursToDateTime(2), actor);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(new int [] {}, result);
    }

    @Test
    public void testMockManyHours() {
        Mockito.when(
            vkService.executeNewsfeedSearchQuery(actor, HASHTAG,
                TimeUtils.hoursToSeconds(100) + 10 * 60, TimeUtils.hoursToSeconds(200) + 10 * 60)
        ).thenReturn(
            Optional.of(
                getMockManyHoursResponse()
            )
        );

        // testing 'last 100 hours'
        int[] result = postsCounter.countPostsWithHashtag(HASHTAG,
            TimeUtils.hoursToDateTime(100).plusMinutes(10), TimeUtils.hoursToDateTime(200).plusMinutes(10), actor);
        Assert.assertNotNull(result);
        int[] expected = new int[100];
        for (int i = 100; i < 200; ++i) {
            expected[200 - i - 1] = (200 - i) / 10 + 1;
        }
        expected[0] = 2;
        Assert.assertArrayEquals(expected, result);
    }

    private SearchResponse getMockSimpleResponse() {
        SearchResponse response = new SearchResponse();
        List<WallpostFull> posts = List.of(
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(10) + 6 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(10) + 10 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(10) + 22 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(10) + 41 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(11) + 2 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(11) + 11 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(11) + 37 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(11) + 51 * 60),
            (WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(12) + 5 * 60)
        );
        response.setItems(posts);
        response.setCount(posts.size());
        return response;
    }

    private SearchResponse getMockManyHoursResponse() {
        SearchResponse response = new SearchResponse();
        List<WallpostFull> posts = new ArrayList<>();
        for (int i = 100; i < 200; ++i) {
            for (int j = 0; j <= (200 - i) / 10; ++j) {
                posts.add((WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(i) + (2 * j + 11) * 60));
            }
        }
        posts.add((WallpostFull) new WallpostFull().setDate(TimeUtils.hoursToSeconds(200) + 2 * 60));
        response.setItems(posts);
        response.setCount(posts.size());
        return response;
    }
}
