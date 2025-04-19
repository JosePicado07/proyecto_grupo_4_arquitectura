package models;

import java.time.LocalDate;
import java.util.Objects;



public class User {
    public static enum UserType {USER, ADMIN, SELLER};

    private String cedula;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String password;
    private UserType userType;

    public User(String name, String firstLastName, String secondLastName, String email, String phone, String address, LocalDate birthDate, String password, UserType userType) {
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.password = password;
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "cedula='" + cedula + '\'' +
                ", name='" + name + '\'' +
                ", firstLastName='" + firstLastName + '\'' +
                ", secondLastName='" + secondLastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(getCedula(), user.getCedula()) && Objects.equals(getName(), user.getName()) && Objects.equals(getFirstLastName(), user.getFirstLastName()) && Objects.equals(getSecondLastName(), user.getSecondLastName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPhone(), user.getPhone()) && Objects.equals(getAddress(), user.getAddress()) && Objects.equals(getBirthDate(), user.getBirthDate()) && Objects.equals(getPassword(), user.getPassword()) && getUserType() == user.getUserType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCedula(), getName(), getFirstLastName(), getSecondLastName(), getEmail(), getPhone(), getAddress(), getBirthDate(), getPassword(), getUserType());
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public User(String cedula, String name, String firstLastName, String secondLastName, String email, String phone, String address, LocalDate birthDate, String password, UserType userType) {
        this.cedula = cedula;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.password = password;
        this.userType = userType;
    }

    public User(String name, String firstLastName, String secondLastName, String email, String phone, String address, LocalDate birthDate, String password) {
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.password = password;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validateUser (String email, String password){
        return this.getEmail().equals(email) && this.getPassword().equals(password);
    }

}
