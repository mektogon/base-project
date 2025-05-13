package ru.dorofeev.application.pagination.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Модель с параметрами пагинации")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    @Range(min = 0, message = "Индекс не может быть отрицательным!")
    @Schema(description = "Количество элементов на странице")
    private Integer pageIndex;

    @Range(min = 1, max = 1000, message = "Количество элементов на странице должно быть в диапазоне [1; 1000]")
    @Schema(description = "Количество элементов на странице")
    private Integer pageSize;
}
