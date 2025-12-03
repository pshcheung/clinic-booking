package com.p2ka.clinic_booking.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.p2ka.clinic_booking.model.User;

public interface UserRegistrationRepository extends CrudRepository<User, String>
{
	
    public User findByEmail(String email);
	
	public User findByUsername(String username);
	
	public User findByEmailAndPassword(String email, String password);
	
	public List<User> findProfileByEmail(String email);

}
