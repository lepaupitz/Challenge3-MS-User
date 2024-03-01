package com.compassuol.sp.challenge.challenge3msuser.web.dto.mapper;

import com.compassuol.sp.challenge.challenge3msuser.web.dto.PageableDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class PageableMapper {

    public static PageableDto toDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}
