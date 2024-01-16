package ua.goit.url.mapper;

import org.springframework.stereotype.Component;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.response.UrlResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UrlMapper {

    public List<UrlDto> toUrlDtos(Collection<UrlEntity> entities){
        return entities.stream()
                .map(this::toUrlDto)
                .collect(Collectors.toList());
    }

    public UrlDto toUrlDto(UrlEntity entity) {
        UrlDto dto = new UrlDto();
        dto.setId(entity.getId());
        dto.setShortUrl(entity.getShortUrl());
        dto.setUrl(entity.getUrl());
        dto.setDescription(entity.getDescription());
        dto.setUsername(entity.getUser().getUsername());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setVisitCount(entity.getVisitCount());
        return dto;
    }

    public UrlEntity toUrlEntity(UrlDto dto) {
        UrlEntity entity = new UrlEntity();
        entity.setId(dto.getId());
        entity.setUrl(dto.getUrl());
        entity.setShortUrl(dto.getShortUrl());
        entity.setDescription(dto.getDescription());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setVisitCount(dto.getVisitCount());
        return entity;
    }

    public UrlDto toUrlDto(CreateUrlRequest request) {
        UrlDto dto = new UrlDto();
        dto.setUrl(request.getUrl());
        dto.setShortUrl(request.getShortUrl());
        dto.setDescription(request.getDescription());
        return dto;
    }

    public UrlResponse toUrlResponse(UrlDto dto) {
        UrlResponse response = new UrlResponse();
        response.setId(dto.getId());
        response.setUrl(dto.getUrl());
        response.setShortUrl(dto.getShortUrl());
        response.setDescription(dto.getDescription());
        response.setCreatedDate(dto.getCreatedDate());
        response.setExpirationDate(dto.getExpirationDate());
        response.setVisitCount(dto.getVisitCount());
        response.setUsername(dto.getUsername());
        return response;
    }

    public List<UrlResponse> toUrlResponses(Collection<UrlDto> dtos) {
        return dtos.stream()
                .map(this::toUrlResponse)
                .collect(Collectors.toList());
    }
}
