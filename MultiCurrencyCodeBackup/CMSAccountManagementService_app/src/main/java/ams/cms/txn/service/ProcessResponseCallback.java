package ams.cms.txn.service;

import org.springframework.http.ResponseEntity;

public interface ProcessResponseCallback {
	void sendResponse(ResponseEntity<?> responseEntity);
}
