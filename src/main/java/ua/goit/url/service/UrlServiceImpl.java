package ua.goit.url.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;

import java.util.List;

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
    public void deleteById(String id) {

    }

    @Override
    public void update(UrlDto url) {

    }
}
