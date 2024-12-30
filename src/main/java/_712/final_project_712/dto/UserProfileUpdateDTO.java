package _712.final_project_712.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息更新请求")
public class UserProfileUpdateDTO {
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "地址信息")
    private AddressDTO address;
    
    @Data
    public static class AddressDTO {
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
} 