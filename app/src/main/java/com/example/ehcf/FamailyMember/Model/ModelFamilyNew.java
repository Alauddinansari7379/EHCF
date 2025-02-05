package com.example.ehcf.FamailyMember.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelFamilyNew {

        @SerializedName("result")
        @Expose
        private List<Result> result;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Integer status;

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }



    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("relation")
        @Expose
        private String relation;

        public String getStateId() {
            return id;
        }

        public void setStateId(String stateId) {
            this.id = stateId;
        }

        public String getName() {
            return relation;
        }

        public void setName(String name) {
            this.relation = name;
        }

    }
}
