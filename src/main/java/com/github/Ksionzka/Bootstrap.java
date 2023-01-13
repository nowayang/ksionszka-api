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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class Bootstrap {
    private final UserRepository userRepository;
    private final ReleaseRepository releaseRepository;
    private final BookRepository bookRepository;

    @PostConstruct
    @Transactional
    void bootstrapData() {
        if (this.addAdminUser()) {
            this.addReleases();
            this.addBook();

            //todo 2 users
            //todo 2 releases
            //todo 3 books

        }
    }

    private boolean addAdminUser() {
        UserEntity userEntity = new UserEntity(
            "admin",
            "admin",
            "admin@admin",
            "admin",
            Role.LIBRARIAN
        );

        if (this.userRepository.findByEmail(userEntity.getEmail()).isEmpty()) {
            this.userRepository.save(userEntity);

            return true;
        } else {
            return false;
        }
    }

    private void addReleases() {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setReleaseDate(LocalDate.now());
        releaseEntity.setAuthor("J.K. Rowling");
        releaseEntity.setPublisher("Rich publisher");
        releaseEntity.setLanguage("pl");
        releaseEntity.setId("978-3-16-148410-0");
        releaseEntity.setGenre(Genre.Fantastyka);
        this.releaseRepository.save(releaseEntity);
    }

    private void addBook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setRelease(this.releaseRepository.getOrThrowById("978-3-16-148410-0"));
        bookEntity.setId("001/15/2016");
        bookEntity.setName("W pustyni i w puszczy");
        this.bookRepository.save(bookEntity);
    }
}
