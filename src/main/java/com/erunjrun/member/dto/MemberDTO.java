package com.erunjrun.member.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDTO {

	@NotNull(message = "아이디는 필수 입력 항목입니다.")
	@Size(min = 3, max = 50, message = "아이디는 3자 이상 50자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영어와 숫자만 포함할 수 있습니다.")
    private String id;
	
	@NotNull(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\\W_]).{8,}$", message = "비밀번호는 숫자, 영문, 특수문자를 포함하여 8자 이상이어야 합니다.")
    private String pw;
	
	@NotNull(message = "이름은 필수 입력 항목입니다.")
    private String name;
    
    @NotNull(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 50, message = "닉네임은 3자 이상 50자 이하로 입력하세요.")
    private String nickname;
    
    private String address;
    
    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{8}$", message = "생년월일은 8자리 숫자로 입력해야 합니다.")
    private Date birth;
    
    @NotNull(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;
    
    @Pattern(regexp = "^(M|F)$", message = "성별은 'M' 또는 'F'로 입력해야 합니다.")
    private String gender;
    
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-xxxx-xxxx 형식으로 입력해야 합니다.")
    private String phone;

    // getter, setter
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getPw() {
        return pw;
    }
    
    public void setPw(String pw) {
        this.pw = pw;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Date getBirth() {
        return birth;
    }
    
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    
    public String getFormattedBirth() { // 포맷된 생년월일 반환
        if (birth != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return dateFormat.format(birth);
        }
        return null; // 생년월일이 없을 경우 null 반환
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
}
