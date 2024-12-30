package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新用户信息请求")
public class UpdateProfileRequest {
    
    @Schema(description = "邮箱", example = "newemail@example.com")
    private String email;
    
    @Schema(description = "地址信息")
    private AddressInfo address;
    
    @Data
    @Schema(description = "地址信息")
    public static class AddressInfo {
        @Schema(description = "收货人姓名", example = "张三", required = true)
        private String receiverName;
        
        @Schema(description = "收货人电话", example = "13800138000", required = true)
        private String receiverPhone;
        
        @Schema(description = "省份", example = "广东省", required = true)
        private String province;
        
        @Schema(description = "城市", example = "深圳市", required = true)
        private String city;
        
        @Schema(description = "区/县", example = "南山区", required = true)
        private String district;
        
        @Schema(description = "详细地址", example = "科技园南区1号楼", required = true)
        private String detailAddress;
        
        @Schema(description = "是否设为默认地址", example = "true")
        private Boolean isDefault;
    }
} 