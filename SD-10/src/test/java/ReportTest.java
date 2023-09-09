import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ru.shik.sd.clock.SettableClock;
import ru.shik.sd.controller.ReportController;
import ru.shik.sd.controller.TicketController;
import ru.shik.sd.controller.TurnstileController;
import ru.shik.sd.dao.TicketDaoImpl;
import ru.shik.sd.dao.TurnstileDao;
import ru.shik.sd.dao.TurnstileDaoImpl;
import ru.shik.sd.model.Report;
import ru.shik.sd.service.ReportServiceImpl;
import ru.shik.sd.service.TicketService;
import ru.shik.sd.service.TicketServiceImpl;
import ru.shik.sd.service.TurnstileServiceImpl;

public class ReportTest {

    private SettableClock clock;
    private DataSource dataSource;
    private TicketController ticketController;
    private TurnstileController turnstileController;
    private ReportController reportController;

    @Before
    public void createDB() {
        clock = new SettableClock(LocalDateTime.parse("2023-01-01T00:00:00"));
        dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
        TicketService ticketService = new TicketServiceImpl(new TicketDaoImpl(dataSource));
        ticketController = new TicketController(clock, ticketService);
        TurnstileDao turnstileDao = new TurnstileDaoImpl(dataSource);
        turnstileController = new TurnstileController(clock, ticketService, new TurnstileServiceImpl(turnstileDao));
        reportController= new ReportController(new ReportServiceImpl(turnstileDao));
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
    public void testEmpty() {
        Report report = (Report) reportController.get().getBody();
        Assert.assertNotNull(report);
        Assert.assertTrue(report.getDailyVisits().isEmpty());
        Assert.assertEquals(0, report.getAverageVisits(), 1e-5);
        Assert.assertEquals(0, report.getAverageDuration(), 1e-5);
    }


    /*
    Visits:
        2023-01-01:
            3: 00-04
            4: 01-02
            5: 03-05
        2023-01-02:
            3: 00-03
            4: 01-02
        2023-01-03:
            3: 00-01
     Summary:
        visits = (3 + 2 + 1) / 3 = 2
        duration = (4 + 1 + 2 + 3 + 1 + 1) / 6 = 2 hours = 120 minutes
     */
    @Test
    public void testMany() {
        ticketController.create(3, 3);
        ticketController.create(4, 3);
        ticketController.create(5, 3);

        turnstileController.enter(3);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.enter(4);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.exit(4);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.enter(5);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.exit(3);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.exit(5);

        clock.setCurrentTime(LocalDateTime.parse("2023-01-02T00:00:00"));
        turnstileController.enter(3);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.enter(4);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.exit(4);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.exit(3);

        clock.setCurrentTime(LocalDateTime.parse("2023-01-03T00:00:00"));
        turnstileController.enter(3);
        clock.setCurrentTime(clock.getCurrentTime().plusHours(1));
        turnstileController.exit(3);

        Report report = (Report) reportController.get().getBody();
        Assert.assertNotNull(report);
        Assert.assertEquals(
            Map.of(
                LocalDate.parse("2023-01-01"), 3L,
                LocalDate.parse("2023-01-02"), 2L,
                LocalDate.parse("2023-01-03"), 1L
            ),
            report.getDailyVisits());
        Assert.assertEquals(2.0, report.getAverageVisits(), 1e-5);
        Assert.assertEquals(120.0, report.getAverageDuration(), 1e-5);
        System.out.println(report);
    }
}
