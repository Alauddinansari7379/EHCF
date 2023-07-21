package com.example.ehcf.Fragment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doctors {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("hospital_id")
    @Expose
    private String hospitalId;
    @SerializedName("unique_code")
    @Expose
    private String uniqueCode;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("phone_with_code")
    @Expose
    private String phoneWithCode;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("specialist")
    @Expose
    private String specialist;
    @SerializedName("sub_specialist")
    @Expose
    private String subSpecialist;
    @SerializedName("additional_qualification")
    @Expose
    private String additionalQualification;
    @SerializedName("no_of_ratings")
    @Expose
    private String noOfRatings;
    @SerializedName("overall_ratings")
    @Expose
    private String overallRatings;
    @SerializedName("document_update_status")
    @Expose
    private String documentUpdateStatus;
    @SerializedName("document_approved_status")
    @Expose
    private String documentApprovedStatus;
    @SerializedName("profile_status")
    @Expose
    private String profileStatus;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;
    @SerializedName("c_id")
    @Expose
    private String cId;
    @SerializedName("c_stat")
    @Expose
    private String cStat;
    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("earnings")
    @Expose
    private String earnings;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_recommended")
    @Expose
    private String isRecommended;
    @SerializedName("booking_commission")
    @Expose
    private String bookingCommission;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("reg_no")
    @Expose
    private String regNo;
    @SerializedName("reg_cer")
    @Expose
    private String regCer;
    @SerializedName("clinic_address")
    @Expose
    private String clinicAddress;
    @SerializedName("clinic_address_one")
    @Expose
    private String clinicAddressOne;
    @SerializedName("clinic_address_two")
    @Expose
    private String clinicAddressTwo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pricing")
    @Expose
    private String pricing;
    @SerializedName("services")
    @Expose
    private String services;
    @SerializedName("degree")
    @Expose
    private String degree;
    @SerializedName("yearofcom")
    @Expose
    private String yearofcom;
    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("hos_name")
    @Expose
    private String hosName;
    @SerializedName("hos_address")
    @Expose
    private String hosAddress;
    @SerializedName("award")
    @Expose
    private Object award;
    @SerializedName("award_year")
    @Expose
    private Object awardYear;
    @SerializedName("membership")
    @Expose
    private Object membership;
    @SerializedName("registration")
    @Expose
    private String registration;
    @SerializedName("reg_year")
    @Expose
    private String regYear;
    @SerializedName("clinic_name")
    @Expose
    private String clinicName;
    @SerializedName("follow_up")
    @Expose
    private String followUp;
    @SerializedName("appointment_status")
    @Expose
    private String appointmentStatus;
    @SerializedName("languages")
    @Expose
    private List<Object> languages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneWithCode() {
        return phoneWithCode;
    }

    public void setPhoneWithCode(String phoneWithCode) {
        this.phoneWithCode = phoneWithCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getSubSpecialist() {
        return subSpecialist;
    }

    public void setSubSpecialist(String subSpecialist) {
        this.subSpecialist = subSpecialist;
    }

    public String getAdditionalQualification() {
        return additionalQualification;
    }

    public void setAdditionalQualification(String additionalQualification) {
        this.additionalQualification = additionalQualification;
    }

    public String getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(String noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public String getOverallRatings() {
        return overallRatings;
    }

    public void setOverallRatings(String overallRatings) {
        this.overallRatings = overallRatings;
    }

    public String getDocumentUpdateStatus() {
        return documentUpdateStatus;
    }

    public void setDocumentUpdateStatus(String documentUpdateStatus) {
        this.documentUpdateStatus = documentUpdateStatus;
    }

    public String getDocumentApprovedStatus() {
        return documentApprovedStatus;
    }

    public void setDocumentApprovedStatus(String documentApprovedStatus) {
        this.documentApprovedStatus = documentApprovedStatus;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcStat() {
        return cStat;
    }

    public void setcStat(String cStat) {
        this.cStat = cStat;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(String isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getBookingCommission() {
        return bookingCommission;
    }

    public void setBookingCommission(String bookingCommission) {
        this.bookingCommission = bookingCommission;
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

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getRegCer() {
        return regCer;
    }

    public void setRegCer(String regCer) {
        this.regCer = regCer;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getClinicAddressOne() {
        return clinicAddressOne;
    }

    public void setClinicAddressOne(String clinicAddressOne) {
        this.clinicAddressOne = clinicAddressOne;
    }

    public String getClinicAddressTwo() {
        return clinicAddressTwo;
    }

    public void setClinicAddressTwo(String clinicAddressTwo) {
        this.clinicAddressTwo = clinicAddressTwo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getYearofcom() {
        return yearofcom;
    }

    public void setYearofcom(String yearofcom) {
        this.yearofcom = yearofcom;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getHosAddress() {
        return hosAddress;
    }

    public void setHosAddress(String hosAddress) {
        this.hosAddress = hosAddress;
    }

    public Object getAward() {
        return award;
    }

    public void setAward(Object award) {
        this.award = award;
    }

    public Object getAwardYear() {
        return awardYear;
    }

    public void setAwardYear(Object awardYear) {
        this.awardYear = awardYear;
    }

    public Object getMembership() {
        return membership;
    }

    public void setMembership(Object membership) {
        this.membership = membership;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRegYear() {
        return regYear;
    }

    public void setRegYear(String regYear) {
        this.regYear = regYear;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public List<Object> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Object> languages) {
        this.languages = languages;
    }

}
