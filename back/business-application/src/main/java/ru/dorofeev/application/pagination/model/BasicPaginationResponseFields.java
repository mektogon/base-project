package ru.dorofeev.application.pagination.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Модель ответа с параметрами пагинации и сортировки")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicPaginationResponseFields {

    @Valid
    @Schema(description = "Информация о параметрах пагинации")
    private PaginationDetail pagination;

    @Valid
    @Schema(description = "Информация о параметрах сортировки")
    private SortField sortField;
}
