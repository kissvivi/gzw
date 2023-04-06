package com.kk.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "task_detail")
public class TaskDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 状态 0：未填报，1：已填报，2：已过期
     */
    private Integer status;

    /**
     * 实际填写企业
     */
    @Column(name = "com_id")
    private Integer comId;

    /**
     * 实际填写人
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 上传时间
     */
    @Column(name = "upload_time")
    private Date uploadTime;

    /**
     * 文件存放地址
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * name
     */
    private String name;

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
     * 获取主任务id
     *
     * @return task_id - 主任务id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置主任务id
     *
     * @param taskId 主任务id
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取状态 0：未填报，1：已填报，2：已过期
     *
     * @return status - 状态 0：未填报，1：已填报，2：已过期
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0：未填报，1：已填报，2：已过期
     *
     * @param status 状态 0：未填报，1：已填报，2：已过期
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取实际填写企业
     *
     * @return com_id - 实际填写企业
     */
    public Integer getComId() {
        return comId;
    }

    /**
     * 设置实际填写企业
     *
     * @param comId 实际填写企业
     */
    public void setComId(Integer comId) {
        this.comId = comId;
    }

    /**
     * 获取实际填写人
     *
     * @return user_id - 实际填写人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置实际填写人
     *
     * @param userId 实际填写人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取上传时间
     *
     * @return upload_time - 上传时间
     */
    public Date getUploadTime() {
        return uploadTime;
    }

    /**
     * 设置上传时间
     *
     * @param uploadTime 上传时间
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * 获取文件存放地址
     *
     * @return file_url - 文件存放地址
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置文件存放地址
     *
     * @param fileUrl 文件存放地址
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取name
     *
     * @return name - name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }
}