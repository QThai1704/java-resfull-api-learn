package vn.hoidanit.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter
    public static class Meta {
        // Trang hiện tại
        private int page;
        // Số phần tử trên mỗi trang
        private int pageSize;
        // Tổng số trang
        private int pages;
        // Tổng số phần tử
        private long total;
    }
}
