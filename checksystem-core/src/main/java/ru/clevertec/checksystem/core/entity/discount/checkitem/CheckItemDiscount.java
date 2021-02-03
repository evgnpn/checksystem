package ru.clevertec.checksystem.core.entity.discount.checkitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.clevertec.checksystem.core.common.check.ICheckItemComposable;
import ru.clevertec.checksystem.core.entity.check.CheckItem;
import ru.clevertec.checksystem.core.entity.discount.Discount;
import ru.clevertec.checksystem.core.util.ThrowUtils;
import ru.clevertec.customlib.json.StringifyIgnore;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class CheckItemDiscount extends Discount<CheckItemDiscount> implements ICheckItemComposable {

    @JsonIgnore
    private CheckItem checkItem;

    protected CheckItemDiscount() {
    }

    public CheckItemDiscount(String description) {
        super(description);
    }

    public CheckItemDiscount(int id, String description) {
        super(id, description);
    }

    public CheckItemDiscount(int id, String description, CheckItemDiscount dependentDiscount) {
        super(id, description, dependentDiscount);
    }

    @StringifyIgnore
    public CheckItem getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(CheckItem checkItem) {

        ThrowUtils.Argument.nullValue("checkItem", checkItem);

        this.checkItem = checkItem;

        if (getDependentDiscount() != null) {
            getDependentDiscount().setCheckItem(checkItem);
        }
    }
}
