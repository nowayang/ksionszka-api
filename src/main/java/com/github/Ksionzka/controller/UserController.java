package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.*;
import com.github.Ksionzka.persistence.entity.BookEntity;
import com.github.Ksionzka.persistence.entity.ReservationEntity;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.BookRepository;
import com.github.Ksionzka.persistence.repository.ReleaseRepository;
import com.github.Ksionzka.persistence.repository.UserRepository;
import com.github.Ksionzka.security.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements BaseController<UserEntity, Long> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @GetMapping
    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Pageable pageable, @RequestParam String search) {
        final String searchTerm = this.getSearchTerm(search);
        return this.userRepository.findAll(
            (Specification<UserEntity>)
                (root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("firstName")), searchTerm),
                    cb.like(cb.lower(root.get("lastName")), searchTerm),
                    cb.like(cb.lower(root.get("email")), searchTerm)
                ),
            pageable
        );
    }

    @Override
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public UserEntity getById(@PathVariable Long id) {
        return this.userRepository.getOrThrowById(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public UserEntity updateById(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        UserEntity userEntity = this.userRepository.getOrThrowById(id);
        BeanUtils.copyProperties(request, userEntity);
        return this.userRepository.save(userEntity);
    }

    @PostMapping("/{id}/change-password")
    @Transactional
    public UserEntity changePasswordById(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        UserEntity userEntity = this.userRepository.getOrThrowById(id);

        if (!this.passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("BAD_PASSWORD");
        }

        BeanUtils.copyProperties(request, userEntity);
        return this.userRepository.save(userEntity);
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable Long id) {
        this.userRepository.deleteById(id);
    }
}
