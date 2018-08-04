package com.tct.po;

import java.util.ArrayList;
import java.util.List;

public class SoftwareVersionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SoftwareVersionExample() {
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

        public Criteria andSoftwarenameIsNull() {
            addCriterion("softwarename is null");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameIsNotNull() {
            addCriterion("softwarename is not null");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameEqualTo(String value) {
            addCriterion("softwarename =", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameNotEqualTo(String value) {
            addCriterion("softwarename <>", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameGreaterThan(String value) {
            addCriterion("softwarename >", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameGreaterThanOrEqualTo(String value) {
            addCriterion("softwarename >=", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameLessThan(String value) {
            addCriterion("softwarename <", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameLessThanOrEqualTo(String value) {
            addCriterion("softwarename <=", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameLike(String value) {
            addCriterion("softwarename like", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameNotLike(String value) {
            addCriterion("softwarename not like", value, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameIn(List<String> values) {
            addCriterion("softwarename in", values, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameNotIn(List<String> values) {
            addCriterion("softwarename not in", values, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameBetween(String value1, String value2) {
            addCriterion("softwarename between", value1, value2, "softwarename");
            return (Criteria) this;
        }

        public Criteria andSoftwarenameNotBetween(String value1, String value2) {
            addCriterion("softwarename not between", value1, value2, "softwarename");
            return (Criteria) this;
        }

        public Criteria andLastversionIsNull() {
            addCriterion("lastversion is null");
            return (Criteria) this;
        }

        public Criteria andLastversionIsNotNull() {
            addCriterion("lastversion is not null");
            return (Criteria) this;
        }

        public Criteria andLastversionEqualTo(String value) {
            addCriterion("lastversion =", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionNotEqualTo(String value) {
            addCriterion("lastversion <>", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionGreaterThan(String value) {
            addCriterion("lastversion >", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionGreaterThanOrEqualTo(String value) {
            addCriterion("lastversion >=", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionLessThan(String value) {
            addCriterion("lastversion <", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionLessThanOrEqualTo(String value) {
            addCriterion("lastversion <=", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionLike(String value) {
            addCriterion("lastversion like", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionNotLike(String value) {
            addCriterion("lastversion not like", value, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionIn(List<String> values) {
            addCriterion("lastversion in", values, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionNotIn(List<String> values) {
            addCriterion("lastversion not in", values, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionBetween(String value1, String value2) {
            addCriterion("lastversion between", value1, value2, "lastversion");
            return (Criteria) this;
        }

        public Criteria andLastversionNotBetween(String value1, String value2) {
            addCriterion("lastversion not between", value1, value2, "lastversion");
            return (Criteria) this;
        }

        public Criteria andDownloadurlIsNull() {
            addCriterion("downloadurl is null");
            return (Criteria) this;
        }

        public Criteria andDownloadurlIsNotNull() {
            addCriterion("downloadurl is not null");
            return (Criteria) this;
        }

        public Criteria andDownloadurlEqualTo(String value) {
            addCriterion("downloadurl =", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlNotEqualTo(String value) {
            addCriterion("downloadurl <>", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlGreaterThan(String value) {
            addCriterion("downloadurl >", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlGreaterThanOrEqualTo(String value) {
            addCriterion("downloadurl >=", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlLessThan(String value) {
            addCriterion("downloadurl <", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlLessThanOrEqualTo(String value) {
            addCriterion("downloadurl <=", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlLike(String value) {
            addCriterion("downloadurl like", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlNotLike(String value) {
            addCriterion("downloadurl not like", value, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlIn(List<String> values) {
            addCriterion("downloadurl in", values, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlNotIn(List<String> values) {
            addCriterion("downloadurl not in", values, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlBetween(String value1, String value2) {
            addCriterion("downloadurl between", value1, value2, "downloadurl");
            return (Criteria) this;
        }

        public Criteria andDownloadurlNotBetween(String value1, String value2) {
            addCriterion("downloadurl not between", value1, value2, "downloadurl");
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