package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.SupplierMapper;
import _712.final_project_712.mapper.SupplierProductMapper;
import _712.final_project_712.model.Supplier;
import _712.final_project_712.model.SupplierProduct;
import _712.final_project_712.model.dto.SupplierDTO;
import _712.final_project_712.service.SupplierService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private SupplierProductMapper supplierProductMapper;

    private Long generateId() {
        return System.currentTimeMillis();
    }

    @Override
    @Transactional
    public void addSupplier(SupplierDTO.AddSupplierRequest request) {
        // 验证必填字段
        if (!StringUtils.hasText(request.getName())) {
            throw new RuntimeException("供应商名称不能为空");
        }
        if (!StringUtils.hasText(request.getContact())) {
            throw new RuntimeException("联系人不能为空");
        }
        if (!StringUtils.hasText(request.getPhone())) {
            throw new RuntimeException("联系电话不能为空");
        }

        // 检查供应商名称是否已存在
        Supplier existingSupplier = QueryChain.of(Supplier.class)
                .where(Supplier::getName).eq(request.getName())
                .one();
        if (existingSupplier != null) {
            throw new RuntimeException("供应商名称已存在");
        }

        // 创建新供应商
        Supplier supplier = new Supplier();
        supplier.setId(generateId()); // 手动生成ID
        supplier.setName(request.getName());
        supplier.setContact(request.getContact());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setDescription(request.getDescription());
        supplier.setStatus(1); // 默认启用
        supplier.setCreateTime(LocalDateTime.now());
        supplier.setUpdateTime(LocalDateTime.now());

        supplierMapper.insert(supplier);
    }

    @Override
    @Transactional
    public void updateSupplier(Long supplierId, SupplierDTO.UpdateSupplierRequest request) {
        // 检查供应商是否存在
        Supplier supplier = supplierMapper.selectOneById(supplierId);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }

        // 如果要更新名称，检查是否与其他供应商重复
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(supplier.getName())) {
            Supplier existingSupplier = QueryChain.of(Supplier.class)
                    .where(Supplier::getName).eq(request.getName())
                    .and(Supplier::getId).ne(supplierId)
                    .one();
            if (existingSupplier != null) {
                throw new RuntimeException("供应商名称已存在");
            }
            supplier.setName(request.getName());
        }

        // 更新其他字段
        if (StringUtils.hasText(request.getContact())) {
            supplier.setContact(request.getContact());
        }
        if (StringUtils.hasText(request.getPhone())) {
            supplier.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getEmail())) {
            supplier.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getAddress())) {
            supplier.setAddress(request.getAddress());
        }
        if (StringUtils.hasText(request.getDescription())) {
            supplier.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            supplier.setStatus(request.getStatus());
        }

        supplier.setUpdateTime(LocalDateTime.now());
        supplierMapper.update(supplier);
    }

    @Override
    public List<Supplier> getSupplierList() {
        return QueryChain.of(Supplier.class).list();
    }

    @Override
    public Supplier getSupplierById(Long supplierId) {
        return supplierMapper.selectOneById(supplierId);
    }

    @Override
    public SupplierDTO.SupplierDetailResponse getSupplierDetail(Long supplierId) {
        return supplierMapper.getSupplierDetail(supplierId);
    }

    @Override
    @Transactional
    public void updateSupplierStatus(Long supplierId, Integer status) {
        // 检查供应商是否存在
        Supplier supplier = supplierMapper.selectOneById(supplierId);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }

        // 检查状态值是否有效
        if (status != 0 && status != 1) {
            throw new RuntimeException("无效的状态值");
        }

        // 更新状态
        supplier.setStatus(status);
        supplier.setUpdateTime(LocalDateTime.now());
        supplierMapper.update(supplier);
    }

    @Override
    @Transactional
    public void addSupplierProduct(Long supplierId, SupplierDTO.AddSupplierProductRequest request) {
        // 检查供应商是否存在且启用
        Supplier supplier = supplierMapper.selectOneById(supplierId);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        if (supplier.getStatus() != 1) {
            throw new RuntimeException("供应商已禁用");
        }

        // 检查商品是否已关联
        SupplierProduct existingProduct = QueryChain.of(SupplierProduct.class)
                .where(SupplierProduct::getSupplierId).eq(supplierId)
                .and(SupplierProduct::getProductId).eq(request.getProductId())
                .one();
        if (existingProduct != null) {
            throw new RuntimeException("该商品已关联到此供应商");
        }

        // 创建供应商商品关联
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setId(generateId()); // 手动生成ID
        supplierProduct.setSupplierId(supplierId);
        supplierProduct.setProductId(request.getProductId());
        supplierProduct.setStock(request.getStock());
        supplierProduct.setPrice(request.getPrice());
        supplierProduct.setCreateTime(LocalDateTime.now());
        supplierProduct.setUpdateTime(LocalDateTime.now());

        supplierProductMapper.insert(supplierProduct);
    }
} 