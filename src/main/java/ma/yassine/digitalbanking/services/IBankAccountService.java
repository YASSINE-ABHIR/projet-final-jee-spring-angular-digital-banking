package ma.yassine.digitalbanking.services;

import ma.yassine.digitalbanking.dtos.*;
import ma.yassine.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.yassine.digitalbanking.exceptions.CustomerNotFoundException;
import ma.yassine.digitalbanking.exceptions.InsufficientBalanceException;

import java.util.List;

public interface IBankAccountService {
    CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException;
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Long customerId, double overDraft) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, Long customerId, double interestRate) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    List<BankAccountDTO> listBankAccounts();

    void credit(double amount, String accountId, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void debit(double amount, String accountId, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void transfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, InsufficientBalanceException;

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<OperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<BankAccountDTO> getCustomerAccounts(Long id);

    List<OperationDTO> getOperations();

    long getCountSavingAccounts();

    long getCountCurrentAccounts();

    List<Object[]> getOperationsCountByMonth();
}
