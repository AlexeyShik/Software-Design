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
import ru.shik.sd.model.TurnstileEvent;
import ru.shik.sd.service.TicketService;
import ru.shik.sd.service.TurnstileService;

@RestController
@RequestMapping("/turnstile")
public class TurnstileController {

    private final Clock clock;
    private final TicketService ticketService;
    private final TurnstileService turnstileService;

    public TurnstileController(Clock clock, TicketService ticketService, TurnstileService turnstileService) {
        this.clock = clock;
        this.ticketService = ticketService;
        this.turnstileService = turnstileService;
    }

    @RequestMapping("/enter")
    public ResponseEntity<Object> enter(@RequestParam int ticketId) {
        Optional<TicketState> state = ticketService.getTicketState(ticketId);
        if (state.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LocalDateTime now = clock.now();
        if (now.isAfter(state.get().getExpirationTime())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<TurnstileEvent> recentEvent = turnstileService.getRecentEvent(ticketId);
        if (recentEvent.isPresent() && recentEvent.get().getAction() == TurnstileEvent.Action.ENTER) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        turnstileService.addEvent(ticketId, TurnstileEvent.Action.ENTER, now);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping("/exit")
    public ResponseEntity<Object> exit(@RequestParam int ticketId) {
        Optional<TicketState> state = ticketService.getTicketState(ticketId);
        if (state.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<TurnstileEvent> recentEvent = turnstileService.getRecentEvent(ticketId);
        if (recentEvent.isEmpty() || recentEvent.get().getAction() != TurnstileEvent.Action.ENTER) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        turnstileService.addEvent(ticketId, TurnstileEvent.Action.EXIT, clock.now());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
