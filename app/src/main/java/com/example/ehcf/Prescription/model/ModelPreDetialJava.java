package com.example.ehcf.Prescription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelPreDetialJava {


        @SerializedName("result")
        @Expose
        private List<Result1> result;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Integer status;

        public List<Result1> getResult() {
            return result;
        }

        public void setResult(List<Result1> result) {
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
        @SerializedName("doctor_id")
        @Expose
        private String doctorId;
        @SerializedName("patient_id")
        @Expose
        private String patientId;
        @SerializedName("subjective_information")
        @Expose
        private String subjectiveInformation;
        @SerializedName("objective_information")
        @Expose
        private String objectiveInformation;
        @SerializedName("assessment")
        @Expose
        private String assessment;
        @SerializedName("plan")
        @Expose
        private String plan;
        @SerializedName("doctor_notes")
        @Expose
        private String doctorNotes;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("booking_id")
        @Expose
        private String bookingId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("report")
        @Expose
        private Object report;
        @SerializedName("is_test")
        @Expose
        private String isTest;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getSubjectiveInformation() {
            return subjectiveInformation;
        }

        public void setSubjectiveInformation(String subjectiveInformation) {
            this.subjectiveInformation = subjectiveInformation;
        }

        public String getObjectiveInformation() {
            return objectiveInformation;
        }

        public void setObjectiveInformation(String objectiveInformation) {
            this.objectiveInformation = objectiveInformation;
        }

        public String getAssessment() {
            return assessment;
        }

        public void setAssessment(String assessment) {
            this.assessment = assessment;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getDoctorNotes() {
            return doctorNotes;
        }

        public void setDoctorNotes(String doctorNotes) {
            this.doctorNotes = doctorNotes;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
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

        public Object getReport() {
            return report;
        }

        public void setReport(Object report) {
            this.report = report;
        }

        public String getIsTest() {
            return isTest;
        }

        public void setIsTest(String isTest) {
            this.isTest = isTest;
        }

    }
    }

