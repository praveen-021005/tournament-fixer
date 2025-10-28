package com.example.matchfixer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*")
@RestController

public class MatchController {

    @GetMapping("/api/test")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    public String testConnection()
    {
        return "Backend is running successfully!";
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final Path dataFile = Path.of("tournaments.json");

    @PostMapping("/submit-tournament")
    public ResponseEntity<?> handleTournament(@RequestBody Map<String, Object> data) {
        try {
            List<Map<String, Object>> list;
            if (Files.exists(dataFile)) {
                list = mapper.readValue(dataFile.toFile(), new TypeReference<List<Map<String, Object>>>(){});
            } else {
                list = new ArrayList<>();
            }
            list.add(data);
            mapper.writerWithDefaultPrettyPrinter().writeValue(dataFile.toFile(), list);

            System.out.println("Saved tournament entry. Total saved: " + list.size());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Tournament saved to tournaments.json",
                    "entries", list.size()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
