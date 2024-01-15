package ua.goit.url.mapper;

import org.springframework.stereotype.Component;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;

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
}
