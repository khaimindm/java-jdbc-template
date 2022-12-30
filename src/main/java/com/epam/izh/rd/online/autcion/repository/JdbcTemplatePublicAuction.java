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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class JdbcTemplatePublicAuction implements PublicAuction {

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

        for (Item item: itemList) {
            String sqlMaxBidForEveryItem = "SELECT * " +
                    "FROM bids " +
                    "WHERE bid_value = (SELECT MAX(bids.bid_value) " +
                    "FROM items " +
                    "INNER JOIN bids " +
                    "ON items.item_id = bids.item_id " +
                    "WHERE items.item_id = '" + item.getItemId() + "')";
            try {
                Object bidsResult = jdbcTemplate.queryForObject(sqlMaxBidForEveryItem, new BidMapper());
                maxBidsForEveryItem.put(item, (Bid) bidsResult);
            } catch (DataAccessException e) {
                continue;
            }
        }
        return maxBidsForEveryItem;
    }

    @Override
    public boolean createUser(User user) {
        String sqlCreateUser = "INSERT INTO users (user_id, billing_address, full_name, login, password) " +
                "VALUES (" + "'" + user.getUserId() + "'" + "," + "'" + user.getBillingAddress() + "'" + "," + "'" +
                user.getFullName() + "'" + "," + "'" + user.getLogin() + "'" + "," + "'" + user.getPassword() + "')";
        int testCreateUser = jdbcTemplate.update(sqlCreateUser);
        if (testCreateUser == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createItem(Item item) {
        String sqlCreateItem = "INSERT INTO items (item_id, bid_increment, buy_it_now, description, start_date, " +
                "start_price, stop_date, title, user_id) " +
                "VALUES ('" + item.getItemId() + "'," + "'" + item.getBidIncrement() +"', '" + item.getBuyItNow() +
                "', '" + item.getDescription() + "', '" + item.getStartDate() + "', '" + item.getStartPrice() + "', '"
                + item.getStopDate() + "', '" + item.getTitle() + "', '" + item.getUserId() + "')";
        int testCreateItem = jdbcTemplate.update(sqlCreateItem);
        if (testCreateItem == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createBid(Bid bid) {
        String sqlCreateBid = "INSERT INTO bids (bid_id, bid_date, bid_value, item_id, user_id) " +
                "VALUES ('" + bid.getBidId() +"', '" + bid.getBidDate() + "', '" + bid.getBidValue() + "', '" +
                bid.getItemId() + "', '" + bid.getUserId() + "')";
        int testCreateBid = jdbcTemplate.update(sqlCreateBid);
        if (testCreateBid == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteUserBids(long id) {
        String sqlDeleteUserBids = "DELETE FROM bids WHERE user_id = " + id;
        int testDeleteUserBids = jdbcTemplate.update(sqlDeleteUserBids);
        if (testDeleteUserBids == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        String sqlDoubleItemsStartPrice = "UPDATE items " +
                "SET start_price = start_price * 2 " +
                "WHERE user_id = " + id;
        int testDoubleItemsStartPrice = jdbcTemplate.update(sqlDoubleItemsStartPrice);
        if (testDoubleItemsStartPrice == 1) {
            return true;
        } else {
            return false;
        }
    }
}
