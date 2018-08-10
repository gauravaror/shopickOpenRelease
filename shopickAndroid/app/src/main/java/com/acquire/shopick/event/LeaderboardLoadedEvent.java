package com.acquire.shopick.event;

import com.acquire.shopick.dao.User;

import java.util.ArrayList;

/**
 * Created by gaurav on 4/4/16.
 */
public class LeaderboardLoadedEvent {
    public LeaderboardLoadedEvent(ArrayList<User> feeds) {
        this.board = feeds;
    }

    public ArrayList<User> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<User> board) {
        this.board = board;
    }

    ArrayList<User> board;

}
