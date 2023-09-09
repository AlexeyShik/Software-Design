import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ru.shik.sd.clock.SettableClock;
import ru.shik.sd.controller.TicketController;
import ru.shik.sd.controller.TurnstileController;
import ru.shik.sd.dao.TicketDaoImpl;
import ru.shik.sd.dao.TurnstileDaoImpl;
import ru.shik.sd.service.TicketService;
import ru.shik.sd.service.TicketServiceImpl;
import ru.shik.sd.service.TurnstileServiceImpl;

public class TurnstileTest {
    
    private static final int TEST_TICKET_ID = 3;
    private static final long TEST_DAYS = 5;

    private SettableClock clock;
    private DataSource dataSource;
    private TicketController ticketController;
    private TurnstileController turnstileController;

    @Before
    public void createDB() {
        clock = new SettableClock(LocalDateTime.now());
        dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
        TicketService ticketService = new TicketServiceImpl(new TicketDaoImpl(dataSource));
        ticketController = new TicketController(clock, ticketService);
        turnstileController = new TurnstileController(clock, ticketService, new TurnstileServiceImpl(new TurnstileDaoImpl(dataSource)));
        try (Connection conn = dataSource.getConnection()) {
            String query = Files.readString(Path.of("src/main/resources/create_db.sql"));
            conn.createStatement().execute(query);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void dropDB() {
        try (Connection conn = dataSource.getConnection()) {
            String query = Files.readString(Path.of("src/main/resources/drop_db.sql"));
            conn.createStatement().execute(query);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEnter() {
        createTicket(TEST_DAYS);
        checkEnter(HttpStatus.OK);
    }

    @Test
    public void testExit() {
        createTicket(TEST_DAYS);
        checkEnter(HttpStatus.OK);
        checkExit(HttpStatus.OK);
    }

    @Test
    public void testEnterTwice() {
        createTicket(TEST_DAYS);
        checkEnter(HttpStatus.OK);
        checkEnter(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testExitTwice() {
        createTicket(TEST_DAYS);
        checkEnter(HttpStatus.OK);
        checkExit(HttpStatus.OK);
        checkExit(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testUnknownTicket() {
        checkEnter(HttpStatus.NOT_FOUND);
        checkExit(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testExpiredTicket() {
        createTicket(1);
        checkEnter(HttpStatus.OK);
        checkExit(HttpStatus.OK);

        clock.setCurrentTime(clock.getCurrentTime().plusDays(2));
        checkEnter(HttpStatus.FORBIDDEN);
        checkExit(HttpStatus.FORBIDDEN);

        createTicket(4, 3);
        clock.setCurrentTime(clock.getCurrentTime().plusMinutes(1));
        checkEnter(4, HttpStatus.OK);
        checkExit(4, HttpStatus.OK);
    }

    private void createTicket(int id, long days) {
        HttpStatus code = ticketController.create(id, days).getStatusCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(HttpStatus.CREATED, code);
        clock.setCurrentTime(clock.getCurrentTime().plusMinutes(1));
    }

    private void createTicket(long days) {
        createTicket(TEST_TICKET_ID, days);
    }

    private void checkEnter(int id, HttpStatus expected) {
        HttpStatus code = turnstileController.enter(id).getStatusCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(expected, code);
        clock.setCurrentTime(clock.getCurrentTime().plusMinutes(1));
    }

    private void checkEnter(HttpStatus expected) {
        checkEnter(TEST_TICKET_ID, expected);
    }

    private void checkExit(int id, HttpStatus expected) {
        HttpStatus code = turnstileController.exit(id).getStatusCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(expected, code);
        clock.setCurrentTime(clock.getCurrentTime().plusMinutes(1));
    }

    private void checkExit(HttpStatus expected) {
        checkExit(TEST_TICKET_ID, expected);
    }
}
