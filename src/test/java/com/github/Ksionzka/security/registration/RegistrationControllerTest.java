package com.github.Ksionzka.security.registration;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {RegistrationController.class})
@ExtendWith(SpringExtension.class)
class RegistrationControllerTest {
    @Autowired
    private RegistrationController registrationController;

    @MockBean
    private RegistrationService registrationService;

    /**
     * Method under test: {@link RegistrationController#confirm(String, HttpServletResponse)}
     */
    @Test
    void testConfirm() throws Exception {
        when(registrationService.confirmToken((String) any())).thenReturn("ABC123");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/register/confirm")
                .param("token", "foo");
        MockMvcBuilders.standaloneSetup(registrationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("ABC123"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("https://www.google.com/"));
    }

    /**
     * Method under test: {@link RegistrationController#register(RegistrationRequest)}
     */
    @Test
    void testRegister() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/register")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(
                objectMapper.writeValueAsString(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "iloveyou")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(registrationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }
}

