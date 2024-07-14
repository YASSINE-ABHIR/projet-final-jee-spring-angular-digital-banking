package ma.yassine.digitalbanking.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingBankAccountDTO extends BankAccountDTO {
    private double interestRate;
}
