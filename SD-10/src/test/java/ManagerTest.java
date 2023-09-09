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

import ru.shik.sd.clock.ClassicClock;
import ru.shik.sd.controller.TicketController;
import ru.shik.sd.dao.TicketDaoImpl;
import ru.shik.sd.model.TicketState;
import ru.shik.sd.service.TicketServiceImpl;

public class ManagerTest {

    private static final int TEST_TICKET_ID = 3;
    private static final long TEST_DAYS_EXTEND = 25;
    private static final long TEST_DAYS = 30;

    private DataSource dataSource;
    private TicketController ticketController;

    @Before
    public void createDB() {
        dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
        ticketController = new TicketController(new ClassicClock(), new TicketServiceImpl(new TicketDaoImpl(dataSource)));
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
    public void testCreate() {
        checkCreate(HttpStatus.CREATED, 30);
        checkInfo();
    }

    @Test
    public void testExtend() {
        checkCreate(HttpStatus.CREATED, 5);
        checkExtend(HttpStatus.OK);
        checkInfo();
    }

    @Test
    public void testEmptyInfo() {
        HttpStatus code = ticketController.info(3).getStatusCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(HttpStatus.NOT_FOUND, code);
    }

    @Test
    public void testCreateTwice() {
        checkCreate(HttpStatus.CREATED, 5);
        checkCreate(HttpStatus.BAD_REQUEST, 10);
    }

    @Test
    public void testExtendUnknown() {
        checkExtend(HttpStatus.NOT_FOUND);
    }

    private void checkCreate(HttpStatus expected, long days) {
        HttpStatus code = ticketController.create(TEST_TICKET_ID, days).getStatusCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(expected, code);
    }

    private void checkExtend(HttpStatus expected) {
        HttpStatus code = ticketController.extend(TEST_TICKET_ID, TEST_DAYS_EXTEND).getStatusCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(expected, code);
    }

    private void checkInfo() {
        TicketState ticket = (TicketState) ticketController.info(TEST_TICKET_ID).getBody();
        Assert.assertNotNull(ticket);
        Assert.assertEquals(TEST_TICKET_ID, ticket.getTicketId());
        Assert.assertTrue(ticket.getExpirationTime().isAfter(LocalDateTime.now())
            && ticket.getExpirationTime().isBefore(LocalDateTime.now().plusDays(TEST_DAYS)));
    }
}
