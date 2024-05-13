package com.bunsen.webtechproject.service;

import com.bunsen.webtechproject.api.model.RegistrationBody;
import com.bunsen.webtechproject.exception.UserAlreadyExistException;
import com.bunsen.webtechproject.model.LocalUser;
import com.bunsen.webtechproject.model.dao.LocalUserDao;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private LocalUserDao localUserDao;

    public UserService(LocalUserDao localUserDao) {
        this.localUserDao = localUserDao;
    }

    public LocalUser registerUser( RegistrationBody registrationBody) throws UserAlreadyExistException {

        if (localUserDao.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                localUserDao.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstname());
        user.setLastname(registrationBody.getLastname());
        user.setUsername(registrationBody.getUsername());
        // TODO: Encrypt passwords!!!
        user.setPassword(registrationBody.getPassword());

        return localUserDao.save(user);
    }
}
