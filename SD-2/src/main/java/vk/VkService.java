package vk;

import java.util.Optional;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;

public interface VkService {

    /**
     * Executes vk api <code>newsfeed.search</code> query
     *
     * @param userActor user credentials
     * @param hashtag hashtag to search in posts
     * @param startTime the least acceptable post publishing time
     * @param endTime the greatest acceptable post publishing time
     * @return search response or <code>Optional.empty()</code> if some exception happens while execution
     */
    Optional<SearchResponse> executeNewsfeedSearchQuery(UserActor userActor, String hashtag, int startTime, int endTime);
}
