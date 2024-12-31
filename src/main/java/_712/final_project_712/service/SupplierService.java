package _712.final_project_712.service;

import _712.final_project_712.model.Supplier;
import _712.final_project_712.model.dto.SupplierDTO;
import java.util.List;

public interface SupplierService {
    /**
     * 添加供应商
     */
    void addSupplier(SupplierDTO.AddSupplierRequest request);

    /**
     * 更新供应商信息
     */
    void updateSupplier(Long supplierId, SupplierDTO.UpdateSupplierRequest request);

    /**
     * 获取供应商列表
     */
    List<Supplier> getSupplierList();

    /**
     * 获取供应商详情
     */
    Supplier getSupplierById(Long supplierId);

    /**
     * 获取供应商详情（包含商品信息）
     */
    SupplierDTO.SupplierDetailResponse getSupplierDetail(Long supplierId);

    /**
     * 启用/禁用供应商
     */
    void updateSupplierStatus(Long supplierId, Integer status);

    /**
     * 添加供应商商品
     */
    void addSupplierProduct(Long supplierId, SupplierDTO.AddSupplierProductRequest request);
} 