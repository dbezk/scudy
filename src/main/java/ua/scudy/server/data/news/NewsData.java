package ua.scudy.server.data.news;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewsData {

    @NotNull
    private String title;

    @NotNull
    private String text;

}
