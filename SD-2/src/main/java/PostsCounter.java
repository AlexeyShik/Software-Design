import java.time.LocalDateTime;

import com.vk.api.sdk.client.actors.UserActor;

public interface PostsCounter {

    int MILLISECONDS_IN_HOUR = 60 * 60 * 1000;

    /**
     * Calculates number of posts with given <code>hashtag</code>, published in last <code>hoursAgo</code> hours
     *
     * @param hashtag hashtag to search in posts
     * @param hoursAgo maximum number of hours passed for its publishing
     * @param userActor user credentials
     * @return <p>array, where <code>ans[i]</code> is number of posts with given hashtag posted <code>(i; i-1]</code> hours ago</p>
     * <p>For example, for query in 10:10 answer [2, 3] means that 2 posts were published in (9:10; 10:10] and 3 posts in (8:10; 9:10]</p>
     */
    default int[] countPostsWithHashtag(String hashtag, int hoursAgo, UserActor userActor) {
        long now = System.currentTimeMillis();
        LocalDateTime startTimeSeconds = TimeUtils.secondsToDateTime((int) ((now - (long) hoursAgo * MILLISECONDS_IN_HOUR) / 1000));
        LocalDateTime endTimeSeconds = TimeUtils.secondsToDateTime((int) (now / 1000));
        return countPostsWithHashtag(hashtag, startTimeSeconds, endTimeSeconds, userActor);
    }

    /**
     * Calculates number of posts with given <code>hashtag</code>, published in last <code>hoursAgo</code> hours
     *
     * @param hashtag hashtag to search in posts
     * @param startTime the least acceptable post publishing time
     * @param endTime the greatest acceptable post publishing time
     * @param userActor user credentials
     * @return <p>array, where <code>ans[i]</code> is number of posts with given hashtag posted <code>(i; i-1]</code> hours ago</p>
     *      <p>For example, for query with <code>startTime</code> 7:50 and <code>endTime</code> 10:10
     *      answer [2, 3, 1] means that 2 posts were published in (9:10; 10:10] and 3 posts in (8:10; 9:10] and 1 post in (7:50; 8:10]</p>
     */
    int[] countPostsWithHashtag(String hashtag, LocalDateTime startTime, LocalDateTime endTime, UserActor userActor);
}
