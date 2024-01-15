package ua.goit.url.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.UpdateUrlRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService{

    @Autowired
    private UrlMapper urlMapper;
    @Autowired
    private UrlRepository urlRepository;

    @Override
    public List<UrlDto> listAll() {
        return urlMapper.toUrlDtos(urlRepository.findAll());
    }

    @Override
    public UrlDto add(UrlDto url) {
        return null;
    }

    @Override
    public void deleteById(BigDecimal id) {
        getById(id);
        urlRepository.deleteById(String.valueOf(id));
    }

    @Override
    public void update(Long id, UpdateUrlRequest url) {
        if(Objects.isNull(url.getShortUrl())){
            throw new RuntimeException("Not found");
        }
        getById(url.getId());
        urlRepository.save(urlMapper.toUrlEntity(url));
    }

    @Override
    public UrlDto getById(Long id) {
        Optional<UrlEntity> optionalUrl = urlRepository.findById(String.valueOf(id));
        if(optionalUrl.isPresent()) {
            return urlMapper.toUrlDto(optionalUrl.get());
        } else {
            throw new RuntimeException("Url is not present");
        }
    }
}
