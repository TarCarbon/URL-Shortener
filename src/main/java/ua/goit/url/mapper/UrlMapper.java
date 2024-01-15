package ua.goit.url.mapper;

import org.springframework.stereotype.Component;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.response.UrlResponse;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UrlMapper {
    public List<UrlEntity> toUrlEntities(Collection<UrlDto> urlDtos){
        return urlDtos.stream()
                .map(this::toUrlEntity)
                .collect(Collectors.toList());
    }

    public UrlEntity toUrlEntity(UrlDto dto) {
        UrlEntity entity = new UrlEntity();
        entity.setId(dto.getId());
        entity.setShortUrl(dto.getShortUrl());
        entity.setUrl(dto.getUrl());
        entity.setDescription(dto.getDescription());
        entity.setUser(dto.getUser());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setVisitCount(dto.getVisitCount());
        return entity;
    }

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
        dto.setUser(entity.getUser());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setVisitCount(entity.getVisitCount());
        return dto;
    }

    public UrlDto toUrlDto(Long id, UpdateUrlRequest request) {
        UrlDto dto = new UrlDto();
        dto.setId(id);
        dto.setShortUrl(request.getShortUrl());
        dto.setDescription(request.getDescription());
        dto.setVisitCount(0);
        return dto;
    }

}
