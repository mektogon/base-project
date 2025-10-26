package ru.dorofeev.application.pagination.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Модель запроса с параметрами пагинации и сортировки")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicPaginationFilterFields {

    @Valid
    @Schema(description = "Параметры пагинации")
    private Pagination pagination;

    @Valid
    @Schema(description = "Параметры сортировки")
    private SortField sortField;
}
