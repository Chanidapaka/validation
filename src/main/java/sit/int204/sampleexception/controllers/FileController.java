package sit.int204.sampleexception.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int204.sampleexception.dtos.JwtRequestUser;
import sit.int204.sampleexception.services.FileService;
import sit.int204.sampleexception.utils.FileStorageProperties;

import java.util.List;

//week 7 File Services
//ตัวไฟล์ที่เอาไว้รับ
@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileStorageProperties fileStorageProperties;
    @Autowired
    private FileService fileService;

    //ทดสอบการตั้งค่าของโฟลเดอร์อัปโหลด
    @GetMapping("/test")
    public ResponseEntity<Object> testPropertiesMapping() {
        return ResponseEntity.ok(
                "Upload Folder (Directory) is \""
                        + fileStorageProperties.getUploadDir() + "\"");
    }

    // คืนค่ารายการประเภทไฟล์ที่รองรับ
    @GetMapping("/support-file-types")
    public ResponseEntity<Object> getSupportFileTypes() {
        return ResponseEntity.ok(fileStorageProperties.getSupportFileTypes());
    }

    // อัปโหลดไฟล์เดี่ยว
    @PostMapping("")
    public ResponseEntity<Object> fileUpload(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                "You successfully uploaded " + fileService.store(file));
    }

    // ดาวน์โหลดไฟล์จากเซิร์ฟเวอร์
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(fileService.getFileType(file))).body(file);
    }

    // ลบไฟล์
    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<Object> removeFile(@PathVariable String filename) {
        fileService.removeFile(filename);
        return ResponseEntity.ok(filename + " has been removed !");
    }

    //week 8 JWT-based Authentication
    // อัปโหลดไฟล์พร้อมข้อมูลผู้ใช้ (JWT)
    @PostMapping("/demos/1")
    public ResponseEntity<Object> createUser(
            @ModelAttribute JwtRequestUser user
            , @RequestParam MultipartFile file) {
        fileService.store(file);
        return ResponseEntity.ok(user);
    }


    // อัปโหลดไฟล์หลายไฟล์พร้อมข้อมูลผู้ใช้ (JWT)
    @PostMapping("/demos/2")
    public ResponseEntity<Object> createUserDemo2(
            @ModelAttribute JwtRequestUser user
            , @RequestParam List<MultipartFile> files) {
        fileService.store(files);
        return ResponseEntity.ok(user);
    }

    // ค้นหาไฟล์ที่ตรงกับรูปแบบที่ระบุ
    @GetMapping("/multiple-files/{pattern:.+}")
    @ResponseBody
    public ResponseEntity<List<String>> getFiles(@PathVariable String pattern) {
        return ResponseEntity.ok(
                fileService.getMatchedFiles(pattern));
    }

}
