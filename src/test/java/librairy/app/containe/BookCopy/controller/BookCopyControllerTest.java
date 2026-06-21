package librairy.app.containe.BookCopy.controller;

import librairy.app.containe.bookCopy.controller.BookCopyController;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest({BookCopyController.class , GlobalExceptionHandler.class})


class BookCopyControllerTest {
    private BookCopy HarryPotter;
    private BookCopy ORV;

    @BeforeEach
    void setup() {
        HarryPotter = BookCopy.builder()
                .id(UUID.randomUUID().toString())
                .
    }

    @Test
    void getAll_shouldReturn_200_when_FindAllBookCopy() {
    }

    @Test
    void getAll_shouldReturn_404_when_notFindAnyBookCopy() {

    }

    @Test
    void getById_shouldReturn_200_when_FindBookCopyById() {
    }

    @Test
    void getById_shouldReturn_404_when_notFindBookCopyById() {

    }
}