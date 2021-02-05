package ru.clevertec.checksystem.core.common.builder;

import ru.clevertec.checksystem.core.common.IBuildable;
import ru.clevertec.checksystem.core.entity.Product;

import java.math.BigDecimal;

public interface IProductBuilder extends IBuildable<Product> {

    IProductBuilder setId(Long id);

    IProductBuilder setName(String name);

    IProductBuilder setPrice(BigDecimal price);
}
