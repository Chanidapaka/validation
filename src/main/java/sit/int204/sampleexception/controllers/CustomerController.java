package sit.int204.sampleexception.controllers;

import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int204.sampleexception.dtos.NewCustomerDto;
import sit.int204.sampleexception.entities.Customer;
import sit.int204.sampleexception.services.CustomerService;

//week 6 validation
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    // ดึงข้อมูลลูกค้า (ตัวอย่างนี้จะเกิด Error แบ่งเลขด้วยศูนย์)
    @GetMapping
    public ResponseEntity<String> getCustomers(
            @RequestParam String name) {
        int x = 100/0; // จะเกิด Exception (ArithmeticException: / by zero)
        return ResponseEntity.ok(name);
    }

    // เพิ่มข้อมูลลูกค้าใหม่
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NewCustomerDto> addCustomer(
            @RequestBody @Valid NewCustomerDto customer, @RequestParam @Min(0) Integer x) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerService.createCustomer(customer));
    }
}
