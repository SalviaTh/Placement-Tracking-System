package com.college.placement.service;

import com.college.placement.model.*;
import com.college.placement.dto.*;
import com.college.placement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(String studentId) {
        return studentRepository.findById(studentId);
    }
    
    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }
    
    public List<Student> getStudentsByBatchYear(Integer batchYear) {
        return studentRepository.findByBatchYear(batchYear);
    }
    
    public List<Student> getStudentsByDepartmentAndYear(String department, Integer batchYear) {
        return studentRepository.findByDepartmentAndBatchYear(department, batchYear);
    }
    
    public Student createStudent(Student student) {
        student.setPlacementStatus(Student.PlacementStatus.NOT_PLACED);
        return studentRepository.save(student);
    }
    
    public Student updateStudent(String studentId, Student studentDetails) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setName(studentDetails.getName());
        student.setDepartment(studentDetails.getDepartment());
        student.setBatchYear(studentDetails.getBatchYear());
        
        return studentRepository.save(student);
    }
    
    public void deleteStudent(String studentId) {
        studentRepository.deleteById(studentId);
    }
}