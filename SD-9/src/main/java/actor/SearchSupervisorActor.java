package actor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.util.Timeout;
import engine.Engine;
import query.EngineSearchRequest;
import query.EngineSearchResponse;
import query.SupervisorSearchRequest;
import query.SupervisorSearchResponse;

public class SearchSupervisorActor extends UntypedActor {

    private Set<ActorRef> childActors;
    private Map<Engine, List<String>> searchResult;
    private CompletableFuture<SupervisorSearchResponse> future;

    @Override
    public void onReceive(Object message) {
        if (message instanceof SupervisorSearchRequest request) {
            childActors = new HashSet<>();
            searchResult = new HashMap<>();
            future = new CompletableFuture<>();
            Timeout timeout = Timeout.apply(request.getConfig().getTtl(), request.getConfig().getUnit());
            getContext().setReceiveTimeout(timeout.duration());
            for (Engine engine : request.getConfig().getEngines()) {
                ActorRef searchActor = getContext().actorOf(Props.create(SearchEngineActor.class), engine.getName() + "Actor");
                childActors.add(searchActor);
                searchActor.tell(new EngineSearchRequest(request.getQuery(), engine), self());
            }
            sender().tell(future, self());
        } else if (message instanceof EngineSearchResponse engineResponse) {
            ActorRef sender = sender();
            childActors.remove(sender);
            searchResult.put(engineResponse.getEngine(), engineResponse.getUrls());
            getContext().stop(sender);
            if (childActors.isEmpty()) {
                terminateSelf();
            }
        } else if (message instanceof ReceiveTimeout) {
            terminateSelf();
        } else if (message instanceof Terminated) {
            terminateSelf();
        }
    }

    private void terminateSelf() {
        for (ActorRef childActor : childActors) {
            getContext().stop(childActor);
        }
        future.complete(new SupervisorSearchResponse(searchResult));
        getContext().stop(self());
    }
}
