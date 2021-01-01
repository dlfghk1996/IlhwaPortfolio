package com.kim.ilhwaportfolio.helper;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSend {

	// Properties 클래스 : 시스템의 속성을 객체로 생성하는 클래스  ( key=value 형식 )
	// 수신자 메일
	String recipient;
	// Text : 내용
	String tempPw;
	
	public MailSend(String recipient,String tempPw) {
		this.recipient= recipient;
		this.tempPw = tempPw;
		System.out.println("MailSend() 생성자 생성 과 동시에 메일이 발송됩니다." + "받는사람 : " + this.recipient + " 보내는 메세지 : " + this.tempPw);
		Properties prop = System.getProperties();
		// com.sun.mail.smtp 패키지
		// 아래 설정은 TLS와 SSL의 사용에 따라 설정값이 다른데 아래 코드는 TLS의 경우
		// 로그인 시 TLS(=SSL 암호화 통신) 를 사용 할 것 인지 설정
		prop.put("mail.smtp.starttls.enable", "true");
		// 이메일 발송을 처리해줄SMTP서버
		prop.put("mail.smtp.host", "smtp.gmail.com");
		// SMTP 서버의 인증을 사용한다는 의미
		prop.put("mail.smtp.auth","true");
		// TLS 의 포트 번호
		prop.put("mail.smtp.port","587");
		
		// Authenticator : 인증클래스
		// 발송자 비밀번호 인증
		Authenticator auth = new Authenticator() {
	            @Override
	            public PasswordAuthentication getPasswordAuthentication() {
	                // 상수로 정의해 둔 Gmail 메일 주소와 비밀번호를 인증객체로 묶어서 리턴한다.
	                return new PasswordAuthentication("ilhwakim1996@gmail.com", "nlqg iwjc jyou ixjg");
	            }
	    };
	    // 세션 생성
		Session session = Session.getDefaultInstance(prop, auth);
		
		// MimeMessage : 인터넷 메일을 위한 클래스
		MimeMessage msg = new MimeMessage(session);
		
		try {
			// 보내는 날짜 지정
			msg.setSentDate(new Date());
			// 발송자 지정
			msg.setFrom(new InternetAddress("ilhwakim1996@gmail.com","nextLineCompany"));

			// 수신자 메일 생성
			InternetAddress to = new InternetAddress(this.recipient);
			// 수신자 설정 (여러 사람일 경우 setRecipients)
			msg.setRecipient(Message.RecipientType.TO, to);
			// 메일 제목 
			msg.setSubject("[ NextLine ] 비밀번호 찾기 : 임시비밀번호를 확인해주세요. ");
			// 메일 내용 입력
			msg.setText(this.tempPw,"UTF-8");
			// 최종적으로 메일 발송
			// Transport : 최종적으로 메일 보내는 클래스
			Transport.send(msg);
		} catch (MessagingException e) {
			// MessagingException - 메일 계정인증 관련 예외 처리
			System.out.println("[ 메일계정 인증 관련 예외 처리 ]" + e.getMessage());
		}catch (UnsupportedEncodingException e) {
			// UnsupportedEncodingException - 지원되지 않는 인코딩을 사용할 경우 예외 처리
			System.out.println("[ 지원하지 않는 인코딩 ]" + e.getMessage());
				e.printStackTrace();
			}
		}	
	}

