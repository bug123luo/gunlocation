package com.tct.po;

import java.util.Date;

public class DeviceGun {
    private Integer id;

    private String deviceNo;

    private String gunMac;

    private Date outWarehouseTime;

    private Date inWarehouseTime;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private Date temperanceTime;

    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo == null ? null : deviceNo.trim();
    }

    public String getGunMac() {
        return gunMac;
    }

    public void setGunMac(String gunMac) {
        this.gunMac = gunMac == null ? null : gunMac.trim();
    }

    public Date getOutWarehouseTime() {
        return outWarehouseTime;
    }

    public void setOutWarehouseTime(Date outWarehouseTime) {
        this.outWarehouseTime = outWarehouseTime;
    }

    public Date getInWarehouseTime() {
        return inWarehouseTime;
    }

    public void setInWarehouseTime(Date inWarehouseTime) {
        this.inWarehouseTime = inWarehouseTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getTemperanceTime() {
        return temperanceTime;
    }

    public void setTemperanceTime(Date temperanceTime) {
        this.temperanceTime = temperanceTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}