package com.example.quanlytour.repository;

import com.example.quanlytour.dto.LichTrinhSearchRequest;
import com.example.quanlytour.entity.LichTrinh;
import java.util.List;

public interface LichTrinhRepositoryCustom {

    List<LichTrinh> search(LichTrinhSearchRequest request);

}