package lk.icet.rentwheelz.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String contactNumber;
    private String address;
}
