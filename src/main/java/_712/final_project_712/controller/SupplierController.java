package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.Supplier;
import _712.final_project_712.model.dto.SupplierDTO;
import _712.final_project_712.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "供应商管理")
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Operation(summary = "添加供应商")
    @PostMapping("/add")
    public Result<?> addSupplier(@RequestBody SupplierDTO.AddSupplierRequest request) {
        try {
            supplierService.addSupplier(request);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "更新供应商信息")
    @PutMapping("/{supplierId}")
    public Result<?> updateSupplier(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long supplierId,
            @RequestBody SupplierDTO.UpdateSupplierRequest request) {
        try {
            supplierService.updateSupplier(supplierId, request);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取供应商列表")
    @GetMapping("/list")
    public Result<List<Supplier>> getSupplierList() {
        try {
            List<Supplier> suppliers = supplierService.getSupplierList();
            return Result.success(suppliers);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取供应商基本信息")
    @GetMapping("/{supplierId}")
    public Result<Supplier> getSupplierById(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long supplierId) {
        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            return Result.success(supplier);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取供应商详情（包含商品信息）")
    @GetMapping("/{supplierId}/detail")
    public Result<SupplierDTO.SupplierDetailResponse> getSupplierDetail(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long supplierId) {
        try {
            SupplierDTO.SupplierDetailResponse detail = supplierService.getSupplierDetail(supplierId);
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "更新供应商状态")
    @PutMapping("/{supplierId}/status")
    public Result<?> updateSupplierStatus(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long supplierId,
            @Parameter(description = "状态：0-禁用，1-启用", required = true) @RequestParam Integer status) {
        try {
            supplierService.updateSupplierStatus(supplierId, status);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "添加供应商商品")
    @PostMapping("/{supplierId}/product")
    public Result<?> addSupplierProduct(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long supplierId,
            @RequestBody SupplierDTO.AddSupplierProductRequest request) {
        try {
            supplierService.addSupplierProduct(supplierId, request);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 