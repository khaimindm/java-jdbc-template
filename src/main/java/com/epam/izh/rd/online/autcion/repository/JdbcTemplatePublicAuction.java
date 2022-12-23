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

import java.time.LocalDate;
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

        for (Item item: itemList) {
            item.getItemId()
        }

        /*
        SELECT items_item_id, bid_increment, buy_it_now, description, start_date, start_price, stop_date, title, user_id, bid_id, bid_date, bid_value, bids_user_id
        FROM (SELECT items.item_id AS items_item_id, items.bid_increment, items.buy_it_now, items.description, items.start_date, items.start_price, items.stop_date, items.title, items.user_id,
                bids.bid_id, bids.bid_date, bids.bid_value, bids.user_id AS bids_user_id
                FROM items
                INNER JOIN bids
                ON items.item_id = bids.item_id) AS result
        WHERE result.items_item_id = '1'
         */

        
        String sqlMaxBidForEveryItem = "SELECT T1.item_id AS t1itemid, T1.bid_increment, T1.buy_it_now, T1.description, T1.start_date, T1.start_price, T1.stop_date, T1.title, T1.user_id, " +
                    "T2.bid_id, T2.bid_date, MAX(T2.bid_value), T2.user_id AS bidsuserid " +
                    "FROM items AS T1 " +
                    "LEFT JOIN bids AS T2 " +
                    "ON T1.item_id = T2.item_id " +
                    "GROUP BY T1.item_id";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlMaxBidForEveryItem);

        for (Map<String,Object> map : results) {    
            
            String testString = map.get("bid_value").toString();

            if (map.get("bid_value") != null) {                        
            Item item = new Item();
            item.setItemId(Long.valueOf(map.get("item_id").toString()));
            item.setBidIncrement(Double.valueOf(map.get("bid_increment").toString()));
            item.setBuyItNow(Boolean.valueOf(map.get("buy_it_now").toString()));
            item.setDescription(map.get("description").toString());
            item.setStartDate(LocalDate.parse(map.get("start_date").toString()));
            item.setStartPrice(Double.valueOf(map.get("start_price").toString()));
            item.setStopDate(LocalDate.parse(map.get("stop_date").toString()));
            item.setTitle(map.get("title").toString());
            item.setUserId(Long.valueOf(map.get("user_id").toString()));

            Bid bid = new Bid();
            bid.setBidId(Long.valueOf(map.get("bid_id").toString()));
            bid.setBidDate(LocalDate.parse(map.get("bid_date").toString()));
            bid.setBidValue(Double.valueOf(map.get("bid_value").toString()));
            bid.setItemId(Long.valueOf(map.get("item_id").toString()));
            bid.setUserId(Long.valueOf(map.get("bidsuserid").toString()));

            maxBidsForEveryItem.put(item, bid);
            } else {
                continue;
            }
        }

        //"WHERE item_id =" + "'" + item.getItemId() + "'" + ")";

            //Bid bid = jdbcTemplate.queryForObject(sqlMaxBidForEveryItem, new BidMapper());
            /*if (bid != null) {
                maxBidsForEveryItem.put(item, bid);
            } else {
                continue;
            }*/
        
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
