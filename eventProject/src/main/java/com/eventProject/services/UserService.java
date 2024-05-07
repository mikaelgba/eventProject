package com.eventProject.services;

import com.eventProject.vo.UserVO;
import com.eventProject.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserVO> findAll(Pageable pageable);

    UserVO findById(String id);

    Page<UserVO> findByNameOrCpf(String nameOrCpf, Pageable pageable);

    UserVO save(UserDTO userDTO);

    UserVO update(String id, UserDTO userDTO);

    void delete(String id);
}
