package ma.yassine.digitalbanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.yassine.digitalbanking.entities.SavingAccount;

public interface SavingBankAccountRepository extends JpaRepository<SavingAccount, String> {
}
