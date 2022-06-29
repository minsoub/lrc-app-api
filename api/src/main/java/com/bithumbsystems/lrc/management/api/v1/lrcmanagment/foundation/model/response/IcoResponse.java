package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IcoResponse {

    private String marketInfo;
    private LocalDateTime icoDate;
}
