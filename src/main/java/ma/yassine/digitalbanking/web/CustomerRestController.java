package ma.yassine.digitalbanking.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yassine.digitalbanking.dtos.CustomerDTO;
import ma.yassine.digitalbanking.exceptions.CustomerNotFoundException;
import ma.yassine.digitalbanking.services.BankAccountServiceImpl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountServiceImpl bankAccountService;

    @GetMapping("/customers/all")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    public List<CustomerDTO> getAllCustomers() {
        return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER')")
    public CustomerDTO getCustomerById(@PathVariable Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customers/new")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        getCustomerById(id);
        customerDTO.setId(id);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        getCustomerById(id);
        bankAccountService.deleteCustomer(id);
    }
}
