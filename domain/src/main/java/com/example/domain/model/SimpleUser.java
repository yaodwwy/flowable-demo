package com.example.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleUser implements Serializable {
    private Long id;
    private String name;
    private String identity;
    /**
     * 消费金额
     */
    private int amount;
    /**
     * 规则计算后的金额
     */
    private double result;
    /**
     * 优惠后的金额
     */
    private int afterDiscount;

    public double doDiscount(double discount) {
        return this.amount * discount;
    }
}
