import java.time.LocalDateTime;

import com.vk.api.sdk.client.actors.UserActor;

public abstract class AbstractPostsCounter implements PostsCounter {

    protected abstract int[] doCountPostsWithHashtag(String hashtag, LocalDateTime startTime, LocalDateTime endTime, UserActor userActor);

    @Override
    public int[] countPostsWithHashtag(String hashtag, LocalDateTime startTime, LocalDateTime endTime, UserActor userActor) {
        assert userActor != null && userActor.getId() != null && userActor.getAccessToken() != null;
        assert hashtag != null && !hashtag.isEmpty();
        assert startTime != null && endTime != null && !startTime.isAfter(endTime);
        return doCountPostsWithHashtag(hashtag, startTime, endTime, userActor);
    }
}
