package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Schema(description = "用户相关DTO")
public class UserDTO {

    @Data
    @Schema(description = "更新用户信息请求")
    public static class UpdateUserRequest {
        @Schema(description = "用户名")
        private String name;
        
        @Schema(description = "邮箱")
        private String email;
    }

    @Data
    @Schema(description = "新增地址请求")
    public static class AddAddressRequest {
        @Schema(description = "收货人姓名", required = true)
        private String receiverName;
        
        @Schema(description = "收货人电话", required = true)
        private String receiverPhone;
        
        @Schema(description = "省份", required = true)
        private String province;
        
        @Schema(description = "城市", required = true)
        private String city;
        
        @Schema(description = "区/县", required = true)
        private String district;
        
        @Schema(description = "详细地址", required = true)
        private String detailAddress;
        
        @Schema(description = "是否设为默认地址")
        private Boolean isDefault;
    }

    @Data
    @Schema(description = "更新地址请求")
    public static class UpdateAddressRequest {
        @Schema(description = "收货人姓名")
        private String receiverName;
        
        @Schema(description = "收货人电话")
        private String receiverPhone;
        
        @Schema(description = "省份")
        private String province;
        
        @Schema(description = "城市")
        private String city;
        
        @Schema(description = "区/县")
        private String district;
        
        @Schema(description = "详细地址")
        private String detailAddress;
        
        @Schema(description = "是否设为默认地址")
        private Boolean isDefault;
    }

    @Data
    @Schema(description = "用户信息响应")
    public static class UserInfoResponse {
        @Schema(description = "用户ID")
        private Long id;
        
        @Schema(description = "用户名")
        private String name;
        
        @Schema(description = "邮箱")
        private String email;
        
        @Schema(description = "收货地址列表")
        private List<AddressInfo> addresses;
    }

    @Data
    @Schema(description = "地址信息")
    public static class AddressInfo {
        @Schema(description = "地址ID")
        private Long id;
        
        @Schema(description = "收货人姓名")
        private String receiverName;
        
        @Schema(description = "收货人电话")
        private String receiverPhone;
        
        @Schema(description = "省份")
        private String province;
        
        @Schema(description = "城市")
        private String city;
        
        @Schema(description = "区/县")
        private String district;
        
        @Schema(description = "详细地址")
        private String detailAddress;
        
        @Schema(description = "是否为默认地址")
        private Boolean isDefault;
    }
} 