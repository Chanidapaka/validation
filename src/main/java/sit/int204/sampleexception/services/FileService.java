package sit.int204.sampleexception.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.sampleexception.utils.FileStorageProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

//week 7
@Service
@Getter
public class FileService {
    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            if (!Files.exists(this.fileStorageLocation)) {
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Can’t create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String store(MultipartFile file) {
        if (!isSupportedContentType(file)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST
                    , "Does not support content type: " + file.getContentType());
        }
//        Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
//            Check if the file 's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
//            Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File operation error: "
                    + fileName, ex);
        }
    }

    public String getFileType(Resource resource) {
        try {
            String type = Files.probeContentType(resource.getFile().toPath());
            return type == null ? "application/text" : type;
        } catch (IOException ex) {
            throw new RuntimeException("ProbeContentType error: " + resource, ex);
        }
    }

    public void removeFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new ResourceNotFoundException("File not found " + fileName);
            }
        } catch (IOException ex) {
            throw new RuntimeException("File operation (DELETE) error: " + fileName, ex);
        }
    }

    private boolean isSupportedContentType(MultipartFile file) {
        String contentType = file.getContentType();
        List<String> supportFileTypes = Arrays.stream(fileStorageProperties.getSupportFileTypes()).toList();
        return supportFileTypes.contains(contentType);
    }

    public List<String> store(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>(files.size());
        files.forEach(file -> fileNames.add(store(file)));
        return fileNames;
    }

    public List<String> getMatchedFiles(String pattern) {
        List<String> matchesList = new ArrayList<String>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs)
                    throws IOException {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
                Path name = file.getFileName();
                if (matcher.matches(name)) {
                    matchesList.add(name.toString());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(this.fileStorageLocation, matcherVisitor);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return matchesList;
    }

}