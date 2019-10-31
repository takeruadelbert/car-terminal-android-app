package com.stn.carterminal.network.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeUhfTag {
    private Long id;
    private String uhfTag;
}
