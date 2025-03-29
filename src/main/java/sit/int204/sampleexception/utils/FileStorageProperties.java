package sit.int204.sampleexception.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

// week 7 fileService
//ช้กำหนดค่าการตั้งค่าเกี่ยวกับไฟล์สำหรับแอปพลิเคชัน Spring Boot
@ConfigurationProperties(prefix = "file") //ใช้เพื่อแมปค่าคอนฟิกจาก application.properties หรือ application.yml
@Getter
@Setter
public class FileStorageProperties {
    private String uploadDir; //โฟลเดอร์ที่ใช้เก็บไฟล์อัปโหลด
    private String[] supportFileTypes; //ประเภทของไฟล์ที่รองรับ เช่น .jpg, .png, .pdf
}
