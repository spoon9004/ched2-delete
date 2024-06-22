package org.example.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UuidReaderService {

    public List<String> readUuids(String uuidFilePath) throws IOException {
        return Files.readAllLines(Paths.get(uuidFilePath));
    }
}
