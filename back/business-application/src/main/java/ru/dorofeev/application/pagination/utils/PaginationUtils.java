package ru.dorofeev.application.pagination.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Immutable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.dorofeev.application.pagination.model.Pagination;
import ru.dorofeev.application.pagination.model.SortField;

import java.util.Objects;

/**
 * Вспомогательный класс для работы с параметрами пагинации и сортировки.
 */
@Immutable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationUtils {

    /**
     * Метод для получения информации о пагинации.
     * В зависимости от наличия параметра {@link SortField} создается объект {@link Pageable} с сортировкой или без.
     *
     * @param pagination информация о текущей странице и количестве элементов на ней.
     * @param sortField  информация о порядке сортировки.
     * @return объект типа {@link Pageable}.
     */
    public static Pageable getPageable(Pagination pagination, SortField sortField) {
        if (Objects.nonNull(sortField) && StringUtils.isNotBlank(sortField.getField())) {
            return PageRequest.of(
                    pagination.getPageIndex(),
                    pagination.getPageSize(),
                    Sort.by(sortField.getDirection(), sortField.getField())
            );
        } else {
            return PageRequest.of(pagination.getPageIndex(), pagination.getPageSize());
        }
    }
}
