package com.pmmp.model.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProcessSatFileResource {
    private Integer processedRecords;
}
