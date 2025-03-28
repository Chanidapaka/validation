package sit.int204.sampleexception.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int204.sampleexception.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
