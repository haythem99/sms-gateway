package com.grvnc.integrationlib.clientof.notification;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSIVO {

	private long id;
	@NotBlank(message = "canNotMakeFieldBlank")
	private String senderNameAlias;
	@NotBlank(message = "canNotMakeFieldBlank")
	@Size(max = 2, min = 2, message = "languageCodeNotCorrectFormat")
	private String languageCode;
	@NotBlank(message = "canNotMakeFieldBlank")
	private String templateName;

	private List<String> templateArgs;

	//@NotBlank (message="canNotMakeFieldBlank")
	private List<@Size(max = 15, message = "enteredValueTooLong") @Size(min = 10, message = "enteredValueTooShort") @NotBlank @Pattern(regexp = "^\\+([0-9]*)", message = "mobileNumberNotCorrectFormat") String> toMobilesNumbersList;
	@Min(1)
	private int keepAliveInMinutes;

	private LocalDateTime creationDate = LocalDateTime.now();

}
