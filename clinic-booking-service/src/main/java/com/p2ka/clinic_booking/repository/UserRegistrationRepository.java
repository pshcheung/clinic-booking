package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRegistrationRepository extends CrudRepository<User, String> {
    User findByEmailAddress(String email);

/*    User findByUsername(String username);

    User findByEmailAndPassword(String email, String password);*/

    List<User> findProfileByEmailAddress(String email);
}
