package com.eventProject.services.impl;

import com.eventProject.exceptions.exceptions.InvalidDataException;
import com.eventProject.vo.UserVO;
import com.eventProject.dto.UserDTO;
import com.eventProject.exceptions.exceptions.AlreadyExistsException;
import com.eventProject.exceptions.exceptions.NotFoundException;
import com.eventProject.models.User;
import com.eventProject.repositories.UserRepository;
import com.eventProject.services.UserService;
import com.eventProject.services.impl.converts.Converters;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserVO> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(UserVO::new);
    }

    @Override
    public UserVO findById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return new UserVO(user);
    }

    @Override
    public Page<UserVO> findByNameOrCpf(String nameOrCpf, Pageable pageable) {
        Page<User> users = userRepository.findByNameOrCpf(nameOrCpf, pageable);
        return users.map(UserVO::new);
    }

    @Override
    public UserVO save(UserDTO userDTO) {

        validateUserDTO(userDTO, null);

        Converters converters = new Converters();
        User user = converters.convertToUser(userDTO);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return new UserVO(savedUser);
    }

    private void validateUserDTO(UserDTO userDTO, String id) {

        if (Stream.of(userDTO.getName(), userDTO.getCpf(), userDTO.getLogin(), userDTO.getPassword(), userDTO.getRole())
                .anyMatch(Objects::isNull)) {
            throw new InvalidDataException("CPF, Login, Password, and Role cannot be null");
        }

        User existingUser = userRepository.findByUserLogin(userDTO.getLogin());
        if (existingUser != null && !Objects.equals(userDTO.getLogin(), existingUser.getLogin())) {
            throw new AlreadyExistsException("Login already exists");
        }

        User user = userRepository.findByUserCpf(userDTO.getCpf());

        if (user != null) {
            if (id == null) {
                throw new AlreadyExistsException("CPF already exists with user");
            } else {
                if (!id.equals(user.getId())) throw new AlreadyExistsException("CPF already exists with user");
            }
        }

    }

    @Override
    public UserVO update(String id, UserDTO userDTO) {

        validateUserDTO(userDTO, id);

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        BeanUtils.copyProperties(userDTO, user, "id", "login", "created");
        user.setUpdated(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return new UserVO(updatedUser);
    }

    @Override
    public void delete(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.deleteById(user.getId());
    }
}
