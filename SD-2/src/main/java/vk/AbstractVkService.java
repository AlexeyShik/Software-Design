package vk;

import java.util.Optional;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.newsfeed.responses.SearchResponse;

public abstract class AbstractVkService implements VkService {

    abstract Optional<SearchResponse> doExecuteNewsfeedSearchQuery(UserActor userActor, String hashtag, int startTime, int endTime);

    @Override
    public Optional<SearchResponse> executeNewsfeedSearchQuery(UserActor userActor, String hashtag, int startTime, int endTime) {
        assert userActor != null && userActor.getId() != null && userActor.getAccessToken() != null;
        assert hashtag != null && !hashtag.isEmpty();
        assert startTime <= endTime;

        Optional<SearchResponse> response = doExecuteNewsfeedSearchQuery(userActor, hashtag, startTime, endTime);
        assert response != null;

        return  response;
    }
}
