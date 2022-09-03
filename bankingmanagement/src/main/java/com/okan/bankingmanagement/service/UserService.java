package com.okan.bankingmanagement.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.okan.bankingmanagement.domain.HttpResponse;
import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.domain.UserPrincipal;
import com.okan.bankingmanagement.dto.response.UserRegisterResponse;
import com.okan.bankingmanagement.dto.response.UserResponse;
import com.okan.bankingmanagement.exception.DeletedAccountException;
import com.okan.bankingmanagement.exception.EmailExistException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.exception.UserNotFoundException;
import com.okan.bankingmanagement.exception.UsernameExistException;
import com.okan.bankingmanagement.repository.UserRepository;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.rmi.UnexpectedException;

import static com.okan.bankingmanagement.constant.UserImplConstant.*;
import static com.okan.bankingmanagement.constant.Role.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserService implements UserDetailsService {

	private final UserRepository userRepo;
	private final ModelMapper mapper;
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepo, ModelMapper mapper, BCryptPasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponse getUser(int id) {
		UserResponse response = mapper.map(userRepo.getUser(id), UserResponse.class);
		return response;
	}

	public UserResponse updateUser(int id) throws UnexpectedErrorException {
		User user = userRepo.getUser(id);
		if(user != null) {
			user.setEnabled(!user.isEnabled());
			userRepo.updateUser(user);
			UserResponse res = mapper.map(user, UserResponse.class);
			return res;
		}
		return null;
		
		
	}
	
	public void register(String username, String email, String password)
			throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
		validateNewUsernameAndEmail(EMPTY, username, email);
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encodePassword(password));
		user.setEnabled(true);
		user.setRoles(ROLE_USER.name());
		user.setAuthorities(ROLE_USER.getAuthorities());
		userRepo.save(user) ;
		
		
		
	}

	private String generateUserId() {
		return RandomStringUtils.randomNumeric(10);
	}
	
	private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
	
	private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);
		if (StringUtils.isNotBlank(currentUsername)) {
			User currentUser = findUserByUsername(currentUsername);
			if (currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
			}
			if (userByNewUsername != null && !(currentUser.getId() == userByNewUsername.getId())) {
				throw new UsernameExistException(USERNAME_ALREADY_EXISTS + newUsername);
			}
			if (userByNewEmail != null && !(currentUser.getId() == userByNewEmail.getId())) {
				throw new EmailExistException(EMAIL_ALREADY_EXISTS + newEmail);
			}
			return currentUser;
		} else {
			if (userByNewUsername != null) {
				throw new UsernameExistException(USERNAME_ALREADY_EXISTS + newUsername);
			}
			if (userByNewEmail != null) {
				throw new EmailExistException(EMAIL_ALREADY_EXISTS + newEmail);
			}
			return null;
		}
	}

	@Cacheable(cacheNames = {"users"}, key = "#username")
	public User findUserByUsername(String username) {
		return userRepo.findUserByUsername(username);
	}

	@Cacheable(cacheNames = {"users"}, key = "#email")
	public User findUserByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}

	@Override
	@Cacheable(cacheNames = {"userDetails"}, key = "#username")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findUserByUsername(username);
        if (user != null) {
        	//validateLoginAttempt(user);
            //user.setLastLoginDateDisplay(user.getLastLoginDate());
            //user.setLastLoginDate(new Date());
            //userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            //LOGGER.info(FOUND_USER_BY_USERNAME + username);
            System.out.println(FOUND_USER_BY_USERNAME + username);
            
            return userPrincipal;
        } else {
            //LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
        	System.out.println(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
         
        }
	}
	
	

	
	
}
