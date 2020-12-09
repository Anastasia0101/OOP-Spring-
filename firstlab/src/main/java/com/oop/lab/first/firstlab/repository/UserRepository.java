package com.oop.lab.first.firstlab.repository;

 
import com.oop.lab.first.firstlab.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
 

public interface UserRepository extends PagingAndSortingRepository<User, Long>, 
        JpaSpecificationExecutor<User> {
}