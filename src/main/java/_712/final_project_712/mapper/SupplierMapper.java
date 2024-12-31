package _712.final_project_712.mapper;

import _712.final_project_712.model.Supplier;
import _712.final_project_712.model.dto.SupplierDTO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    
    @Select("SELECT s.*, " +
            "GROUP_CONCAT(DISTINCT CONCAT_WS(',', sp.product_id, p.name, sp.stock, sp.price, sp.create_time, sp.update_time) " +
            "SEPARATOR ';') as product_info " +
            "FROM supplier s " +
            "LEFT JOIN supplier_product sp ON s.id = sp.supplier_id " +
            "LEFT JOIN product p ON sp.product_id = p.id " +
            "WHERE s.id = #{supplierId} " +
            "GROUP BY s.id")
    SupplierDTO.SupplierDetailResponse getSupplierDetail(Long supplierId);
} 