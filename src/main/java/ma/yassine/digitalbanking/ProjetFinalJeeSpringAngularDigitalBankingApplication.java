package ma.yassine.digitalbanking;

import ma.yassine.digitalbanking.dtos.BankAccountDTO;
import ma.yassine.digitalbanking.dtos.CustomerDTO;
import ma.yassine.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.yassine.digitalbanking.exceptions.CustomerNotFoundException;
import ma.yassine.digitalbanking.exceptions.InsufficientBalanceException;
import ma.yassine.digitalbanking.services.BankAccountServiceImpl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class ProjetFinalJeeSpringAngularDigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetFinalJeeSpringAngularDigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountServiceImpl bankAccountService){
        return args -> {
            Stream.of("Hamza", "Mohammed", "Khadija", "Mustapha").forEach(name ->{
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        double random = Math.random();
                        if (random < 0.5) {
                            bankAccountService.saveCurrentBankAccount(Math.random()*900000,customer.getId(),5000);
                        } else {
                            bankAccountService.saveSavingBankAccount(Math.random()*900000,customer.getId(),3.5);
                        }
                    }
                    List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccounts();
                    for (BankAccountDTO bankAccount : bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            bankAccountService.credit(Math.random()*15000,bankAccount.getId(),"Credit");
                        }
                        for (int i = 0; i < 6; i++) {
                            bankAccountService.debit(1000 + Math.random()*5000,bankAccount.getId(),"Debit");
                        }
                    }
                } catch (CustomerNotFoundException | BankAccountNotFoundException | InsufficientBalanceException e) {
                    e.printStackTrace();
                }
            });
        };
    }
}
