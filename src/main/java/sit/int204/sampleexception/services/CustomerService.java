package sit.int204.sampleexception.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.sampleexception.dtos.NewCustomerDto;
import sit.int204.sampleexception.entities.Customer;
import sit.int204.sampleexception.repositories.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository repository;
    @Autowired
    ModelMapper mapper;

    public NewCustomerDto createCustomer(NewCustomerDto newCustomer) {
        if (repository.existsById(newCustomer.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , "Duplicate customer for id " + newCustomer.getId());
        }
        Customer customer = mapper.map(newCustomer, Customer.class);
        return mapper.map(repository.saveAndFlush(customer), NewCustomerDto.class);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }
}
