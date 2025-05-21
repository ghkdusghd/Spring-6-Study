package io.raon.web.example.error;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
	private String password;
}