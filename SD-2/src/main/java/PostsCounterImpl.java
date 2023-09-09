import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;
import com.vk.api.sdk.objects.wall.Wallpost;

import vk.VkService;

public class PostsCounterImpl extends AbstractPostsCounter {

    private final VkService vk;

    public PostsCounterImpl(VkService vk) {
        this.vk = vk;
    }

    @Override
    public int[] doCountPostsWithHashtag(String hashtag, LocalDateTime startTime, LocalDateTime endTime, UserActor userActor) {
        Duration duration = Duration.between(startTime, endTime);
        int nHours = (int) duration.toHours();
        if (duration.toMinutes() != 60 * duration.toHours()) {
            // (00:01; 02:02] contains 3 hours: (00:01; 01:00], (01:00; 02:00], (02:00; 02:02]
            ++nHours;
        }

        int[] postsPerHour = new int[nHours];

        Optional<SearchResponse> optionalResponse = vk.executeNewsfeedSearchQuery(userActor, hashtag,
            (int) startTime.atZone(ZoneId.systemDefault()).toEpochSecond(),
            (int) endTime.atZone(ZoneId.systemDefault()).toEpochSecond());

        if (optionalResponse.isEmpty()) {
            return postsPerHour;
        }

        SearchResponse response = optionalResponse.get();
        for (Wallpost post : response.getItems()) {
            int index = (int) Duration.between(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(post.getDate()), ZoneId.systemDefault()),
                endTime
            ).toHours();

            ++postsPerHour[index];
        }

        return postsPerHour;
    }

}
