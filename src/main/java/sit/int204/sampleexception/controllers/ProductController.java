package sit.int204.sampleexception.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int204.sampleexception.dtos.PageDto;
import sit.int204.sampleexception.dtos.ProductDtoA;
import sit.int204.sampleexception.dtos.SampleData;
import sit.int204.sampleexception.entities.Product;
import sit.int204.sampleexception.exceptions.MyErrorResponse;
import sit.int204.sampleexception.exceptions.SimpleMyErrorResponseForApiDoc;
import sit.int204.sampleexception.services.ProductService;
import sit.int204.sampleexception.utils.ListMapper;

// week 5 และ 6
//Controller ที่จัดการ API สำหรับสินค้า
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    protected ListMapper listMapper;

    //เพื่อให้ API มีเอกสารอธิบาย และรองรับการตอบกลับกรณีเกิดข้อผิดพลาด
    @Operation(summary = "Retrive product by id such as S10_5879")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = ProductDtoA.class))}),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = {
                            @Content(schema = @Schema(implementation = SimpleMyErrorResponseForApiDoc.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request (Invalid arguments)",
                    content = {
                            @Content(schema = @Schema(implementation = MyErrorResponse.class))})
    })

    //ดึงข้อมูลสินค้าตาม id (เช่น S10_5879) และแปลงเป็น ProductDtoA
    @GetMapping("/{id}")
    public ResponseEntity<ProductDtoA> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(
                modelMapper.map(productService.findById(id), ProductDtoA.class)
        );
    }

    //ดึงรายการสินค้าทั้งหมดแบบแบ่งหน้า (pagination)
    @GetMapping("")
    public ResponseEntity<PageDto<ProductDtoA>> getAllProducts(
            @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Page<Product> productPage = productService.findAll(pageNo, pageSize);
        return ResponseEntity.ok(listMapper.toPageDTO(
                productPage, ProductDtoA.class, modelMapper));
    }

    //ใช้ ทดสอบการตรวจสอบข้อมูล (@Valid) เช่น ID, คะแนนสอบ, และวันเกิด
    @PostMapping("/validations")
    public ResponseEntity<Object> testValidation(
            @Valid @RequestBody SampleData sampleData) {
        return ResponseEntity.ok(sampleData);
    }
}
