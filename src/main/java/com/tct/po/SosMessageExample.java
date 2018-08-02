package com.tct.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SosMessageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SosMessageExample() {
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

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(String value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(String value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(String value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(String value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(String value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(String value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLike(String value) {
            addCriterion("latitude like", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotLike(String value) {
            addCriterion("latitude not like", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<String> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<String> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(String value1, String value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(String value1, String value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNull() {
            addCriterion("longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(String value) {
            addCriterion("longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(String value) {
            addCriterion("longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(String value) {
            addCriterion("longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(String value) {
            addCriterion("longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(String value) {
            addCriterion("longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(String value) {
            addCriterion("longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLike(String value) {
            addCriterion("longitude like", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotLike(String value) {
            addCriterion("longitude not like", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<String> values) {
            addCriterion("longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<String> values) {
            addCriterion("longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(String value1, String value2) {
            addCriterion("longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(String value1, String value2) {
            addCriterion("longitude not between", value1, value2, "longitude");
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

        public Criteria andSosTimeIsNull() {
            addCriterion("sos_time is null");
            return (Criteria) this;
        }

        public Criteria andSosTimeIsNotNull() {
            addCriterion("sos_time is not null");
            return (Criteria) this;
        }

        public Criteria andSosTimeEqualTo(Date value) {
            addCriterion("sos_time =", value, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeNotEqualTo(Date value) {
            addCriterion("sos_time <>", value, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeGreaterThan(Date value) {
            addCriterion("sos_time >", value, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("sos_time >=", value, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeLessThan(Date value) {
            addCriterion("sos_time <", value, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeLessThanOrEqualTo(Date value) {
            addCriterion("sos_time <=", value, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeIn(List<Date> values) {
            addCriterion("sos_time in", values, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeNotIn(List<Date> values) {
            addCriterion("sos_time not in", values, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeBetween(Date value1, Date value2) {
            addCriterion("sos_time between", value1, value2, "sosTime");
            return (Criteria) this;
        }

        public Criteria andSosTimeNotBetween(Date value1, Date value2) {
            addCriterion("sos_time not between", value1, value2, "sosTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeIsNull() {
            addCriterion("finally_time is null");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeIsNotNull() {
            addCriterion("finally_time is not null");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeEqualTo(Date value) {
            addCriterion("finally_time =", value, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeNotEqualTo(Date value) {
            addCriterion("finally_time <>", value, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeGreaterThan(Date value) {
            addCriterion("finally_time >", value, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("finally_time >=", value, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeLessThan(Date value) {
            addCriterion("finally_time <", value, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeLessThanOrEqualTo(Date value) {
            addCriterion("finally_time <=", value, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeIn(List<Date> values) {
            addCriterion("finally_time in", values, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeNotIn(List<Date> values) {
            addCriterion("finally_time not in", values, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeBetween(Date value1, Date value2) {
            addCriterion("finally_time between", value1, value2, "finallyTime");
            return (Criteria) this;
        }

        public Criteria andFinallyTimeNotBetween(Date value1, Date value2) {
            addCriterion("finally_time not between", value1, value2, "finallyTime");
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