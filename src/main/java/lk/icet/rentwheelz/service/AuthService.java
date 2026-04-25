package lk.icet.rentwheelz.service;

import lk.icet.rentwheelz.model.AuthResponse;
import lk.icet.rentwheelz.model.LoginRequest;
import lk.icet.rentwheelz.model.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
