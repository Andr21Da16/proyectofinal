package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.CampaniaRequest;
import com.proyecto.coolboxtienda.dto.response.CampaniaResponse;

import java.util.List;

public interface CampaniaMarketingService {
    List<CampaniaResponse> getAllCampanias();

    List<CampaniaResponse> getCampaniasActivas();

    CampaniaResponse getCampaniaById(Integer id);

    CampaniaResponse createCampania(CampaniaRequest request);

    CampaniaResponse updateCampania(Integer id, CampaniaRequest request);

    void deleteCampania(Integer id);

    List<CampaniaResponse> getCampaniasByProducto(Integer idProducto);
}
