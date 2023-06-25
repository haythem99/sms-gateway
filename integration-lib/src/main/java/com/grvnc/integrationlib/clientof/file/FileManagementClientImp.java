package com.grvnc.integrationlib.clientof.file;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.grvnc.sharedlib.viewobj.ResponseVO;

public interface FileManagementClientImp {

	@PostMapping("/file-management/api/v3/internal/file-management/confirm-file-in-use")
	public ResponseVO onfirmFIlesInUse(@RequestBody List<String> encryptedFilesIds);


}