package com.kk.api.entity;

import javax.persistence.*;

public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 代号
     */
    @Column(name = "com_no")
    private String comNo;

    /**
     * 名称
     */
    private String name;

    /**
     * 联系人
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 联系人电话
     */
    @Column(name = "user_phone")
    private Integer userPhone;

    /**
     * 关联用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取代号
     *
     * @return com_no - 代号
     */
    public String getComNo() {
        return comNo;
    }

    /**
     * 设置代号
     *
     * @param comNo 代号
     */
    public void setComNo(String comNo) {
        this.comNo = comNo;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取联系人
     *
     * @return user_name - 联系人
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置联系人
     *
     * @param userName 联系人
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取联系人电话
     *
     * @return user_phone - 联系人电话
     */
    public Integer getUserPhone() {
        return userPhone;
    }

    /**
     * 设置联系人电话
     *
     * @param userPhone 联系人电话
     */
    public void setUserPhone(Integer userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * 获取关联用户id
     *
     * @return user_id - 关联用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置关联用户id
     *
     * @param userId 关联用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}