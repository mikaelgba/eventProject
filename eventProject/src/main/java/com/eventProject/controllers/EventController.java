package com.eventProject.controllers;

import com.eventProject.vo.EventVO;
import com.eventProject.dto.EventDTO;
import com.eventProject.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@Tag(name = "Event", description = "the Event Api")
public class EventController {

    @Autowired
    private EventService eventService;

    @Operation(summary = "Get page events, default 10 per page")
    @GetMapping
    public ResponseEntity<Page<EventVO>> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.findAll(pageable));
    }

    @Operation(summary = "Get event by Id")
    @GetMapping("/{id}")
    public ResponseEntity<EventVO> findById(@PathVariable String id) {
        EventVO eventVO = eventService.findById(id);
        return ResponseEntity.ok(eventVO);
    }

    @Operation(summary = "Get event by name")
    @GetMapping("/search/{name}")
    public ResponseEntity<Page<EventVO>> findByNameEvent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String name) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<EventVO> eventsVO = eventService.findByName(name, pageable);
        return ResponseEntity.ok(eventsVO);
    }

    @Operation(summary = "Create new event")
    @PostMapping
    public ResponseEntity<EventVO> create(@RequestBody @Validated EventDTO userDTO) {
        EventVO savedEventVO = eventService.save(userDTO);
        return new ResponseEntity<>(savedEventVO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update event by id with eventDTO")
    @PutMapping("/{id}")
    public ResponseEntity<EventVO> update(@PathVariable String id, @RequestBody @Validated EventDTO eventDTO) {
        EventVO updatedUserVO = eventService.update(id, eventDTO);
        return ResponseEntity.ok(updatedUserVO);
    }

    @Operation(summary = "Delete event by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
