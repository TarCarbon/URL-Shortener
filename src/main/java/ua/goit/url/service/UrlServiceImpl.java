package ua.goit.url.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;

import java.util.List;


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
    public UrlDto add(CreateUrlRequest url) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        return;
    }

    @Override
    public void update(Long id, UpdateUrlRequest url) {

    }

    @Override
    public UrlDto getById(Long id) {
        return null;
    }


}
