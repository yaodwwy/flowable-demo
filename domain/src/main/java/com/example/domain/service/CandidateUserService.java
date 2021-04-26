package com.example.domain.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CandidateUserService implements Serializable  {

    public List<String> getUsers() {
        List<String> result = new ArrayList<String>();
        result.add("userA");
        result.add("userB");
        return result;
    }
}
