package ua.goit.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.response.UrlResponse;
import ua.goit.url.service.UrlService;

import java.math.BigDecimal;
import java.util.List;

@Validated
@Controller
@RequestMapping("/V1/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlMapper urlMapper;

    @GetMapping("/list")
    public ResponseEntity<List<UrlResponse>> urlList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(urlMapper.toUrlResponses(urlService.listAll()));
    }

    @PostMapping("/create")
    public ResponseEntity<UrlResponse> createUrl(
            @RequestBody CreateUrlRequest request) {
        UrlDto dto = urlMapper.toUrlDto(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(urlMapper.toUrlResponse(urlService.add(dto)));
    }

    @PutMapping("/{id}}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUrl(
            @PathVariable("id") BigDecimal id,
            @RequestBody UpdateUrlRequest request
            ) {
        UrlDto dto = urlMapper.toUrlDto(id, request);
        urlService.update(dto);
    }

    @DeleteMapping("/delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public void deleteUrlById(@PathVariable("id") BigDecimal id){
        urlService.deleteById(id);
    }
}
