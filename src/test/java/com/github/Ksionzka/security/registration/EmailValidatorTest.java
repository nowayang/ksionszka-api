package com.github.Ksionzka.security.registration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailValidator.class})
@ExtendWith(SpringExtension.class)
class EmailValidatorTest {
    @Autowired
    private EmailValidator emailValidator;

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testValid() {
        assertTrue(emailValidator.test("jane.doe@example.org"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testValid2() {
        assertTrue(emailValidator.test("fsdf@gmail.com"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testValid3() {
        assertTrue(emailValidator.test("fefwef@mail.mail.com"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testInvalid() {
        assertFalse(emailValidator.test("U.U.U@UU.U.U.U"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testInvalid2() {
        assertFalse(emailValidator.test("text.text"));
    }

}

