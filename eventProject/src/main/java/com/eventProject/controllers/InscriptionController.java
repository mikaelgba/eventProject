package com.eventProject.controllers;

import com.eventProject.dto.InscriptionDTO;
import com.eventProject.services.InscriptionService;
import com.eventProject.vo.InscriptionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inscriptions")
@Tag(name = "Inscription", description = "the Inscription Api")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @Operation(summary = "Get page inscriptions, default 10 per page")
    @GetMapping
    public ResponseEntity<Page<InscriptionVO>> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inscriptionService.findAllInscription(pageable));
    }

    @Operation(summary = "Get inscription by id")
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionVO> findById(@PathVariable String id) {
        InscriptionVO inscriptionVO = inscriptionService.findById(id);
        return ResponseEntity.ok(inscriptionVO);
    }

    @Operation(summary = "Get page inscriptions by name event, default 10 per page")
    @GetMapping("/search-event/{nameEvent}")
    public ResponseEntity<Page<InscriptionVO>> findByNameEvent(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @PathVariable String nameEvent) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<InscriptionVO> inscriptionsVO = inscriptionService.findByNameEvent(nameEvent, pageable);
        return ResponseEntity.ok(inscriptionsVO);
    }

    @Operation(summary = "Get page inscriptions by cpf or username, default 10 per page")
    @GetMapping("/search-user/{nameOrCpf}")
    public ResponseEntity<Page<InscriptionVO>> findByNameOrCpf(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @PathVariable String nameOrCpf) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<InscriptionVO> inscriptionsVO = inscriptionService.findByCpfOrNameUser(nameOrCpf, pageable);
        return ResponseEntity.ok(inscriptionsVO);
    }

    @Operation(summary = "Create new inscription")
    @PostMapping
    public ResponseEntity<InscriptionVO> create(@RequestBody @Validated InscriptionDTO inscriptionDTO) {
        InscriptionVO saveInscription = inscriptionService.save(inscriptionDTO);
        return new ResponseEntity<>(saveInscription, HttpStatus.CREATED);
    }
}
