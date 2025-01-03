package _712.final_project_712.model.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

public class ReturnOrderTableDef extends TableDef {
    public static final ReturnOrderTableDef RETURN_ORDER = new ReturnOrderTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");
    public final QueryColumn ORDER_ID = new QueryColumn(this, "order_id");
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");
    public final QueryColumn RETURN_REASON = new QueryColumn(this, "return_reason");
    public final QueryColumn RETURN_AMOUNT = new QueryColumn(this, "return_amount");
    public final QueryColumn STATUS = new QueryColumn(this, "status");
    public final QueryColumn IMAGES = new QueryColumn(this, "images");
    public final QueryColumn APPLY_TIME = new QueryColumn(this, "apply_time");
    public final QueryColumn HANDLE_TIME = new QueryColumn(this, "handle_time");
    public final QueryColumn HANDLE_NOTE = new QueryColumn(this, "handle_note");
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public ReturnOrderTableDef() {
        super("order_return", "order_return");
    }
} 