package com.college.placement.controller;

import com.college.placement.model.*;
import com.college.placement.dto.*;
import com.college.placement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // GET all students
    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Debug Error: " + e.getMessage()));
        }
    }
    
    // GET student by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id)
                .map(student -> ResponseEntity.ok(
                        ApiResponse.success("Student found", student)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found")));
    }
    
    // GET students by department
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByDepartment(
            @PathVariable String department) {
        List<Student> students = studentService.getStudentsByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success("Students retrieved", students));
    }
    
    // GET students by batch year
    @GetMapping("/batch/{year}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByBatchYear(
            @PathVariable Integer year) {
        List<Student> students = studentService.getStudentsByBatchYear(year);
        return ResponseEntity.ok(ApiResponse.success("Students retrieved", students));
    }
    
    // POST create new student
    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(
            @Valid @RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", createdStudent));
    }
    
    // PUT update student
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudent(
            @PathVariable String id, @Valid @RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudent(id, student);
            return ResponseEntity.ok(
                    ApiResponse.success("Student updated successfully", updatedStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // DELETE student
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }
}

