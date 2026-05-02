package com.apiintegration.hngstage1profileaggregator.dtos.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationApiResponse {
    private String status;
    private long total;
    private int limit;
    private int page;
    private int total_pages;
    private Map<String, String> links;
    private List<Summary> data;

    public SpecificationApiResponse(Page<Summary> data) {
        this.total = data.getTotalElements();
        this.data = data.getContent();
        this.status = "success";
        this.limit = data.getSize();
        this.page = data.getNumber() + 1;
        this.total_pages = data.getTotalPages();

        String base = "/api/profiles?page=" + this.page + "&limit=" + this.limit;
        String next = data.hasNext() ? "/api/profiles?page=" + (this.page + 1) + "&limit=" + this.limit : null;
        String prev = data.hasPrevious() ? "/api/profiles?page=" + (this.page - 1) + "&limit=" + this.limit : null;

        this.links = new java.util.HashMap<>();
        this.links.put("self", base);
        this.links.put("next", next);
        this.links.put("prev", prev);
    }
}