


package com.example.MangaWebsite.Model;
import jakarta.persistence.*;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "anh")
public class Anh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuong_id", referencedColumnName = "id", nullable = false)
    private Chuong chuong;

    // Thêm các trường dữ liệu khác của ảnh
    @Column(name = "duong_dan")
    private String duongDan;
    public Anh() {
    }

    public Anh(Chuong chuong, String duongDan) {
        this.chuong = chuong;
        this.duongDan = duongDan;
    }

}
