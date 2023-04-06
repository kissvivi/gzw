package com.kk.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "task_main")
public class TaskMain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 发起人id
     */
    @Column(name = "from_user")
    private Integer fromUser;

    /**
     * 接收人id
     */
    @Column(name = "to_user")
    private String toUser;

    /**
     * 接收企业id
     */
    @Column(name = "to_com")
    private String toCom;

    /**
     * 类型，1：日报，2：周报，3：年报
     */
    private Integer type;

    /**
     * 状态，1：已下发，0：未下发
     */
    private Integer status;

    /**
     * 截止时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 任务名
     */
    private String name;

    /**
     * 任务号
     */
    @Column(name = "task_no")
    private String taskNo;

    /**
     * 模板
     */
    @Column(name = "temp_id")
    private Integer tempId;

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
     * 获取发起人id
     *
     * @return from_user - 发起人id
     */
    public Integer getFromUser() {
        return fromUser;
    }

    /**
     * 设置发起人id
     *
     * @param fromUser 发起人id
     */
    public void setFromUser(Integer fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * 获取接收人id
     *
     * @return to_user - 接收人id
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * 设置接收人id
     *
     * @param toUser 接收人id
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * 获取接收企业id
     *
     * @return to_com - 接收企业id
     */
    public String getToCom() {
        return toCom;
    }

    /**
     * 设置接收企业id
     *
     * @param toCom 接收企业id
     */
    public void setToCom(String toCom) {
        this.toCom = toCom;
    }

    /**
     * 获取类型，1：日报，2：周报，3：年报
     *
     * @return type - 类型，1：日报，2：周报，3：年报
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型，1：日报，2：周报，3：年报
     *
     * @param type 类型，1：日报，2：周报，3：年报
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取状态，1：已下发，0：未下发
     *
     * @return status - 状态，1：已下发，0：未下发
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态，1：已下发，0：未下发
     *
     * @param status 状态，1：已下发，0：未下发
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取截止时间
     *
     * @return end_time - 截止时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置截止时间
     *
     * @param endTime 截止时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取任务名
     *
     * @return name - 任务名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置任务名
     *
     * @param name 任务名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取任务号
     *
     * @return task_no - 任务号
     */
    public String getTaskNo() {
        return taskNo;
    }

    /**
     * 设置任务号
     *
     * @param taskNo 任务号
     */
    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    /**
     * 获取模板
     *
     * @return temp_id - 模板
     */
    public Integer getTempId() {
        return tempId;
    }

    /**
     * 设置模板
     *
     * @param tempId 模板
     */
    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }
}