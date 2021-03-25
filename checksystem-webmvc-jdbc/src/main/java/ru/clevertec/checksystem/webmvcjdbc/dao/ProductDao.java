package ru.clevertec.checksystem.webmvcjdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.clevertec.checksystem.core.constant.Entities;
import ru.clevertec.checksystem.core.entity.BaseEntity;
import ru.clevertec.checksystem.core.entity.Product;
import ru.clevertec.checksystem.webmvcjdbc.mapper.ProductMapper;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductDao extends Dao<Product, Long> {

    private final static String SQL_FIND_BY_ID = "SELECT * FROM " + Entities.Table.PRODUCTS + " WHERE id = :id";
    private final static String SQL_FIND_ALL = "SELECT * FROM " + Entities.Table.PRODUCTS;
    private final static String SQL_FIND_ALL_BY_ID = "SELECT * FROM " + Entities.Table.PRODUCTS + " WHERE id IN (:ids)";

    private final static String SQL_DELETE_ALL = "DELETE FROM " + Entities.Table.PRODUCTS;
    private final static String SQL_DELETE_BY_ID = "DELETE FROM " + Entities.Table.PRODUCTS + " WHERE id = :id";
    private final static String SQL_DELETE_ALL_BY_ID = "DELETE FROM " + Entities.Table.PRODUCTS + " WHERE id IN(:ids)";

    private final static String SQL_UPDATE_BY_ID = "UPDATE " + Entities.Table.PRODUCTS + " SET name = :name, price = :price WHERE id = :id";

    private final static String SQL_INSERT = "INSERT INTO " + Entities.Table.PRODUCTS + "(id, name, price) VALUES(:id, :name, :price)";

    private final ProductMapper rowMapper;

    @Autowired
    public ProductDao(DataSource dataSource, ProductMapper rowMapper) {
        super(new NamedParameterJdbcTemplate(dataSource));
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Product> findAll() {
        return getJdbcTemplate().query(SQL_FIND_ALL, rowMapper);
    }

    @Override
    public List<Product> findAllById(Collection<Long> ids) {
        var parameters = new MapSqlParameterSource().addValue("ids", ids);
        return getJdbcTemplate().query(SQL_FIND_ALL_BY_ID, parameters, rowMapper);
    }

    @Override
    public Product findById(Long id) {
        var parameters = new MapSqlParameterSource().addValue(Entities.Column.ID, id);
        return getJdbcTemplate().queryForObject(SQL_FIND_BY_ID, parameters, rowMapper);
    }

    @Override
    public int deleteById(Long id) {
        var parameters = new MapSqlParameterSource().addValue(Entities.Column.ID, id);
        return getJdbcTemplate().update(SQL_DELETE_BY_ID, parameters);
    }

    @Override
    public int delete(Product entity) {
        var parameters = new MapSqlParameterSource().addValue("id", entity.getId());
        return getJdbcTemplate().update(SQL_DELETE_BY_ID, parameters);
    }

    @Override
    public <S extends Product> int deleteAll(Collection<S> entities) {
        var ids = entities.stream().map(BaseEntity::getId).collect(Collectors.toList());
        var parameters = new MapSqlParameterSource().addValue("ids", ids);
        return getJdbcTemplate().update(SQL_DELETE_ALL_BY_ID, parameters);
    }

    @Override
    public int deleteAll() {
        return getJdbcTemplate().update(SQL_DELETE_ALL, new MapSqlParameterSource());
    }

    @Override
    public Product update(Product entity) {
        getJdbcTemplate().update(SQL_UPDATE_BY_ID, createMapSqlParameterSource(entity));
        return findById(entity.getId());
    }

    @Override
    public <S extends Product> List<Product> updateAll(Collection<S> entities) {
        getJdbcTemplate().batchUpdate(SQL_UPDATE_BY_ID, createBatchMapSqlParameterSource(entities));
        var ids = entities.stream().map(BaseEntity::getId).collect(Collectors.toList());
        return findAllById(ids);
    }

    @Override
    public Product add(Product entity) {
        var keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(SQL_INSERT, createMapSqlParameterSource(entity), keyHolder, new String[]{"id"});
        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public <S extends Product> List<Product> addAll(Collection<S> entities) {
        return entities.stream().map(this::add).collect(Collectors.toList());
    }

    private static MapSqlParameterSource[] createBatchMapSqlParameterSource(Collection<? extends Product> entities) {
        return entities.stream().map(ProductDao::createMapSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private static MapSqlParameterSource createMapSqlParameterSource(Product entity) {
        return new MapSqlParameterSource()
                .addValue(Entities.Column.ID, entity.getId())
                .addValue(Entities.Column.NAME, entity.getName())
                .addValue(Entities.Column.PRICE, entity.getPrice());
    }
}
