package com.erunjrun.member.service;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	Logger logger = LoggerFactory.getLogger(getClass());

	// 설정(SMTP 서버의 정보로, 이메일을 전송할 때 필요한 인증 정보).
	private static final String SENDER_EMAIL = "toastgamehq@gmail.com"; // 발신자 이메일
	private static final String SMTP_HOST = "smtp.gmail.com"; // SMTP 서버 주소
	private static final int SMTP_PORT = 587; // SMTP 서버 포트
	private static final String SMTP_USER = "toastgamehq@gmail.com"; // 이메일 계정 (사용자)
	private static final String SMTP_PASSWORD = "sddp qbkf zerb goyn"; // 이메일 비밀번호 (비밀)

	public void sendPwMail(String email, String tempPw) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // JavaMailSenderImpl을 사용하여 이메일을 보낼 준비
		mailSender.setHost(SMTP_HOST);
		mailSender.setPort(SMTP_PORT);
		mailSender.setUsername(SMTP_USER);
		mailSender.setPassword(SMTP_PASSWORD);

		// 객체에서 SMTP 서버와의 연결 설정을 지정.
		mailSender.getJavaMailProperties().put("mail.smtp.auth", "true"); // SMTP 서버 인증을 활성화.
		mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true"); // TLS(전송 계층 보안) 프로토콜을 활성화.
		mailSender.getJavaMailProperties().put("mail.smtp.timeout", "5000"); // 서버와의 연결 타임아웃을 설정.

		MimeMessage message = mailSender.createMimeMessage();

		try {
			message.setFrom(SENDER_EMAIL);
			message.setRecipients(MimeMessage.RecipientType.TO, email);
			message.setSubject("임시 비밀번호 안내");
			// 메일을 HTML 형식으로 보내는 설정.
			String body = "<h3>안녕하세요.</h3>";
			body += "<p>" + "요청하신 임시 비밀번호는 <strong>" + tempPw + "</strong>입니다." + "</p>";
			body += "<p>" + "로그인 후 반드시 비밀번호를 변경해주세요." + "</p>";
			body += "<p>" + "감사합니다." + "</p>";
			message.setText(body, "UTF-8", "html");
			mailSender.send(message);
			logger.info("임시 비밀번호 메일을 성공적으로 보냈습니다.");
		} catch (MessagingException e) {
			logger.error("메일 전송 실패", e);
		}
	}

	// 인증번호 전송 메서드
	public String sendVerificationCode(String email) {
		// 인증번호 생성
		String verificationCode = generateVerificationCode();

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // JavaMailSenderImpl을 사용하여 이메일을 보낼 준비
		mailSender.setHost(SMTP_HOST);
		mailSender.setPort(SMTP_PORT);
		mailSender.setUsername(SMTP_USER);
		mailSender.setPassword(SMTP_PASSWORD);

		// SMTP 서버와의 연결 설정을 지정.
		mailSender.getJavaMailProperties().put("mail.smtp.auth", "true"); // SMTP 서버 인증을 활성화.
		mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true"); // TLS 활성화.
		mailSender.getJavaMailProperties().put("mail.smtp.timeout", "5000"); // 연결 타임아웃 설정.

		MimeMessage message = mailSender.createMimeMessage();

		try {
			message.setFrom(SENDER_EMAIL);
			message.setRecipients(MimeMessage.RecipientType.TO, email);
			message.setSubject("이메일 인증번호");

			String body = "<h3>안녕하세요.</h3>";
			body += "<p>" + "귀하의 인증번호는 <strong>" + verificationCode + "</strong>입니다." + "</p>";
			body += "<p>" + "인증번호를 입력하여 회원가입을 완료해주세요." + "</p>";
			body += "<p>" + "감사합니다." + "</p>";
			message.setText(body, "UTF-8", "html");
			mailSender.send(message);
			logger.info("인증번호 메일을 성공적으로 보냈습니다.");
		} catch (MessagingException e) {
			logger.error("메일 전송 실패", e);
			return null; // 메일 전송 실패 시 null 반환
		}

		return verificationCode; // 성공적으로 메일을 보냈다면 인증번호 반환
	}
	
	// UUID로 인증번호 생성 (긴 UUID에서 앞부분 6자리만 사용)
    private String generateVerificationCode() {
        String uuid = UUID.randomUUID().toString(); // UUID 생성
        return uuid.substring(0, 6); // UUID의 앞 6자리만 사용하여 인증번호로 반환
    }
    
}