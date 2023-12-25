package com.example.storyteller.service;

import com.example.storyteller.dto.AuthenticationRequest;
import com.example.storyteller.dto.AuthenticationResponse;
import com.example.storyteller.dto.RegisterRequest;
import com.example.storyteller.entity.ConfirmationToken;
import com.example.storyteller.entity.Role;
import com.example.storyteller.entity.User;
import com.example.storyteller.exception.CustomException;
import com.example.storyteller.utils.EmailValidator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final static String USER_EXISTS_MSG =
            "使用者 %s 已註冊過";

    private final static String Email_Link = "http://aazz282828-alps:8080/api/v1/auth/confirm?token=%s";

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    public boolean isValidPassword(String password) {
        Pattern englishPattern = Pattern.compile("[a-zA-Z]");
        Pattern digitPattern = Pattern.compile("\\d");

        boolean hasEnglish = englishPattern.matcher(password).find();
        boolean hasDigits = digitPattern.matcher(password).find();
        boolean hasValidLength = password.length() >= 6;

        return hasEnglish && hasDigits && hasValidLength;
    }

    @Transactional
    public AuthenticationResponse register(@NotNull RegisterRequest request) {

        if (userService.existsByName(request.getName())) {
            throw new CustomException("ACCOUNT_EXISTS", String.format(USER_EXISTS_MSG, request.getName()));
        }

        if (userService.existsByEmail(request.getEmail())) {
            throw new CustomException("EMAIL_EXISTS", String.format(USER_EXISTS_MSG, request.getEmail()));
        }

        if (!isValidPassword(request.getPassword())) {
            throw new CustomException("INVALID_PASSWORD", "密碼錯誤");
        }

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            throw new CustomException("INVALID_EMAIL", "信箱格式錯誤");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.save(user);

        String jwtToken = jwtService.generateToken(user);

        ConfirmationToken confirmationToken = ConfirmationToken
            .builder()
            .token(jwtToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .user(user)
            .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = String.format(Email_Link, jwtToken);

        emailService.send(request.getEmail(), buildEmail(request.getName(), link));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElse(null);
//                .orElseThrow(() ->
//                        new CustomException("TOKEN_NOT_FOUND", "未找到token"));

        if (confirmationToken == null) {
            return "未找到token";
        }

        if (confirmationToken.getConfirmedAt() != null) {
//            throw new CustomException("EMAIL_IS_CONFIRMED", "信箱已註冊過");
            return "該信箱已註冊過";
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
//            throw new CustomException("TOKEN_EXPIRED", "");
            return "token已過期";
        }

        confirmationTokenService.setConfirmedAt(token);

        userService.enableUser(
                confirmationToken.getUser().getEmail());

        return "電子郵件認證成功";
    }

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("EMAIL_NOT_FOUND", "信箱未找到"));

        if (!user.isEnabled()) {
            throw new CustomException("EMAIL_INVALID", "您的帳戶需要進行電子郵件驗證。請檢查您的郵件信箱!");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new CustomException("INVALID_CREDENTIALS", "無效的認證資訊");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .expiresIn(1)
                .token(jwtToken)
                .build();
    }

    @Contract(pure = true)
    private @NotNull String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">您好 " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> \n 感謝您在「說書人」網站註冊帳戶。為了啟用您的帳戶，請點擊下方連結： </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">立即啟用</a> </p></blockquote>\n 連結將在 15 分鐘後過期。 <p>期待與您再會</p>\n <p> 說書人團隊 </p> \n <p> (請勿回覆此郵件，如有任何疑問，請聯繫我們的客服人員。) </p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}