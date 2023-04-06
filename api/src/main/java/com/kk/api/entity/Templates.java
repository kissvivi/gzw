package com.kk.api.entity;

import java.util.Date;
import javax.persistence.*;

public class Templates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 模板名
     */
    private String name;

    /**
     * 模板地址
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * 格式 1：xls
     */
    private Integer type;

    @Column(name = "create_time")
    private Date createTime;

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
     * 获取模板名
     *
     * @return name - 模板名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板名
     *
     * @param name 模板名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取模板地址
     *
     * @return file_url - 模板地址
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置模板地址
     *
     * @param fileUrl 模板地址
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取格式 1：xls
     *
     * @return type - 格式 1：xls
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置格式 1：xls
     *
     * @param type 格式 1：xls
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}