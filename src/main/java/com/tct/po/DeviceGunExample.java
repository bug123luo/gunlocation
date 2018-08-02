package com.tct.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceGunExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DeviceGunExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDeviceNoIsNull() {
            addCriterion("device_no is null");
            return (Criteria) this;
        }

        public Criteria andDeviceNoIsNotNull() {
            addCriterion("device_no is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceNoEqualTo(String value) {
            addCriterion("device_no =", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotEqualTo(String value) {
            addCriterion("device_no <>", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoGreaterThan(String value) {
            addCriterion("device_no >", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoGreaterThanOrEqualTo(String value) {
            addCriterion("device_no >=", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoLessThan(String value) {
            addCriterion("device_no <", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoLessThanOrEqualTo(String value) {
            addCriterion("device_no <=", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoLike(String value) {
            addCriterion("device_no like", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotLike(String value) {
            addCriterion("device_no not like", value, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoIn(List<String> values) {
            addCriterion("device_no in", values, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotIn(List<String> values) {
            addCriterion("device_no not in", values, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoBetween(String value1, String value2) {
            addCriterion("device_no between", value1, value2, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andDeviceNoNotBetween(String value1, String value2) {
            addCriterion("device_no not between", value1, value2, "deviceNo");
            return (Criteria) this;
        }

        public Criteria andGunMacIsNull() {
            addCriterion("gun_mac is null");
            return (Criteria) this;
        }

        public Criteria andGunMacIsNotNull() {
            addCriterion("gun_mac is not null");
            return (Criteria) this;
        }

        public Criteria andGunMacEqualTo(String value) {
            addCriterion("gun_mac =", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacNotEqualTo(String value) {
            addCriterion("gun_mac <>", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacGreaterThan(String value) {
            addCriterion("gun_mac >", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacGreaterThanOrEqualTo(String value) {
            addCriterion("gun_mac >=", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacLessThan(String value) {
            addCriterion("gun_mac <", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacLessThanOrEqualTo(String value) {
            addCriterion("gun_mac <=", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacLike(String value) {
            addCriterion("gun_mac like", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacNotLike(String value) {
            addCriterion("gun_mac not like", value, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacIn(List<String> values) {
            addCriterion("gun_mac in", values, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacNotIn(List<String> values) {
            addCriterion("gun_mac not in", values, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacBetween(String value1, String value2) {
            addCriterion("gun_mac between", value1, value2, "gunMac");
            return (Criteria) this;
        }

        public Criteria andGunMacNotBetween(String value1, String value2) {
            addCriterion("gun_mac not between", value1, value2, "gunMac");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeIsNull() {
            addCriterion("out_warehouse_time is null");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeIsNotNull() {
            addCriterion("out_warehouse_time is not null");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeEqualTo(Date value) {
            addCriterion("out_warehouse_time =", value, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeNotEqualTo(Date value) {
            addCriterion("out_warehouse_time <>", value, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeGreaterThan(Date value) {
            addCriterion("out_warehouse_time >", value, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("out_warehouse_time >=", value, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeLessThan(Date value) {
            addCriterion("out_warehouse_time <", value, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeLessThanOrEqualTo(Date value) {
            addCriterion("out_warehouse_time <=", value, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeIn(List<Date> values) {
            addCriterion("out_warehouse_time in", values, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeNotIn(List<Date> values) {
            addCriterion("out_warehouse_time not in", values, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeBetween(Date value1, Date value2) {
            addCriterion("out_warehouse_time between", value1, value2, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andOutWarehouseTimeNotBetween(Date value1, Date value2) {
            addCriterion("out_warehouse_time not between", value1, value2, "outWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeIsNull() {
            addCriterion("in_warehouse_time is null");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeIsNotNull() {
            addCriterion("in_warehouse_time is not null");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeEqualTo(Date value) {
            addCriterion("in_warehouse_time =", value, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeNotEqualTo(Date value) {
            addCriterion("in_warehouse_time <>", value, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeGreaterThan(Date value) {
            addCriterion("in_warehouse_time >", value, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("in_warehouse_time >=", value, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeLessThan(Date value) {
            addCriterion("in_warehouse_time <", value, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeLessThanOrEqualTo(Date value) {
            addCriterion("in_warehouse_time <=", value, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeIn(List<Date> values) {
            addCriterion("in_warehouse_time in", values, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeNotIn(List<Date> values) {
            addCriterion("in_warehouse_time not in", values, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeBetween(Date value1, Date value2) {
            addCriterion("in_warehouse_time between", value1, value2, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andInWarehouseTimeNotBetween(Date value1, Date value2) {
            addCriterion("in_warehouse_time not between", value1, value2, "inWarehouseTime");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Integer value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Integer value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Integer value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Integer value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Integer value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Integer> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Integer> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Integer value1, Integer value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Integer value1, Integer value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeIsNull() {
            addCriterion("temperance_time is null");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeIsNotNull() {
            addCriterion("temperance_time is not null");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeEqualTo(Date value) {
            addCriterion("temperance_time =", value, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeNotEqualTo(Date value) {
            addCriterion("temperance_time <>", value, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeGreaterThan(Date value) {
            addCriterion("temperance_time >", value, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("temperance_time >=", value, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeLessThan(Date value) {
            addCriterion("temperance_time <", value, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeLessThanOrEqualTo(Date value) {
            addCriterion("temperance_time <=", value, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeIn(List<Date> values) {
            addCriterion("temperance_time in", values, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeNotIn(List<Date> values) {
            addCriterion("temperance_time not in", values, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeBetween(Date value1, Date value2) {
            addCriterion("temperance_time between", value1, value2, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andTemperanceTimeNotBetween(Date value1, Date value2) {
            addCriterion("temperance_time not between", value1, value2, "temperanceTime");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Integer value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Integer value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Integer value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Integer value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Integer> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Integer> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Integer value1, Integer value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}