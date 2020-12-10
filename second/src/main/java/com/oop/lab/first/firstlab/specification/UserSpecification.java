package com.oop.lab.first.firstlab.specification;

import com.oop.lab.first.firstlab.model.User;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification implements Specification<User>  {
    private User filter;
 
    public UserSpecification(User filter) {
        super();
        this.filter = filter;
    }
 
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq,
            CriteriaBuilder cb) {
 
        Predicate p = cb.disjunction();
 
        if (filter.getName() != null) {
            p.getExpressions().add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
        }
 
        if (filter.getAge()!= null) {
            p.getExpressions().add(cb.like(root.get("age"), "%" + filter.getAge() + "%"));
        }
 
        return p;
    }
}
