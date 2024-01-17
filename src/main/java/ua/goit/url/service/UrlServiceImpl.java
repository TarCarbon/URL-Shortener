package ua.goit.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.goit.url.UrlEntity;
import ua.goit.url.repository.UrlRepository;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.EditUrlRequest;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService{
    private final UrlMapper urlMapper;
    private final UrlRepository urlRepository;

    @Override
    public List<UrlDto> listAll() {
        return urlMapper.toUrlDtos(urlRepository.findAll());
    }

    @Override
    @Transactional
    public UrlDto createUrl(UrlDto dto) {
        UrlEntity entity = urlMapper.toUrlEntity(dto);
        entity.setId(null);
        return urlMapper.toUrlDto(urlRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        return;
    }

    @Override
    public void edit(Long id, EditUrlRequest url) {

    }

    @Override
    public UrlDto getById(Long id) {
        return null;
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
