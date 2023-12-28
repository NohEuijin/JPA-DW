package com.example.dw.repository.community;

import com.example.dw.domain.dto.community.WalkMateDetailDto;
import com.example.dw.domain.dto.community.WalkMateListDto;
import com.example.dw.domain.form.SearchForm;
import com.example.dw.domain.form.SearchLocationForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WalkingMateRepositoryCustom {

    Page<WalkMateListDto> findAllWalkMate(Pageable pageable, SearchLocationForm searchLocationForm);

    Page<WalkMateListDto> findAllWalkMate(Pageable pageable, SearchForm searchForm);


    Optional<WalkMateDetailDto> walkMateDetail(Long walkBoardId);

}