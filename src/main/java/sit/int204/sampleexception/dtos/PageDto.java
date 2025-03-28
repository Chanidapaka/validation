package sit.int204.sampleexception.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageDto<T> {
    private List<T> content;
    private Boolean last;
    private Boolean first;
    private Integer totalPages;
    private Integer size;
    @JsonIgnore
    private Integer number;

    public Integer getPageNumber() {
        return number;
    }
}
