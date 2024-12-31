package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

public class SupplierDTO {

    @Data
    @Schema(description = "添加供应商请求")
    public static class AddSupplierRequest {
        @Schema(description = "供应商名称", example = "苹果中国", required = true)
        private String name;

        @Schema(description = "联系人", example = "张三", required = true)
        private String contact;

        @Schema(description = "联系电话", example = "13800138000", required = true)
        private String phone;

        @Schema(description = "电子邮箱", example = "contact@apple.com.cn")
        private String email;

        @Schema(description = "地址", example = "北京市朝阳区xx路xx号")
        private String address;

        @Schema(description = "供应商描述")
        private String description;
    }

    @Data
    @Schema(description = "更新供应商请求")
    public static class UpdateSupplierRequest {
        @Schema(description = "供应商名称", example = "苹果中国")
        private String name;

        @Schema(description = "联系人", example = "张三")
        private String contact;

        @Schema(description = "联系电话", example = "13800138000")
        private String phone;

        @Schema(description = "电子邮箱", example = "contact@apple.com.cn")
        private String email;

        @Schema(description = "地址", example = "北京市朝阳区xx路xx号")
        private String address;

        @Schema(description = "供应商描述")
        private String description;

        @Schema(description = "状态：0-禁用，1-启用", example = "1")
        private Integer status;
    }

    @Data
    @Schema(description = "添加供应商商品请求")
    public static class AddSupplierProductRequest {
        @Schema(description = "商品ID", required = true)
        private Long productId;

        @Schema(description = "库存数量", required = true)
        private Integer stock;

        @Schema(description = "供应价格", required = true)
        private BigDecimal price;
    }
} 