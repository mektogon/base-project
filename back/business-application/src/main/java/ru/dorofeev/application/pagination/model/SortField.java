package ru.dorofeev.application.pagination.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@Schema(description = "Модель с параметрами сортировки")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SortField {

    @NotBlank(message = "Поле сортировки не может быть пустым!")
    @Schema(description = "Поле сортировки")
    private String field;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Направление сортировки", defaultValue = "ASC")
    private Sort.Direction direction = Sort.Direction.ASC;

    /**
     * Формирование объекта типа {@link SortField} в зависимости от параметров объекта {@link Slice}.
     *
     * @param slice параметры страницы.
     * @param <T>   объект обернутый в {@link Slice}.
     * @return объект типа {@link SortField}.
     */
    public static <T> SortField build(Slice<T> slice) {
        return slice.getSort().get().findFirst()
                .map(order -> SortField.builder()
                        .field(order.getProperty())
                        .direction(order.getDirection())
                        .build()
                ).orElse(null);
    }
}
