package com.example.ehcf.Prescription.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class My_model {

    @SerializedName("result")
    @Expose
    private List<List<Result>> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<List<Result>> getResult() {
        return result;
    }

    public void setResult(List<List<Result>> result) {
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
        @SerializedName("patient_id")
        @Expose
        private String patientId;
        @SerializedName("doctor_id")
        @Expose
        private String doctorId;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("consultation_type")
        @Expose
        private String consultationType;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("comments")
        @Expose
        private Object comments;
        @SerializedName("customer_rating")
        @Expose
        private String customerRating;
        @SerializedName("customer_comments")
        @Expose
        private Object customerComments;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("rtest")
        @Expose
        private String rtest;
        @SerializedName("slot_id")
        @Expose
        private String slotId;
        @SerializedName("doctor_name")
        @Expose
        private String doctorName;
        @SerializedName("phone_number")
        @Expose
        private String phoneNumber;
        @SerializedName("specialist")
        @Expose
        private String specialist;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("end_time")
        @Expose
        private String endTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getConsultationType() {
            return consultationType;
        }

        public void setConsultationType(String consultationType) {
            this.consultationType = consultationType;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

        public String getCustomerRating() {
            return customerRating;
        }

        public void setCustomerRating(String customerRating) {
            this.customerRating = customerRating;
        }

        public Object getCustomerComments() {
            return customerComments;
        }

        public void setCustomerComments(Object customerComments) {
            this.customerComments = customerComments;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getRtest() {
            return rtest;
        }

        public void setRtest(String rtest) {
            this.rtest = rtest;
        }

        public String getSlotId() {
            return slotId;
        }

        public void setSlotId(String slotId) {
            this.slotId = slotId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSpecialist() {
            return specialist;
        }

        public void setSpecialist(String specialist) {
            this.specialist = specialist;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

    }


}
