package sit.int204.sampleexception.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

//week 06 validation
//ใช้สำหรับรับและตรวจสอบข้อมูล โดยมีการ Validation ให้แน่ใจว่าข้อมูลที่รับมาถูกต้อง
@Data
public class SampleData {
    @NotNull //ห้ามว่าง
    @Min(10001) //ค่าเริ่มต้น
    @Max(99999) //ไม่เกิน
    private Integer id;

    @NotBlank //ห้ามว่าง
    @Size(max = 10) //ความยาวไม่เกิน 10
    private String name;

    @Email //ต้องเป็นอีเมลที่ถูกต้อง
    private String email;

    @Past //ต้องเป็นวันที่ในอดีต
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    //มันจะลบช่องว่างของชื่อออก เช่น "   Aom  " -> จะได้ "Aom"
    public void setName(String name) {
        this.name = name == null ? name : name.trim();
    }

    @NotNull
    private Integer midScore ;
    @NotNull
    private Integer finScore;

    @AssertTrue(message = "Total score must greater than or equals 30")
    @JsonIgnore //เพื่อไม่ให้มีใน JSON
    public boolean isValidScore() {
            return (midScore + finScore) >= 30;

    }
}
