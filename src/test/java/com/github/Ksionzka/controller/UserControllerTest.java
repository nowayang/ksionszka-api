package com.github.Ksionzka.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Ksionzka.controller.dto.ChangePasswordRequest;
import com.github.Ksionzka.controller.dto.UpdateUserRequest;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link UserController#changePasswordById(Long, ChangePasswordRequest)}
     */
    @Test
    void testChangePasswordById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setCreatedAt(null);
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setEnabled(true);
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setLastName("Doe");
        userEntity1.setLocked(true);
        userEntity1.setPassword("iloveyou");
        userEntity1.setRole(Role.LIBRARIAN);
        when(userRepository.save((UserEntity) any())).thenReturn(userEntity1);
        when(userRepository.getOrThrowById((Long) any())).thenReturn(userEntity);
        when(passwordEncoder.matches((CharSequence) any(), (String) any())).thenReturn(true);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("iloveyou");
        changePasswordRequest.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(changePasswordRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/{id}/change-password", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\"," +
                                        "\"role\":\"LIBRARIAN\",\"createdAt\":null,\"username\":\"jane.doe@example.org\",\"authorities\":[{\"authority\":\"ROLE_LIBRARIAN\"}]}"));
    }

    /**
     * Method under test: {@link UserController#deleteById(Long)}
     */
    @Test
    void testDeleteById() throws Exception {
        doNothing().when(userRepository).deleteById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{id}", 123L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserController#findAll(Pageable, String)}
     */
    @Test
    void testFindAll() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserController#getById(Long)}
     */
    @Test
    void testGetById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);
        when(userRepository.getOrThrowById((Long) any())).thenReturn(userEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{id}", 123L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\"," +
                                        "\"role\":\"LIBRARIAN\",\"createdAt\":null,\"username\":\"jane.doe@example.org\",\"authorities\":[{\"authority\":\"ROLE_LIBRARIAN\"}]}"));
    }

    /**
     * Method under test: {@link UserController#updateById(Long, UpdateUserRequest)}
     */
    @Test
    void testUpdateById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(null);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setLastName("Doe");
        userEntity.setLocked(true);
        userEntity.setPassword("iloveyou");
        userEntity.setRole(Role.LIBRARIAN);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setCreatedAt(null);
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setEnabled(true);
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setLastName("Doe");
        userEntity1.setLocked(true);
        userEntity1.setPassword("iloveyou");
        userEntity1.setRole(Role.LIBRARIAN);
        when(userRepository.save((UserEntity) any())).thenReturn(userEntity1);
        when(userRepository.getOrThrowById((Long) any())).thenReturn(userEntity);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("jane.doe@example.org");
        updateUserRequest.setFirstName("Jane");
        updateUserRequest.setLastName("Doe");
        String content = (new ObjectMapper()).writeValueAsString(updateUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/users/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\"," +
                                "\"role\":\"LIBRARIAN\",\"createdAt\":null,\"username\":\"jane.doe@example.org\",\"authorities\":[{\"authority\":\"ROLE_LIBRARIAN\"}]}"));
    }
}

