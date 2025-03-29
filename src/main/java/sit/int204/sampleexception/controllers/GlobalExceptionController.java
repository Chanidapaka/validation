package sit.int204.sampleexception.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import sit.int204.sampleexception.exceptions.ItemNotFoundException;
import sit.int204.sampleexception.exceptions.MyErrorResponse;
import java.util.List;

//ช่วยจัดการข้อผิดพลาดของ Spring Boot API ได้ดีขึ้น โดยแยกการจัดการแต่ละข้อผิดพลาดให้เหมาะสมกับสถานการณ์
// week 4 exception
@RestControllerAdvice //เพื่อจัดการข้อผิดพลาดทั่วทั้งแอปพลิเคชัน

public class GlobalExceptionController {
    @Operation(hidden = false)

    // สำหรับจับข้อผิดพลาดเฉพาะประเภท
    @ExceptionHandler(IllegalArgumentException.class)
    //จับข้อผิดพลาด IllegalArgumentException  //เกิดขึ้นเมื่อพารามิเตอร์ที่ส่งมาไม่ถูกต้อง (อาจเกิดจากเงื่อนไขทางธุรกิจ)
    //ส่งสถานะ HTTP 400 BAD_REQUEST
    public ResponseEntity<MyErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(myErrorResponse);
    }

    //จับข้อผิดพลาด MissingServletRequestParameterException
    //เกิดขึ้นเมื่อมีพารามิเตอร์ที่ต้องการ (@RequestParam) แต่ไม่ได้ส่งมา //ส่งสถานะ HTTP 400 BAD_REQUEST
    @Operation(hidden = false)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<MyErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(myErrorResponse);
    }

    //จับข้อผิดพลาด ItemNotFoundException
    //ใช้สำหรับกรณีค้นหาข้อมูลไม่พบ เช่น customerService.findById(id) แล้วไม่มีข้อมูล
    //ส่งสถานะ HTTP 404 NOT_FOUND
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<MyErrorResponse> handleItemNotFoundException(
            ItemNotFoundException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(
                HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(myErrorResponse);
    }

    //จับข้อผิดพลาด MethodArgumentNotValidException
    //เกิดขึ้นเมื่อการตรวจสอบข้อมูล @Valid ใน @RequestBody ล้มเหลว
    //ใช้สำหรับ Validation ของฟิลด์ต่าง ๆ ใน DTO
    //ส่งสถานะ HTTP 400 BAD_REQUEST และแนบรายละเอียดของฟิลด์ที่ไม่ผ่านเงื่อนไข
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        MyErrorResponse errorResponse = new MyErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error. Check 'errors' field for details.",
                request.getRequestURI());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(
                    fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //week 6 validation
    //จับข้อผิดพลาด HandlerMethodValidationException
    //ใช้สำหรับการตรวจสอบ @RequestParam หรือ @PathVariable ที่มีการใช้ @Min @Max @NotNull
    //ดึงข้อมูลพารามิเตอร์ที่ผิดพลาดแล้วส่งกลับเป็น JSON
    //ส่งสถานะ HTTP 400 BAD_REQUEST
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<MyErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException exception, HttpServletRequest request) {
        MyErrorResponse errorResponse = new MyErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error. Check 'errors' field for details.", request.getRequestURI());
        List<ParameterValidationResult> paramNames = exception.getParameterValidationResults();
        for (ParameterValidationResult param : paramNames) {
            errorResponse.addValidationError(param.getMethodParameter().getParameterName(), param.getResolvableErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

//ใช้สำหรับจัดการข้อผิดพลาดทั่วไป (Exception.class)
//ส่งสถานะ HTTP 500 INTERNAL_SERVER_ERROR
//อาจใช้ใน production เพื่อป้องกันข้อมูล error รั่วไหล
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<MyErrorResponse> handleException(Exception ex, HttpServletRequest request) {
//        MyErrorResponse myErrorResponse = new MyErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR.value()
//                , "Something Wrong, Please contact back-end support"
//                , request.getRequestURI()
//        );
//        myErrorResponse.setStackTrace(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myErrorResponse);
//    }


}
