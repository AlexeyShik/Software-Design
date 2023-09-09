package actor;

import akka.actor.UntypedActor;
import engine.Engine;
import query.EngineSearchRequest;
import query.EngineSearchResponse;

public class SearchEngineActor extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof EngineSearchRequest searchRequest) {
            Engine engine = searchRequest.getEngine();
            EngineSearchResponse response = new EngineSearchResponse(engine, engine.search(searchRequest.getQuery()));
            sender().tell(response, self());
        }
    }
}
