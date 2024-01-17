package ua.goit.url.service.exceptions;

import ua.goit.url.request.CreateUrlRequest;

public class NotAccessibleException extends Exception{
    private static final String URL_IS_NOT_ACCESSIBLE_TEXT = "Url with url = %s is not accessible!";

    public NotAccessibleException(CreateUrlRequest url){
        super(String.format(URL_IS_NOT_ACCESSIBLE_TEXT, url.getUrl()));
    }
}
