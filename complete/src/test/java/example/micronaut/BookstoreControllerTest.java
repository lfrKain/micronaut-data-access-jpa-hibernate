package example.micronaut;

import example.micronaut.anotherdomain.Bookstore;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookstoreControllerTest {

    private static EmbeddedServer server; // <1>
    private static HttpClient client; // <2>

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext
                .build()
                .run(EmbeddedServer.class); // <1>
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL()); // <2>
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testBookstoreListOperation() {
        HttpRequest request = HttpRequest.GET("/bookstores/list");
        List<Bookstore> bookstores = client.toBlocking().retrieve(request, Argument.of(List.class, Bookstore.class));

        assertEquals(0, bookstores.size());
    }

    @Test
    public void testBookstoreListOperationThatThrowsException() {
        HttpRequest request = HttpRequest.GET("/bookstores/list");
        List<Bookstore> bookstores = client.toBlocking().retrieve(request, Argument.of(List.class, Bookstore.class));

        assertEquals(0, bookstores.size());
    }
}
