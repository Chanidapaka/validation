package sit.int204.sampleexception.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class SimpleMyErrorResponseForApiDoc {
    @Schema(example = "404")
    private int status;

    @Schema(example = "Resource NOT FOUND")
    private String message;

    @Schema(example = "/api/products/S10_123")
    private String instance;

    private Instant timestamp = Instant.now();
}
