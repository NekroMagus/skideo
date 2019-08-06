package com.football.manager.service;

import com.football.manager.dao.UserDao;
import com.football.manager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link com.football.manager.service.UserService} interface.
 *
 * @author Igor Fliginskikh
 * @version 1.0
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao dao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return dao.findByUsername(username);
    }
}
