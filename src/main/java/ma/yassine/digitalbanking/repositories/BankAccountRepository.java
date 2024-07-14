package ma.yassine.digitalbanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.yassine.digitalbanking.entities.BankAccount;
import ma.yassine.digitalbanking.entities.Customer;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByCustomerId(Long id);
    /*int countAllSavingsAccounts();
    int countAllCurrentAccounts();*/
}
