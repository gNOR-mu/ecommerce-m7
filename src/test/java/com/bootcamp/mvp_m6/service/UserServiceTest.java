package com.bootcamp.mvp_m6.service;

import com.bootcamp.mvp_m6.dto.user.UserPrivateRegisterDTO;
import com.bootcamp.mvp_m6.dto.user.UserPublicRegisterDTO;
import com.bootcamp.mvp_m6.enums.Role;
import com.bootcamp.mvp_m6.exceptions.ResourceAlreadyExistsException;
import com.bootcamp.mvp_m6.mapper.UserMapper;
import com.bootcamp.mvp_m6.model.User;
import com.bootcamp.mvp_m6.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    /**
     * captura el usuario antes de enviarlo a la bd
     */
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Debe crear un usuario público cuando el email no existe")
    public void createPublicUser_shouldCreateUser_whenEmailNotExists() {
        //arrange
        String passHashed = "a&k!s$lñd#$%12ep";
        String plainPassword = "prueba1234";

        UserPublicRegisterDTO dto = UserPublicRegisterDTO.builder()
                .email("user@email.cl")
                .password(plainPassword)
                .lastName("Pérez")
                .name("usuario")
                .build();

        User mappedUser = User.builder()
                .email("user@email.cl")
                .passHash(plainPassword)
                .lastName("Pérez")
                .name("usuario")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(passHashed);

        //act
        userService.createPublicUser(dto);

        //assert
        verify(userMapper).toEntity(dto);
        verify(userRepository).existsByEmail(mappedUser.getEmail());

        //capturo el usuario enviado al repositorio
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(passHashed, savedUser.getPassHash());
        assertEquals(dto.getEmail(), savedUser.getEmail());
        assertEquals(dto.getName(), savedUser.getName());
        assertEquals(dto.getLastName(), savedUser.getLastName());

        //los usuarios públicos siempre son clientes
        assertEquals(Role.CLIENT, savedUser.getRole());
    }

    @Test
    @DisplayName("Debe crear un usuario privado cuando el email no existe")
    public void createPrivateUser_shouldCreateUser_whenEmailNotExists() throws Exception {
        //arrange
        String passHashed = "a&k!s$lñd#$%12ep";
        String plainPassword = "prueba1234";

        UserPrivateRegisterDTO dto = UserPrivateRegisterDTO.builder()
                .email("user@email.cl")
                .password(plainPassword)
                .lastName("Pérez")
                .name("usuario")
                .role(Role.ADMIN)
                .build();

        User mappedUser = User.builder()
                .email("user@email.cl")
                .passHash(plainPassword)
                .lastName("Pérez")
                .name("usuario")
                .role(Role.ADMIN)
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(passHashed);

        //act
        userService.createPrivateUser(dto);

        //assert
        verify(userMapper).toEntity(dto);
        verify(userRepository).existsByEmail(mappedUser.getEmail());

        //capturo el usuario enviado al repositorio
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(passHashed, savedUser.getPassHash());
        assertEquals(dto.getEmail(), savedUser.getEmail());
        assertEquals(dto.getName(), savedUser.getName());
        assertEquals(dto.getLastName(), savedUser.getLastName());

        //los usuarios privados conservan el rol enviado
        assertEquals(Role.ADMIN, savedUser.getRole());
    }

    @Test
    @DisplayName("No debe crear un usuario público cuando el email ya existe")
    public void createPublicUser_shouldThrowError_whenEmailExists() throws Exception {
        //arrange
        String passHashed = "a&k!s$lñd#$%12ep";
        String plainPassword = "prueba1234";

        UserPublicRegisterDTO dto = UserPublicRegisterDTO.builder()
                .email("user@email.cl")
                .password(plainPassword)
                .lastName("Pérez")
                .name("usuario")
                .build();

        User mappedUser = User.builder()
                .email("user@email.cl")
                .passHash(plainPassword)
                .lastName("Pérez")
                .name("usuario")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        when(userMapper.toEntity(dto)).thenReturn(mappedUser);

        // act & assert
        Exception ex = assertThrows(ResourceAlreadyExistsException.class, () -> userService.createPublicUser(dto));

        verify(userMapper).toEntity(dto);
        verify(userRepository).existsByEmail(mappedUser.getEmail());

        verify(passwordEncoder, never()).encode(mappedUser.getPassHash());
        verify(userRepository, never()).save(mappedUser);
    }
}