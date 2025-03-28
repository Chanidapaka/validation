package sit.int204.sampleexception.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SampleData {
    @NotNull
    @Min(10001)
    @Max(99999)
    private Integer id ;

    @NotBlank
    @Size(max = 10)
    private String name;

    @Email
    private String email;

    @Past
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;

    public void setName(String name) {
        this.name = name==null?name:name.trim();
    }

    @NotNull
    private Integer midScore;
    @NotNull
    private Integer finScore;

    @AssertTrue(message = "Total score must greater than or equals 30")
    @JsonIgnore
    public boolean isValidScore() {
        return (midScore+finScore)>=30 ;
    }
}
