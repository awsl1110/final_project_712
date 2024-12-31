package _712.final_project_712.model.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

public class ProductCategoryTableDef extends TableDef {
    
    public static final ProductCategoryTableDef PRODUCT_CATEGORY = new ProductCategoryTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");
    public final QueryColumn NAME = new QueryColumn(this, "name");
    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");
    public final QueryColumn LEVEL = new QueryColumn(this, "level");
    public final QueryColumn SORT_ORDER = new QueryColumn(this, "sort_order");
    public final QueryColumn STATUS = new QueryColumn(this, "status");
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public ProductCategoryTableDef() {
        super("product_category", "", "product_category");
    }
} 