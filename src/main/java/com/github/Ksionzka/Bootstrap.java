package com.github.Ksionzka;

import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.Genre;
import com.github.Ksionzka.persistence.entity.ReleaseEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
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

    @PostConstruct
    @Transactional
    void bootstrapData() {
        this.addUser(new UserEntity("admin", "admin", "admin@admin", "admin", Role.LIBRARIAN, true));
        this.addUser(new UserEntity("Karol", "Nowacki", "karol.nowacki@gmail.com", "password", Role.LIBRARIAN, true));
        this.addUser(new UserEntity("Andrzej", "Nowak", "andrze.nowak@gmail.com", "password", Role.USER, true));
        this.addUser(new UserEntity("Władysław", "Mol", "władek2137@gmail.com", "password", Role.USER, true));

        this.addRelease("978-3-16-148410-0", "J.K. Rowling", "Rich publisher",
            Genre.Fantastyka, LocalDate.now(), List.of("001/15/2012", "002/15/2012", "003/15/2012"), "Morze czerwone");
        this.addRelease("378-3-16-148410-1", "W. Romtz", "Google", Genre.Fantastyka,
            LocalDate.now().minusDays(5), List.of("001/04/2022", "002/04/2022", "003/04/2022"), "Jak obniżyć libido");
        this.addRelease("978-3-16-148410-2", "M. Mertz", "V Dynamics", Genre.Fantastyka,
            LocalDate.now().minusYears(2), List.of("001/04/2018", "002/04/2018", "003/04/2018"), "W pustyni i w puszczy");

    }

    void addUser(UserEntity userEntity) {
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

        if (this.releaseRepository.existsById(releaseEntity.getId())) {
            ReleaseEntity entity = this.releaseRepository.saveAndFlush(releaseEntity);

            for (String bookId : booksId) {
                this.addBook(bookId,  bookName, entity);
            }
        }
    }

    void addBook(String id, String name, ReleaseEntity releaseEntity) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setRelease(releaseEntity);
        bookEntity.setId(id);
        bookEntity.setName(name);

        if (this.bookRepository.existsById(bookEntity.getId())) {
            this.bookRepository.saveAndFlush(bookEntity);
        }
    }
}
