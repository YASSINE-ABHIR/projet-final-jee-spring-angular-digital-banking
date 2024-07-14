package ma.yassine.digitalbanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.yassine.digitalbanking.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
