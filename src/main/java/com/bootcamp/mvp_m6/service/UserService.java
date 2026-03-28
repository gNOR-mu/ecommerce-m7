package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.dto.user.UserPrivateRegisterDTO;
import com.bootcamp.mvp_m6.dto.user.UserPublicRegisterDTO;
import com.bootcamp.mvp_m6.enums.Role;
import com.bootcamp.mvp_m6.exceptions.ResourceAlreadyExistsException;
import com.bootcamp.mvp_m6.mapper.UserMapper;
import com.bootcamp.mvp_m6.model.User;
import com.bootcamp.mvp_m6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para los usuarios
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    /**
     * Crea un nuevo usuario verificando su existencia en la base de datos
     *
     * @param user Usuario a crear
     * @apiNote La contraseña viene en texto plano, por lo que se encripta antes de guardar
     * @throws ResourceAlreadyExistsException cuando un email ya existe
     */
    @Transactional
    private void createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", user.getEmail());
        }

        user.setPassHash(passwordEncoder.encode(user.getPassHash()));

        userRepository.save(user);
    }

    /**
     * Crea un nuevo usuario público con ROL: CLIENT
     *
     * @param dto DTO del nuevo usuario a crear
     */
    @Transactional
    public void createPublicUser(UserPublicRegisterDTO dto) {
        User user = userMapper.toEntity(dto);

        /*Establece el rol USER*/
        user.setRole(Role.CLIENT);

        createUser(user);
    }

    /**
     * Crea un nuevo usuario privado
     *
     * @param dto DTO del nuevo usuario a crear
     */
    @Transactional
    public void createPrivateUser(UserPrivateRegisterDTO dto) {
        User user = userMapper.toEntity(dto);

        createUser(user);
    }
}
