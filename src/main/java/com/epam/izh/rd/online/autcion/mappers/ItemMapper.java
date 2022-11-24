package com.epam.izh.rd.online.autcion.mappers;

import com.epam.izh.rd.online.autcion.entity.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        Item item = new Item();

        item.setItemId(resultSet.getLong("item_id"));
        item.setBidIncrement(resultSet.getDouble("bid_increment"));
        item.setBuyItNow(resultSet.getBoolean("buy_it_now"));
        item.setDescription(resultSet.getString("description"));
        item.setStartDate(new java.sql.Date(resultSet.getDate("start_date").getTime()).toLocalDate());
        item.setStartPrice(resultSet.getDouble("start_price"));
        item.setStopDate(new java.sql.Date(resultSet.getDate("stop_date").getTime()).toLocalDate());
        item.setTitle(resultSet.getString("title"));
        item.setUserId(resultSet.getLong("user_id"));                

        return item;
    }
}
