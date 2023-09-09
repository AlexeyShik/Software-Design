package vk;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;
import com.vk.api.sdk.queries.newsfeed.NewsfeedSearchQuery;

public class VkServiceImpl extends AbstractVkService {

    private static final Log LOG = LogFactory.getLog(VkServiceImpl.class);

    private final VkApiClient vk;

    public VkServiceImpl(VkApiClient vk) {
        this.vk = vk;
    }

    public Optional<SearchResponse> doExecuteNewsfeedSearchQuery(UserActor userActor, String hashtag, int startTime, int endTime) {
        NewsfeedSearchQuery query = vk
            .newsfeed()
            .search(userActor)
            .startTime(startTime)
            .endTime(endTime)
            .q(hashtag);

        try {
            return Optional.of(query.execute());
        } catch (ApiException | ClientException e) {
            LOG.error("Error while executing query: startTime = " + startTime + " endTime = " + endTime + " hashtag = " + hashtag, e);
        }
        return Optional.empty();
    }
}
