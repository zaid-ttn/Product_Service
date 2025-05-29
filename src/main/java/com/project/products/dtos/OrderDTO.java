package com.project.products.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class OrderDTO {

    private Long id;
    private int quantity;
}
