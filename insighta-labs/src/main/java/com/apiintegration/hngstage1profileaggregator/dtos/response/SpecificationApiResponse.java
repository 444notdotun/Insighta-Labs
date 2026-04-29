package com.apiintegration.hngstage1profileaggregator.dtos.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationApiResponse  {
    private String status;
    private long total;
    private int limit;
    private int page;
    private List<Summary> data;

    public SpecificationApiResponse( Page<Summary> data) {
        this.total=data.getTotalElements();
        this.data=data.getContent();
        this.status= "success";
        this.limit=data.getSize();
        this.page=data.getNumber()+1;
    }

}
