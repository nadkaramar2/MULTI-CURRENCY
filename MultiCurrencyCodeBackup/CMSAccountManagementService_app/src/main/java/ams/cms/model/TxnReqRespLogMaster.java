
package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "transaction_request_response_log")
public class TxnReqRespLogMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "participant_id")
	private String participantId;
	
	@Column(name = "montra_txn_id")
	private String montraTxnId;
	
	@Column(name = "txn_endpoint")
	private String txnEndpoint;
	
	@Column(name = "txn_status")
	private String txnStatus;
	
	@Column(name = "txn_id")
	private String txnId;
	
	@Column(name = "txn_request")
	private String txnRequest;
	
	@Column(name = "txn_response")
	private String txnResponse;
	
	@Column(name = "req_txn_date")
	private Date reqTxnDate;
	
	@Column(name = "res_txn_date")
	private Date resTxnDate;
	
	@Column(name = "api_key")
	private String apiKey;
	
	@Column(name = "secret_key")
	private String secretKey;
	
	@Column(name = "api_hash")
	private String apiHash;
	
	@Column(name = "req_payload")
	private String reqPayload;
	
	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnRequest() {
		return txnRequest;
	}

	public void setTxnRequest(String txnRequest) {
		this.txnRequest = txnRequest;
	}

	public String getTxnResponse() {
		return txnResponse;
	}

	public void setTxnResponse(String txnResponse) {
		this.txnResponse = txnResponse;
	}

	public Date getReqTxnDate() {
		return reqTxnDate;
	}

	public void setReqTxnDate(Date reqTxnDate) {
		this.reqTxnDate = reqTxnDate;
	}

	public Date getResTxnDate() {
		return resTxnDate;
	}

	public String getMontraTxnId() {
		return montraTxnId;
	}

	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}

	public void setResTxnDate(Date resTxnDate) {
		this.resTxnDate = resTxnDate;
	}

	public String getTxnEndpoint() {
		return txnEndpoint;
	}

	public void setTxnEndpoint(String txnEndpoint) {
		this.txnEndpoint = txnEndpoint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getApiHash() {
		return apiHash;
	}

	public void setApiHash(String apiHash) {
		this.apiHash = apiHash;
	}

	public String getReqPayload() {
		return reqPayload;
	}

	public void setReqPayload(String reqPayload) {
		this.reqPayload = reqPayload;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
}
