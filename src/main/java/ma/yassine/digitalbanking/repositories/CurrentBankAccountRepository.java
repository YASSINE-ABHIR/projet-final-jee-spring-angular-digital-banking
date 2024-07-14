package ma.yassine.digitalbanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.yassine.digitalbanking.entities.CurrentAccount;

public interface CurrentBankAccountRepository extends JpaRepository<CurrentAccount, String> {
}
