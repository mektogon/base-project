package ru.dorofeev.application.pagination.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Schema(description = "Модель с детальными параметрами пагинации")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDetail extends Pagination {

    @Schema(description = "Признак следующей страницы")
    private Boolean hasNextPage;

    @Schema(description = "Общее количество элементов")
    private Long totalElements;

    @Schema(description = "Общее количество страниц")
    private Integer totalPages;

    /**
     * Формирование {@link PaginationDetail} в зависимости от параметров объекта {@link Page}.
     *
     * @param page параметры страницы.
     * @param <T>  объект обернутый в {@link Page}.
     * @return объект типа {@link PaginationDetail}.
     */
    public static <T> PaginationDetail build(Page<T> page) {
        return PaginationDetail.builder()
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .hasNextPage(page.hasNext())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    /**
     * Формирование {@link PaginationDetail} в зависимости от параметров объекта {@link Page} и параметра hasNextPage.
     *
     * @param page параметры страницы.
     * @param <T>  объект обернутый в {@link Page}.
     * @return объект типа {@link PaginationDetail}.
     */
    public static <T> PaginationDetail build(Page<T> page, boolean hasNextPage) {
        return PaginationDetail.builder()
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .hasNextPage(hasNextPage)
                .build();
    }

    /**
     * Формирование {@link PaginationDetail} в зависимости от параметров объекта {@link Slice}.
     *
     * @param slice параметры страницы.
     * @param <T>   объект обернутый в {@link Slice}.
     * @return объект типа {@link PaginationDetail}.
     */
    public static <T> PaginationDetail build(Slice<T> slice) {
        return PaginationDetail.builder()
                .pageIndex(slice.getNumber())
                .pageSize(slice.getSize())
                .hasNextPage(slice.hasNext())
                .build();
    }
}
