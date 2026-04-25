package lk.icet.rentwheelz.model;

import lk.icet.rentwheelz.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String fullName;
    private String email;
    private String password;
    private UserRole role;
    private String contactNumber;
    private String address;
    private Timestamp createdAt;
    private String status;
}
