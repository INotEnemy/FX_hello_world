package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String     fio;

    private Short      grpNum;

    private BigDecimal grants;

    private Integer    profNum;

    private String     zachNum;
}
