package com.github.Ksionzka.controller;

import com.github.Ksionzka.controller.dto.ChangePasswordRequest;
import com.github.Ksionzka.controller.dto.UpdateUserRequest;
import com.github.Ksionzka.persistence.entity.UserEntity;
import com.github.Ksionzka.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements BaseController<UserEntity, Long> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @GetMapping
    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Pageable pageable, @RequestParam(required = false) String search) {
        final String searchTerm = this.getSearchTerm(search);

        Specification<UserEntity> specification = Specification.where(null);

        if (Strings.isNotBlank(search)) {
            specification = specification.and((root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("firstName")), searchTerm),
                cb.like(cb.lower(root.get("lastName")), searchTerm),
                cb.like(cb.lower(root.get("email")), searchTerm)
            ));
        }

        return this.userRepository.findAll(specification, pageable);
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
    public UserEntity changePasswordById(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
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
    @Secured("ROLE_LIBRARIAN")
    public void deleteById(@PathVariable Long id) {
        this.userRepository.deleteById(id);
    }
}
