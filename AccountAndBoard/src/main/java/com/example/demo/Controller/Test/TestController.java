package com.example.demo.Controller.Test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("안녕하세요! 테스트 컨트롤러입니다.");
    }

    @GetMapping("/echo/{message}")
    public ResponseEntity<String> echo(@PathVariable String message) {
        return ResponseEntity.ok("받은 메시지: " + message);
    }

    @PostMapping("/data")
    public ResponseEntity<String> postData(@RequestBody String data) {
        return ResponseEntity.ok("전송된 데이터: " + data);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateData(@PathVariable Long id, @RequestBody String data) {
        return ResponseEntity.ok("ID " + id + "의 데이터가 업데이트되었습니다: " + data);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteData(@PathVariable Long id) {
        return ResponseEntity.ok("ID " + id + "의 데이터가 삭제되었습니다.");
    }
}
