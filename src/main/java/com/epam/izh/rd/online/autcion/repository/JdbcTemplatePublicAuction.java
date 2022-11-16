package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;

import java.util.List;
import java.util.Map;

import com.epam.izh.rd.online.autcion.mappers.BidMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Repository
public class JdbcTemplatePublicAuction implements PublicAuction {

    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public JdbcTemplatePublicAuction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bid> getUserBids(long id) {
        //return emptyList();

        String sqlGetUserBids = "SELECT bids.bid_value FROM bids WHERE user_id=" + id;
        BidMapper bidMapper = new BidMapper();
        return jdbcTemplate.query(sqlGetUserBids, bidMapper);
    }

    @Override
    public List<Item> getUserItems(long id) {
        return emptyList();
    }

    @Override
    public Item getItemByName(String name) {
        return new Item();
    }

    @Override
    public Item getItemByDescription(String name) {
        return new Item();
    }

    @Override
    public Map<User, Double> getAvgItemCost() {
        return emptyMap();
    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        return emptyMap();
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
