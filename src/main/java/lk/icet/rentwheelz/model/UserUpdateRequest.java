package lk.icet.rentwheelz.model;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String contactNumber;
    private String address;
}