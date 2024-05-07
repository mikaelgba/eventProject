package com.eventProject.services.impl;

import com.eventProject.dto.InscriptionDTO;
import com.eventProject.exceptions.exceptions.FullEventException;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.Event;
import com.eventProject.models.Inscription;
import com.eventProject.models.User;
import com.eventProject.repositories.EventRepository;
import com.eventProject.repositories.InscriptionRepository;
import com.eventProject.repositories.UserRepository;
import com.eventProject.services.InscriptionService;
import com.eventProject.vo.InscriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InscriptionServiceImpl implements InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Page<InscriptionVO> findAllInscription(Pageable pageable) {
        Page<Inscription> inscriptionsPage = inscriptionRepository.findAll(pageable);
        return inscriptionsPage.map(InscriptionVO::new);
    }

    @Override
    public InscriptionVO findById(String id) {
        Inscription inscriptions = inscriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        return new InscriptionVO(inscriptions);
    }

    @Override
    public Page<InscriptionVO> findByNameEvent(String name, Pageable pageable) {
        Page<Inscription> events = inscriptionRepository.findByNameEvent(name, pageable);
        return events.map(InscriptionVO::new);
    }

    @Override
    public Page<InscriptionVO> findByCpfOrNameUser(String cpfOrNameUser, Pageable pageable) {
        Page<Inscription> events = inscriptionRepository.findByCpfOrNameUser(cpfOrNameUser, pageable);
        return events.map(InscriptionVO::new);
    }

    @Override
    public InscriptionVO save(InscriptionDTO inscriptionDTO) {

        Event event = eventRepository.findByIdEvent(inscriptionDTO.getEventId());
        if (event == null) throw new NotFoundException("Event not found");
        if (event.getCapMax() <= event.getCapActual()) throw new FullEventException("Event is full");

        User user = userRepository.findByIdUser(inscriptionDTO.getUserId());
        if (user == null) throw new NotFoundException("User not found");

        event.setCapActual(event.getCapActual() + 1);
        event.setUpdated(LocalDateTime.now());
        eventRepository.save(event);

        Inscription inscription = new Inscription();
        inscription.setEvent(event);
        inscription.setUser(user);
        inscription.setDateInscription(LocalDateTime.now());
        Inscription inscriptionSaved = inscriptionRepository.save(inscription);
        return new InscriptionVO(inscriptionSaved);
    }
}
