package com.example.springOAuth.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagedResponse<T> {

    private List<T> data;
    private long totalElements;
    private long totalPages;
    private int size;
    private boolean first;
    private boolean last;

}
