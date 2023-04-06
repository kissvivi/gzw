package com.kk.api.entity;

import java.util.Date;
import javax.persistence.*;

public class Msg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String msg;

    @Column(name = "caeate_time")
    private Date caeateTime;

    /**
     * 0未通知 1已通知
     */
    private Integer status;

//    @Column(name = "to_user")
//    private Integer toUser;

    @Column(name = "to_com")
    private Integer toCom;

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
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return caeate_time
     */
    public Date getCaeateTime() {
        return caeateTime;
    }

    /**
     * @param caeateTime
     */
    public void setCaeateTime(Date caeateTime) {
        this.caeateTime = caeateTime;
    }

    /**
     * 获取0未通知 1已通知
     *
     * @return status - 0未通知 1已通知
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0未通知 1已通知
     *
     * @param status 0未通知 1已通知
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

//    /**
//     * @return to_user
//     */
//    public Integer getToUser() {
//        return toUser;
//    }
//
//    /**
//     * @param toUser
//     */
//    public void setToUser(Integer toUser) {
//        this.toUser = toUser;
//    }

    /**
     * @return to_com
     */
    public Integer getToCom() {
        return toCom;
    }

    /**
     * @param toCom
     */
    public void setToCom(Integer toCom) {
        this.toCom = toCom;
    }
}