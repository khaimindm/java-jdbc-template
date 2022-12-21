package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;

import java.util.List;
import java.util.Map;

import com.epam.izh.rd.online.autcion.mappers.BidMapper;
import com.epam.izh.rd.online.autcion.mappers.ItemMapper;
import com.epam.izh.rd.online.autcion.mappers.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.util.HashMap;

@Repository
public class JdbcTemplatePublicAuction implements PublicAuction {

    /*private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public JdbcTemplatePublicAuction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }*/

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Bid> getUserBids(long id) {        
        String sqlGetUserBids = "SELECT * FROM bids WHERE user_id=" + id;        
        return jdbcTemplate.query(sqlGetUserBids, new BidMapper());
    }

    @Override
    public List<Item> getUserItems(long id) {
        String sqlGetUserItems = "SELECT * FROM items WHERE user_id=" + id;        
        return jdbcTemplate.query(sqlGetUserItems, new ItemMapper());
    }

    @Override
    public Item getItemByName(String name) {
        String sqlGetItemByName = "SELECT * FROM items WHERE title=" + "'" + name + "'";        
        return jdbcTemplate.queryForObject(sqlGetItemByName, new ItemMapper());        
    }

    @Override
    public Item getItemByDescription(String name) {
        String sqlGetItemByDescription = "SELECT * FROM items WHERE description=" + "'" + name + "'";
        return jdbcTemplate.queryForObject(sqlGetItemByDescription, new ItemMapper());
    }

    @Override
    public Map<User, Double> getAvgItemCost() {
        Map<User, Double> avgItemCost = new HashMap<>();

        String sqlGetUsers = "SELECT * FROM users";
        List<User> userList = jdbcTemplate.query(sqlGetUsers, new UserMapper());

        for (User user : userList) {
            String avgItem = "SELECT AVG(start_price) FROM items WHERE user_id = " + "'" + user.getUserId() + "'";
            Object avgResult = jdbcTemplate.queryForObject(avgItem, double.class);
            if (avgResult != null) {
                avgItemCost.put(user, Double.valueOf(avgResult.toString()));
            } else {
                continue;
            }
        }

        return avgItemCost;       
    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        Map<Item, Bid> maxBidsForEveryItem = new HashMap<>();

        String sqlGetItems = "SELECT * FROM items";
        List<Item> itemList = jdbcTemplate.query(sqlGetItems, new ItemMapper());

        for (Item item : itemList) {
            String sqlMaxBidForEveryItem = "SELECT * " +
                    "FROM bids " +
                    "WHERE bid_value = (SELECT MAX(bid_value) " +
                    "FROM bids " +
                    "WHERE item_id =" + "'" + item.getItemId() + "'" + ")";
            Bid bid = jdbcTemplate.queryForObject(sqlMaxBidForEveryItem, new BidMapper());
            if (bid != null) {
                maxBidsForEveryItem.put(item, bid);
            } else {
                continue;
            }
        }
        return maxBidsForEveryItem;
    }

    @Override
    public boolean createUser(User user) {
        return false;
    }

    @Override
    public boolean createItem(Item item) {
        return false;
    }

    @Override
    public boolean createBid(Bid bid) {
        return false;
    }

    @Override
    public boolean deleteUserBids(long id) {
        return false;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        return false;
    }
}
