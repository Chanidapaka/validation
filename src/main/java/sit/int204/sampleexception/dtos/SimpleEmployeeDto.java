package sit.int204.sampleexception.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

//week 6 validation
@Data
public class SimpleEmployeeDto {
    private Integer employeeNumber;
    @JsonIgnore
    private String firstName;
    @JsonIgnore
    private String lastName;

    public String getName() {
        return firstName + ' ' + lastName;
    }
}
