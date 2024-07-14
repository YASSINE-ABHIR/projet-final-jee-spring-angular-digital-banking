package ma.yassine.digitalbanking.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.yassine.digitalbanking.enumirats.OperationType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private OperationType type;
    private String description;
}
