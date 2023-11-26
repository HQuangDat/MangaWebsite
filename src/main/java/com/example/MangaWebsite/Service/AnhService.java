package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Anh;
import com.example.MangaWebsite.Repository.IAnhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnhService {
    @Autowired
    private IAnhRepository anhRepository;


    public void addAnh(Anh anh) {
        anhRepository.save(anh);
    }
    public void updateAnh(Anh anh) {
        anhRepository.save(anh);
    }

    public void deleteAnh(Long anhId) {
        // Kiểm tra xem ảnh có tồn tại trong cơ sở dữ liệu không
        if (anhRepository.existsById(anhId)) {
            anhRepository.deleteById(anhId);
        } else {
            // Nếu ảnh không tồn tại, bạn có thể xử lý theo ý của bạn, ví dụ như ném một ngoại lệ.
            throw new RuntimeException("Ảnh không tồn tại.");
        }
    }

    public List<Anh> getAnhsByChuongId(Long chuongId) {
        return anhRepository.findByChuongId(chuongId);
    }
}
