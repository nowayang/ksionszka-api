package com.github.Ksionzka;

import com.github.Ksionzka.persistence.entity.*;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Bootstrap {
    private final UserRepository userRepository;
    private final ReleaseRepository releaseRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    void bootstrapData() {
        this.addUser(new UserEntity("admin", "admin", "admin@gmail.com", "admin", Role.LIBRARIAN, true));
        this.addUser(new UserEntity("Karol", "Nowacki", "karol.nowacki@gmail.com", "password", Role.LIBRARIAN, true));
        this.addUser(new UserEntity("Andrzej", "Nowak", "andrze.nowak@gmail.com", "password", Role.USER, true));
        this.addUser(new UserEntity("Władysław", "Mol", "władek2137@gmail.com", "password", Role.USER, true));

        this.addRelease("978-3-16-148410-0", "J.K. Rowling", "Rich publisher",
            Genre.Fantastyka, LocalDate.now(), List.of("001/15/2012", "002/15/2012", "003/15/2012"), "Morze czerwone");
        this.addRelease("378-3-16-148410-1", "W. Romtz", "Google", Genre.Fantastyka,
            LocalDate.now().minusDays(5), List.of("001/04/2022", "002/04/2022", "003/04/2022"), "Jak obniżyć libido");
        this.addRelease("978-3-16-148410-2", "M. Mertz", "V Dynamics", Genre.Fantastyka,
            LocalDate.now().minusYears(2), List.of("001/04/2018", "002/04/2018", "003/04/2018"), "W pustyni i w puszczy");
        this.addRelease("978-3-16-148410-3", "J.K. Rowling", "Media Rodzina",
                Genre.SciFi, LocalDate.of(2016, 7, 20), List.of("001/04/2023", "002/04/2023"), "Harry Potter i Kamień Filozoficzny");
        this.addRelease("978-3-16-148410-4", "Antoine de Saint-Exupéry", "Wydawnictwo SMB",
                Genre.Fantastyka, LocalDate.of(2020, 10, 12), List.of("001/04/2024", "002/04/2024"), "Mały Książę");
        this.addRelease("978-3-16-148410-5", "Michaił Bułhakow", "Grove Press",
                Genre.Powieść, LocalDate.of(1994, 1, 13), List.of("001/04/2025", "002/04/2025"), "Mistrz i Małgorzata");
        this.addRelease("978-3-16-148410-6", "George Orwell", "Rebis",
                Genre.SciFi, LocalDate.of(1994, 1, 13), List.of("001/04/2027", "002/04/2027"), "Rok 1984");
        this.addRelease("978-3-16-148410-8", "George Orwell", "Media Rodzina",
                Genre.SciFi, LocalDate.of(2015, 11, 11), List.of("003/04/2027", "004/04/2027"), "Rok 1984");
        this.addRelease("978-3-16-148410-8", "Bolesław Prus", "Greg",
                Genre.Powieść, LocalDate.of(2018, 2, 11), List.of("003/04/2028", "004/04/2028"), "Lalka");
        this.addRelease("978-3-16-148410-9", "Walter Isaacson", "Insignis",
                Genre.Powieść, LocalDate.of(2013, 2, 1), List.of("003/04/2029", "004/04/2029"), "Steve Jobs");
        this.addRelease("978-3-16-148411-0", "Ewa Stachniak", "Greg",
                Genre.Biografia, LocalDate.of(2003, 12, 11), List.of("003/04/2031", "004/04/2031"), "Katarzyna Wielka. Gra o władzę");
        this.addRelease("978-3-16-148411-1", "Camilla Läckberg", "Greg",
                Genre.Kryminał, LocalDate.of(2022, 12, 11), List.of("003/04/2032", "004/04/2032"), "Księżniczka z lodu");
        this.addRelease("978-3-16-148411-2", "Dan Brown", "Insignis",
                Genre.Kryminał, LocalDate.of(2003, 12, 11), List.of("003/04/2033", "004/04/2033"), "Inferno");
    }

    void addUser(UserEntity userEntity) {
        userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword()));

        if (this.userRepository.findByEmail(userEntity.getEmail()).isEmpty()) {
            this.userRepository.saveAndFlush(userEntity);
        }
    }

    void addRelease(String id, String author, String publisher, Genre genre, LocalDate releaseDate, List<String> booksId, String bookName) {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setReleaseDate(releaseDate);
        releaseEntity.setAuthor(author);
        releaseEntity.setPublisher(publisher);
        releaseEntity.setLanguage("pl");
        releaseEntity.setId(id);
        releaseEntity.setGenre(genre);
        releaseEntity.setTitle(bookName);

        if (!this.releaseRepository.existsById(releaseEntity.getId())) {
            ReleaseEntity entity = this.releaseRepository.saveAndFlush(releaseEntity);

            for (String bookId : booksId) {
                this.addBook(bookId, entity);
            }
        }
    }

    void addBook(String number, ReleaseEntity releaseEntity) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setRelease(releaseEntity);
        bookEntity.setNumber(number);
        this.bookRepository.save(bookEntity);
    }
}
