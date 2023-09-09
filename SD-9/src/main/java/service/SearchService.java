package service;

import java.util.concurrent.CompletableFuture;

import actor.SearchSupervisorActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import config.SearchConfig;
import query.SearchRequest;
import query.SearchResponse;
import query.SupervisorSearchRequest;
import query.SupervisorSearchResponse;
import scala.concurrent.Await;

public class SearchService {

    private static final String ACTOR_SYSTEM_NAME = "searchSystem";

    private final SearchConfig searchConfig;

    public SearchService(SearchConfig searchConfig) {
        this.searchConfig = searchConfig;
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<SearchResponse> search(SearchRequest request) {
        ActorSystem actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef supervisor = actorSystem.actorOf(Props.create(SearchSupervisorActor.class), "supervisorActor");
        Timeout timeout = Timeout.apply(searchConfig.getTtl(), searchConfig.getUnit());
        CompletableFuture<SupervisorSearchResponse> future;
        try {
            future = (CompletableFuture<SupervisorSearchResponse>) Await.result(Patterns.ask(
                supervisor,
                new SupervisorSearchRequest(request.getQuery(), searchConfig),
                timeout
            ), timeout.duration());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new SearchResponse());
        }
        return future.thenApply(response -> {
            actorSystem.terminate();
            return new SearchResponse(response.getResponseMap());
        });
    }

}
