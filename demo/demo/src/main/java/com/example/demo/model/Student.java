package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Student {

    private String hoTen;
    private String maSinhVien;
    private String lop;
    private String nganhHoc;
    private String khoaHoc;

}