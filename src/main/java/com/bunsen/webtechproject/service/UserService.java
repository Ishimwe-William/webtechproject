package com.bunsen.webtechproject.service;

import com.bunsen.webtechproject.api.model.LoginBody;
import com.bunsen.webtechproject.api.model.RegistrationBody;
import com.bunsen.webtechproject.exception.UserAlreadyExistException;
import com.bunsen.webtechproject.model.LocalUser;
import com.bunsen.webtechproject.model.dao.LocalUserDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private LocalUserDao localUserDao;
    private EncryptionService encryptionService;
    private JWTService jwtService;

    public UserService(LocalUserDao localUserDao, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDao = localUserDao;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistException {

        if (localUserDao.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                localUserDao.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstname());
        user.setLastname(registrationBody.getLastname());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        return localUserDao.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = localUserDao.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}
