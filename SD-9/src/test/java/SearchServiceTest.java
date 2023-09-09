import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.glassfish.grizzly.http.Method;
import org.junit.Assert;
import org.junit.Test;

import com.xebialabs.restito.builder.stub.StubHttp;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;

import actor.SearchEngineActor;
import actor.SearchSupervisorActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestKit;
import config.MockSearchConfig;
import config.RemoteSearchConfig;
import config.SearchConfig;
import engine.Engine;
import engine.RemoteEngine;
import query.EngineSearchRequest;
import query.EngineSearchResponse;
import query.SearchRequest;
import query.SearchResponse;
import query.SupervisorSearchRequest;
import engine.BrokenMockEngine;
import engine.MockEngine;
import service.SearchService;

public class SearchServiceTest {

    private static final int PORT = 8080;

    private static final MockEngine YANDEX = new MockEngine("Yandex", "dzen.ru", Map.of(
        "cat", List.of("cat.com", "cats2.ru"),
        "football", List.of("rfpl.ru", "worldcup.com", "fifa.com")
    ));
    private static final MockEngine GOOGLE = new MockEngine("Google", "google.com", Map.of(
        "cat", List.of("cat_stories.com", "cats_eng.en"),
        "football", List.of("apl.en", "worldcup.com", "fifa.com")
    ));
    private static final MockEngine BING = new MockEngine("Bing", "bing.com", Map.of(
        "cat", List.of("cat_microsoft_cat.com", "cats_windows.ru"),
        "football", List.of("worldcup.com", "soccer.com")
    ));
    private static final MockEngine BROKEN_BING = new BrokenMockEngine("Bing", "bing.com", Map.of(
        "cat", List.of("cat_microsoft_cat.com", "cats_windows.ru"),
        "football", List.of("worldcup.com", "soccer.com")
    ));
    private static final SearchConfig FINE_CONFIG = new MockSearchConfig(
        3, TimeUnit.SECONDS, (short) 15, List.of(YANDEX, GOOGLE, BING)
    );
    private static final SearchConfig BROKEN_CONFIG = new MockSearchConfig(
        3, TimeUnit.SECONDS, (short) 15, List.of(YANDEX, GOOGLE, BROKEN_BING)
    );

    private final ActorSystem system = ActorSystem.create("testSystem");
    private SearchService searchService;

    @Test
    public void testServiceFineEngines() {
        searchService = new SearchService(FINE_CONFIG);
        SearchResponse response;
        try {
            response = searchService.search(new SearchRequest("football")).get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.fail();
            return;
        }
        Assert.assertEquals(
            Map.of(
                "Yandex", List.of("rfpl.ru", "worldcup.com", "fifa.com"),
                "Google", List.of("apl.en", "worldcup.com", "fifa.com"),
                "Bing", List.of("worldcup.com", "soccer.com")
            ),
            response.getResponseMap().entrySet().stream().collect(Collectors.toMap(x -> x.getKey().getName(), Map.Entry::getValue))
        );
    }

    @Test
    public void testServiceBrokenEngines() {
        searchService = new SearchService(BROKEN_CONFIG);
        SearchResponse response;
        try {
            response = searchService.search(new SearchRequest("football")).get(4, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.fail();
            return;
        }
        Assert.assertEquals(
            Map.of(
                "Yandex", List.of("rfpl.ru", "worldcup.com", "fifa.com"),
                "Google", List.of("apl.en", "worldcup.com", "fifa.com")
            ),
            response.getResponseMap().entrySet().stream().collect(Collectors.toMap(x -> x.getKey().getName(), Map.Entry::getValue))
        );
    }

    @Test
    public void testSearchActor() {
        new TestKit(system) {
            {
                final TestKit probe = new TestKit(system);
                final Props props = Props.create(SearchEngineActor.class);
                final ActorRef actor = system.actorOf(props, "searchActor");
                actor.tell(new EngineSearchRequest("cat", YANDEX), probe.testActor());
                probe.expectMsg(new EngineSearchResponse(YANDEX, List.of("cat.com", "cats2.ru")));
            }
        };
    }

    @Test
    public void testSupervisorSearchActor() {
        new TestKit(system) {
            {
                final TestKit probe = new TestKit(system);
                final Props props = Props.create(SearchSupervisorActor.class);
                final ActorRef actor = system.actorOf(props, "supervisorActor");
                actor.tell(new SupervisorSearchRequest("football", FINE_CONFIG), probe.testActor());
                probe.expectMsgType(scala.reflect.ClassTag$.MODULE$.apply(CompletableFuture.class));
            }
        };
    }

    @Test
    public void testSearchServiceWithStub() {
        Engine stubYandex = new RemoteEngine(YANDEX.getName(), "localhost", PORT);
        Engine stubGoogle = new RemoteEngine(GOOGLE.getName(), "localhost", PORT + 1);
        SearchConfig searchConfig = new RemoteSearchConfig(3, TimeUnit.SECONDS, (short) 15, List.of(stubYandex, stubGoogle));

        withStubServer(s1 -> {
            StubHttp.whenHttp(s1)
                .match(Condition.method(Method.GET), Condition.uri("/search"))
                .then(Action.stringContent("{ \"response\": [ \"abc.ru\", \"cde.ru\" ] }"));

            withStubServer(s2 -> {
                StubHttp.whenHttp(s2)
                    .match(Condition.method(Method.GET), Condition.uri("/search"))
                    .then(Action.stringContent("{ \"response\": [ \"res1.com\", \"res2.com\" ] }"));

                try {
                    Map<Engine, List<String>> responseMap = new SearchService(searchConfig)
                        .search(new SearchRequest("abc")).get().getResponseMap();
                    Assert.assertEquals(List.of("abc.ru", "cde.ru"), responseMap.get(stubYandex));
                    Assert.assertEquals(List.of("res1.com", "res2.com"), responseMap.get(stubGoogle));
                } catch (InterruptedException | ExecutionException e) {
                    Assert.fail();
                }
            }, PORT + 1);
        }, PORT);
    }

    @Test
    public void testSearchServiceWithStubTimeout() {
        Engine stubYandex = new RemoteEngine(YANDEX.getName(), "localhost", PORT);
        Engine stubGoogle = new RemoteEngine(GOOGLE.getName(), "localhost", PORT + 1);
        SearchConfig searchConfig = new RemoteSearchConfig(3, TimeUnit.SECONDS, (short) 15, List.of(stubYandex, stubGoogle));

        withStubServer(s1 -> {
            StubHttp.whenHttp(s1)
                .match(Condition.method(Method.GET), Condition.uri("/search"))
                .then(Action.stringContent("{ \"response\": [ \"abc.ru\", \"cde.ru\" ] }"));

            withStubServer(s2 -> {
                StubHttp.whenHttp(s2)
                    .match(Condition.method(Method.GET), Condition.uri("/search"))
                    .then(Action.delay(100_000));

                try {
                    Map<Engine, List<String>> responseMap = new SearchService(searchConfig)
                        .search(new SearchRequest("abc")).get().getResponseMap();
                    Assert.assertEquals(List.of("abc.ru", "cde.ru"), responseMap.get(stubYandex));
                    Assert.assertEquals(Collections.emptyList(), responseMap.get(stubGoogle));
                } catch (InterruptedException | ExecutionException e) {
                    Assert.fail();
                }
            }, PORT + 1);
        }, PORT);
    }

    private void withStubServer(Consumer<StubServer> callback, int port) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(port).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
