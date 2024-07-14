package ma.yassine.digitalbanking.services;

import lombok.AllArgsConstructor;
import ma.yassine.digitalbanking.dtos.*;
import ma.yassine.digitalbanking.entities.*;
import ma.yassine.digitalbanking.enumirats.AccountStatus;
import ma.yassine.digitalbanking.enumirats.OperationType;
import ma.yassine.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.yassine.digitalbanking.exceptions.CustomerNotFoundException;
import ma.yassine.digitalbanking.exceptions.InsufficientBalanceException;
import ma.yassine.digitalbanking.mappers.IAppMapper;
import ma.yassine.digitalbanking.repositories.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements IBankAccountService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private OperationRepository operationRepository;
    private CurrentBankAccountRepository currentBankAccountRepository;
    private SavingBankAccountRepository savingBankAccountRepository;
    private IAppMapper appMapper;

    //Customer methods

    @Override
    public CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return appMapper.toCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> appMapper.toCustomerDTO(customer)).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return appMapper.toCustomerDTO(customer);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = appMapper.toCustomer(customerDTO);
        return appMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = appMapper.toCustomer(customerDTO);
        return appMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    //BankAccount methods

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Long customerId, double overDraft) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount account = new CurrentAccount();
        double random = Math.random();
        account.setBalance(initialBalance);
        account.setCustomer(customer);
        account.setCreatedAt(new Date());
        account.setStatus(random <= 0.3 ? AccountStatus.CREATED : random <= 0.6 ? AccountStatus.ACTIVATED : AccountStatus.SUSPENDED);
        account.setCurrency("MAD");
        account.setOverDraft(overDraft);
        return appMapper.toCurrentBankAccountDTO(bankAccountRepository.save(account));
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, Long customerId, double interestRate) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SavingAccount account = new SavingAccount();
        double random = Math.random();
        account.setBalance(initialBalance);
        account.setCustomer(customer);
        account.setCreatedAt(new Date());
        account.setStatus(random <= 0.3 ? AccountStatus.CREATED : random <= 0.6 ? AccountStatus.ACTIVATED : AccountStatus.SUSPENDED);
        account.setCurrency("MAD");
        account.setInterestRate(interestRate);
        return appMapper.toSavingBankAccountDTO(bankAccountRepository.save(account));
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException{
       BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
       if(bankAccount instanceof CurrentAccount currentAccount){
           return appMapper.toCurrentBankAccountDTO(currentAccount);
       } else {
           SavingAccount savingAccount = (SavingAccount) bankAccount;
           return appMapper.toSavingBankAccountDTO(savingAccount);
       }
    }

    @Override
    public List<BankAccountDTO> listBankAccounts(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(account -> {
            if (account instanceof CurrentAccount currentAccount) {
                return appMapper.toCurrentBankAccountDTO(currentAccount);
            } else {
                SavingAccount savingAccount = (SavingAccount) account;
                return appMapper.toSavingBankAccountDTO(savingAccount);
            }
        }).toList();

    }

    @Override
    public void credit(double amount, String accountId, String description) throws BankAccountNotFoundException, InsufficientBalanceException {
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("The account balance is insufficient.");
        }
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setAccount(account);
        operation.setDate(new Date(2024, (int)(Math.random() * 12),1));
        operation.setType(OperationType.CREDIT);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void debit(double amount, String accountId, String description) throws BankAccountNotFoundException, InsufficientBalanceException {
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setAccount(account);
        operation.setDate(new Date(2024, (int)(Math.random() * 12),1));
        operation.setType(OperationType.DEBIT);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void transfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, InsufficientBalanceException {
        debit(amount, accountSourceId, "Transfer to " + accountDestinationId);
        credit(amount, accountDestinationId, "Transfer from " + accountSourceId);
    }

    @Override
    public List<OperationDTO> accountHistory(String accountId){
        List<Operation> operations = operationRepository.findByAccount_Id(accountId);
        return operations.stream().map(operation -> appMapper.toOperationDTO(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException{
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null) throw new BankAccountNotFoundException("Bank account not found");
        Page<Operation> accountOperations = operationRepository.findByAccount_Id(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<OperationDTO> operationDTOS = accountOperations.getContent().stream().map(operation -> appMapper.toOperationDTO(operation)).collect(Collectors.toList());
        accountHistoryDTO.setOperationDTOList(operationDTOS);
        accountHistoryDTO.setId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setTotalElements(accountOperations.getTotalElements());
        return accountHistoryDTO;
    }

    @Override
    public List<BankAccountDTO> getCustomerAccounts(Long id) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(id);
        return bankAccounts.stream().map(bankAccount -> appMapper.toBankAccountDTO(bankAccount)).toList();
    }

    @Override
    public List<OperationDTO> getOperations(){
        List<Operation> operations = operationRepository.findAll();
        return operations.stream().map(operation -> appMapper.toOperationDTO(operation)).collect(Collectors.toList());
    }

    @Override
    public long getCountCurrentAccounts(){
        return currentBankAccountRepository.count();
    }

    @Override
    public long getCountSavingAccounts(){
        return savingBankAccountRepository.count();
    }

    @Override
    public List<Object[]> getOperationsCountByMonth() {
        List<Object[]> debitResults = operationRepository.countDebitOperationsByMonth();
        List<Object[]> creditResults = operationRepository.countCreditOperationsByMonth();

        // Prepare final results list
        List<Object[]> finalResults = new ArrayList<>();

        // Process debit results
        List<Object[]> processedDebitResults = operationCountList(debitResults);
        finalResults.add(new Object[]{"Debit", processedDebitResults});

        // Process credit results
        List<Object[]> processedCreditResults = operationCountList(creditResults);
        finalResults.add(new Object[]{"Credit", processedCreditResults});

        return finalResults;
    }

    List<Object[]> operationCountList(List<Object[]> list){
        Map<Integer, Long> monthCountMap = new LinkedHashMap<>();

        // Initialize the map with all months and zero counts
        for (int month = 1; month <= 12; month++) {
            monthCountMap.put(month, 0L);
        }

        // Populate the map with actual counts from the query
        for (Object[] result : list) {
            Integer month = (Integer) result[0];
            Long count = (Long) result[1];
            monthCountMap.put(month, count);
        }

        // Convert the map to a list of Object[] for the result
        List<Object[]> finalResults = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : monthCountMap.entrySet()) {
            finalResults.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        return finalResults;
    }
}
