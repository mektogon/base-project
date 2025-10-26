package ru.dorofeev.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.dorofeev.application.pagination.model.Pagination;
import ru.dorofeev.application.pagination.model.SortField;
import ru.dorofeev.application.pagination.utils.PaginationUtils;

class PaginationUtilsTest extends BaseUnitTest {

    private static final Integer EXPECTED_PAGE_INDEX = 0;
    private static final Integer EXPECTED_PAGE_SIZE = 10;
    private static final String EXPECTED_SORT_FIELD_NAME = "someField";
    private static final SortField EMPTY_SORT_FIELD = null;
    private static final Pagination DEFAULT_PAGINATION_REQUEST = Pagination.builder()
            .pageIndex(EXPECTED_PAGE_INDEX)
            .pageSize(EXPECTED_PAGE_SIZE)
            .build();
    private static final SortField NOT_EMPTY_SORT_FIELD = SortField.builder()
            .field(EXPECTED_SORT_FIELD_NAME)
            .direction(Sort.Direction.ASC)
            .build();

    @DisplayName("[UNIT-тест] Должен сформировать Pageable с объектом Sort")
    @Test
    void getPageable_shouldReturnPageableWithSort() {
        Pageable pageable = PaginationUtils.getPageable(
                DEFAULT_PAGINATION_REQUEST,
                NOT_EMPTY_SORT_FIELD
        );

        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(EXPECTED_PAGE_SIZE, pageable.getPageSize());
        Assertions.assertEquals(EXPECTED_PAGE_INDEX, pageable.getPageNumber());

        Sort currentSort = pageable.getSort();
        Assertions.assertNotNull(currentSort);

        Sort.Order currentOrder = currentSort.get().findFirst().get();
        Assertions.assertNotNull(currentOrder.getDirection());
        Assertions.assertEquals(EXPECTED_SORT_FIELD_NAME, currentOrder.getProperty());
    }

    @DisplayName("[UNIT-тест] Должен сформировать Pageable assertEquals Sort")
    @Test
    void getPageable_shouldReturnPageableWithoutSort() {
        Pageable pageable = PaginationUtils.getPageable(
                DEFAULT_PAGINATION_REQUEST,
                EMPTY_SORT_FIELD
        );

        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(EXPECTED_PAGE_SIZE, pageable.getPageSize());
        Assertions.assertEquals(EXPECTED_PAGE_INDEX, pageable.getPageNumber());
        Assertions.assertEquals(Sort.unsorted(), pageable.getSort());
    }
}