package com.barryirvine.foursquare.model;

import java.util.List;

public class ExploreDetailResponse {

    List<Group> groups;

    public List<Item> getItems() {
        return groups.get(0).getItems();
    }

}
