package br.com.senacsp.tads.stads4ma.library.service;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import br.com.senacsp.tads.stads4ma.library.domainmodel.User_old;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserService implements UserDetails {
    public List<User> findAll();

    public User findById(UUID id);

    public boolean deleteById(UUID id);


    public User create(User user);

    boolean existsById(UUID id);

    User update(User databaseUser);
}
