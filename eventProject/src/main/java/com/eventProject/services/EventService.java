package com.eventProject.services;

import com.eventProject.vo.EventVO;
import com.eventProject.dto.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    Page<EventVO> findAll(Pageable pageable);

    EventVO findById(String id);

    Page<EventVO> findByName(String name, Pageable pageable);

    EventVO save(EventDTO userDTO);

    EventVO update(String id, EventDTO userDTO);

    void delete(String id);
}
