package net.yefangwong.csp.common.api;

import junit.framework.TestCase;
import java.util.Arrays;
import java.util.List;

public class PageResultTest extends TestCase {

    public void testPageResultOf() {
        List<String> items = Arrays.asList("item1", "item2", "item3");
        PageResult<String> page = PageResult.of(1, 10, 25L, items);

        assertEquals(1, page.getPageNum());
        assertEquals(10, page.getPageSize());
        assertEquals(25L, page.getTotal());
        assertEquals(3, page.getTotalPages());
        assertEquals(3, page.getList().size());
        assertEquals("item1", page.getList().get(0));
    }

    public void testPageResultEmpty() {
        PageResult<String> page = PageResult.empty(2, 20);

        assertEquals(2, page.getPageNum());
        assertEquals(20, page.getPageSize());
        assertEquals(0L, page.getTotal());
        assertEquals(0, page.getTotalPages());
        assertTrue(page.getList().isEmpty());
    }

    public void testPageResultBuilder() {
        List<Integer> numbers = Arrays.asList(100, 200);
        PageResult<Integer> page = PageResult.<Integer>builder()
                .pageNum(2)
                .pageSize(5)
                .total(12L)
                .list(numbers)
                .build();

        assertEquals(2, page.getPageNum());
        assertEquals(5, page.getPageSize());
        assertEquals(12L, page.getTotal());
        assertEquals(3, page.getTotalPages());
        assertEquals(2, page.getList().size());
    }

    public void testTotalPagesCalculation() {
        PageResult<String> exactPage = PageResult.of(1, 10, 30L, null);
        assertEquals(3, exactPage.getTotalPages());

        PageResult<String> partialPage = PageResult.of(1, 10, 31L, null);
        assertEquals(4, partialPage.getTotalPages());
    }
}
