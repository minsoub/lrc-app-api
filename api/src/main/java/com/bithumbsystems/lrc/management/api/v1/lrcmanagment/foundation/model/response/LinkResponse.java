package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkResponse {

    private String projectId;
    private String projectName;
    private String symbol;
}
