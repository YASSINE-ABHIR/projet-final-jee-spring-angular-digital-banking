package ma.yassine.digitalbanking.mappers;

import ma.yassine.digitalbanking.dtos.*;
import ma.yassine.digitalbanking.entities.*;

public interface IAppMapper {
    Customer toCustomer(CustomerDTO customerDTO);

    CustomerDTO toCustomerDTO(Customer customer);

    CurrentBankAccountDTO toCurrentBankAccountDTO(CurrentAccount currentBankAccount);

    CurrentAccount toCurrentBankAccount(CurrentBankAccountDTO currentBankAccountDTO);

    SavingBankAccountDTO toSavingBankAccountDTO(SavingAccount savingBankAccount);

    SavingAccount toSavingBankAccount(SavingBankAccountDTO savingBankAccountDTO);

    BankAccountDTO toBankAccountDTO(BankAccount bankAccount);

    BankAccount toBankAccount(BankAccountDTO bankAccountDTO);

    OperationDTO toOperationDTO(Operation operation);

    Operation toOperation(OperationDTO operationDTO);
}
