package com.example.oskuda.service;

import com.example.oskuda.entity.Department;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oskar Krishna Shrestha
 * date: 3/1/2023
 * package: com.example.oskuda.service
 */
public interface DepartmentService {
    public Department saveDepartment(Department department);

    public List<Department> fetchDepartmentList();

    public Department fetchDepartmentById(Long departmentId);

    public void deleteDepartmentById(Long departmentId);

    public Department updateDepartment(Long departmentId, Department department);

    public Department fetchDepartmentByName(String departmentName);
}
