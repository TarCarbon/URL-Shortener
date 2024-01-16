package ua.goit.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.goit.url.UrlEntity;
import ua.goit.url.repository.UrlRepository;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.user.UserEntity;
import ua.goit.user.dto.UserDto;
import ua.goit.user.mapper.UserMapper;
import ua.goit.user.repository.UserRepository;
import ua.goit.user.service.UserService;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService{
    private final UrlMapper urlMapper;

    private final UrlRepository urlRepository;



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
    public void update(Long id, UpdateUrlRequest request) {
        if(Objects.isNull(request.getShortUrl())){
            throw new RuntimeException("Not found");
        }
        if (!isLinkUnique(request.getShortUrl())){
            throw new RuntimeException("this short link is present");
        }

        UrlDto dto = getById(id);
        dto.setUrl(request.getUrl());
        dto.setShortUrl(request.getShortUrl());
        dto.setDescription(request.getDescription());
        UrlEntity urlEntity = urlMapper.toUrlEntity(dto);

        urlRepository.save(urlEntity);
    }

    @Override
    public UrlDto getById(Long id) {
        return urlMapper.toUrlDto(urlRepository.getReferenceById(id));
    }


    public boolean isLinkUnique(String link) {
        return listAll().stream().noneMatch(urlEntity -> urlEntity.getShortUrl().equals(link));
    }

    //before passing to the method, check and if necessary add to the originalUrl https://
    private boolean isUrlAccessible(String originalUrl) {
        int responseCode;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(originalUrl).openConnection();
            connection.setRequestMethod("HEAD");
            responseCode = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException e) {
            responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
        }
        return responseCode >= 200 && responseCode < 300;
    }
}
