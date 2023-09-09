package ru.shik.sd.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.shik.sd.clock.Clock;
import ru.shik.sd.model.TicketState;
import ru.shik.sd.service.TicketService;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final Clock clock;
    private final TicketService ticketService;

    public TicketController(Clock clock, TicketService ticketService) {
        this.clock = clock;
        this.ticketService = ticketService;
    }

    @RequestMapping("/info")
    public ResponseEntity<Object> info(@RequestParam int ticketId) {
        Optional<TicketState> state = ticketService.getTicketState(ticketId);
        if (state.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(state.get(), HttpStatus.OK);
    }

    @RequestMapping("/create")
    public ResponseEntity<Object> create(@RequestParam int ticketId, @RequestParam long days) {
        Optional<TicketState> state = ticketService.getTicketState(ticketId);
        if (state.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime now = clock.now();
        LocalDateTime expirationTime = now.plusDays(days);
        ticketService.addTicket(ticketId, now, expirationTime);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/extend")
    public ResponseEntity<Object> extend(@RequestParam int ticketId, @RequestParam long days) {
        Optional<TicketState> state = ticketService.getTicketState(ticketId);
        if (state.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LocalDateTime now = clock.now();
        LocalDateTime expirationTime = now.plusDays(days);
        ticketService.extendTicket(ticketId, now, expirationTime);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
