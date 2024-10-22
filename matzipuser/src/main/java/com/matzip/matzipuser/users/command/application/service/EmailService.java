package com.matzip.matzipuser.users.command.application.service;

import com.matzip.matzipuser.exception.ErrorCode;
import com.matzip.matzipuser.exception.RestApiException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.matzip.matzipuser.exception.ErrorCode.EXPIRE_VERIFICATION_CODE;
import static com.matzip.matzipuser.exception.ErrorCode.SEND_MAIL_FAIL;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>();  // 인증 코드 저장
    private final Map<String, LocalDateTime> codeExpireTimes = new HashMap<>(); // 인증 코드 만료 시간 저장
    private final Map<String, Boolean> emailVerifiedMap = new HashMap<>(); // 인증 성공 상태 저장

    private static final int codeValidTime = 10; // 인증 코드 유효 시간 (10분)

    public String getVerificationCode(String email) {   // 테스트를 위해 필요
        return verificationCodes.get(email);
    }

    // 회원가입 인증코드 보내기
    public void sendSignUpEmail(String email, String name){
//        log.info("========인증코드 발송 서비스 - sendSignUpEmail : email: {}========", email);
        String subject = "[맛zip]회원가입 인증코드입니다.";
        String verificationCode = makeVerificationCode();
        verificationCodes.put(email, verificationCode);

        // 현재 시간에 10분을 더한 시간을 만료 시간으로 설정
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(codeValidTime);
        codeExpireTimes.put(email, expirationTime);

        // 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            String emailContent = String.format(
                    "%s님 안녕하세요.\n\n회원가입을 해주셔서 감사합니다.\n\n" +
                            "인증코드는 %s입니다.\n\n" +
                            "인증코드는 10분동안 유효하니 그 안에 인증해주세요.\n\n" +
                            "이 메일은 회신이 불가능합니다.",
                    name, verificationCode
            );
            message.setText(emailContent);

        try {
            mailSender.send(message);
//            log.info("회원가입 인증코드 이메일 발송 성공! 이메일: {}", email);
        } catch (Exception e) {
//            log.error("회원가입 인증코드 이메일 발송 실패! 이메일: {}", email, e);
            throw new RestApiException(SEND_MAIL_FAIL);
        }
    }

    // 인증코드 생성
    private String makeVerificationCode(){
        return UUID.randomUUID().toString().substring(0,6).toUpperCase();
    }

    // 인증코드 확인
    public boolean verifyEmailCode(String email, String code) {
//        log.info("========인증코드 확인 서비스 - verifyEmailCode========");
        String storedCode = verificationCodes.get(email);
        LocalDateTime expirationTime = codeExpireTimes.get(email);

        // 만료 시간 확인
        if (expirationTime != null && LocalDateTime.now().isAfter(expirationTime)) {
            // 만료 시간이 지나면 인증 실패 처리
//            log.info("인증 코드 만료! 이메일: {}, 시간 : {}", email, expirationTime);
            clearEmailVerificationStatus(email);
            throw new RestApiException(EXPIRE_VERIFICATION_CODE);
        }

        if (storedCode != null && storedCode.equals(code)) {
//            log.info("이메일 인증 성공! 이메일: {}", email);
            emailVerifiedMap.put(email, true); // 인증 성공 상태 저장
            return true;
        } else {
//            log.info("이메일 인증 실패! 이메일: {}, 입력된 코드: {}", email, code);
            return false;
        }
    }

    // 이메일 인증 여부 확인
    public boolean isEmailVerified(String email) {
        // emailVerifiedMap에 해당 이메일이 있으면(인증된 이메일이면) 그 값을 반환하고, 없으면 false 반환
        return emailVerifiedMap.getOrDefault(email, false); // 인증 여부 확인
    }

    // 인증이 완료되면 호출하여 인증 상태 삭제
    public void clearEmailVerificationStatus(String email) {
        verificationCodes.remove(email); // 인증 성공 시 맵에서 코드 제거
        codeExpireTimes.remove(email); // 코드 만료시간 제거
        emailVerifiedMap.remove(email); // 인증 성공 시 맵에서 인증여부 제거
    }

    // 비밀번호 재설정 토큰 url 보내기
    public void sendPasswordResetUrl(String email, String token) {
//        log.info("========비밀번호 재설정 url 이메일 전송 서비스 - sendPasswordResetUrl========");
        String baseUrl = "http://localhost:5173";

        String resetUrl = baseUrl + "/user/api/v1/auth/reset-password?pwtoken=" + token;
        String subject = "[맛zip] 비밀번호 재설정 링크 안내";
        String message = "<p>비밀번호 재설정을 위한 링크입니다:</p>" +
                "<p><a href='" + resetUrl + "'>여기</a> 를 클릭하여 비밀번호를 재설정하세요.</p>" +
                "<p>링크는 1시간 동안만 유효합니다.</p>" +
                "<p>이 메일은 회신이 불가능합니다.</p>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true); // HTML 형식으로 전송

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RestApiException(ErrorCode.SEND_MAIL_FAIL);
        }

    }
}
