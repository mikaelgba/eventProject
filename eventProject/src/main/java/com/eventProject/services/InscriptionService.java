package com.eventProject.services;

import com.eventProject.dto.InscriptionDTO;
import com.eventProject.vo.InscriptionVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InscriptionService {

    Page<InscriptionVO> findAllInscription(Pageable pageable);

    InscriptionVO findById(String id);

    Page<InscriptionVO> findByNameEvent(String nameEvent, Pageable pageable);

    Page<InscriptionVO> findByCpfOrNameUser(String cpfOrNameUser, Pageable pageable);

    InscriptionVO save(InscriptionDTO userDTO);

}
