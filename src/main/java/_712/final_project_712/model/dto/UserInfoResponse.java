package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "用户信息响应")
public class UserInfoResponse {
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "用户名")
    private String name;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "地址列表")
    private List<AddressInfo> addresses;
    
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
        
        @Schema(description = "是否默认地址")
        private Boolean isDefault;
    }
} 