package ma.yassine.digitalbanking.dtos;

import java.util.List;

import lombok.Data;

@Data
public class AccountHistoryDTO {
    private List<OperationDTO> operationDTOList;
    private String id;
    private double balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
