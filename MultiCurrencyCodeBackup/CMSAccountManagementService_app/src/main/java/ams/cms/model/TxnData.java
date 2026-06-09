package ams.cms.model;

import java.io.Serializable;



import com.fasterxml.jackson.annotation.JsonInclude;



@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TxnData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String de002_pan;
	private String de003_processing_code;
	private String de004_amount;
	private String de005_settle_amount;
	private String de007_transaction_date;
	private String de009_conversion_rate_settle;
	private String de011_stan;
	private String de012_local_time;
	private String de013_local_date;
	private String de014_expiry_date;
	private String de015_settlement_date;
	private String de016_conversion_date;
	private String de018_mcc;
	private String de019_acquiring_currency_code;
	private String de022_pos_entry_mode;
	private String de023_card_sequence_nr;
	private String de024_nii;
	private String de025_pos_condition_code;
	private String de026_tran_fee_amount;
	private String de027_tran_fee_settle;
	private String de032_acquirer_inst_id;
	private String de033_forward_inst_id;
	private String de035_track_2;
	private String de037_rrn;
	private String de038_auth_code;
	private String de039_response;
	private String de040_service_restriction_code;
	private String de041_terminal_id;
	private String de042_mid;
	private String de043_merchant_name_loc;
	private String de044_additional_rsp_data;
	private String de045_track_1;
	private String de048_additional_data;
	private String de049_tran_currency_code;
	private String de050_settle_currency_code;
	private String de052_pin_data;
	private String de053_security_control_info;
	private String de054_additional_amount;
	private String de055_emv;
	private String de056_message_reason_code;
	private String de057_acquirer_rec_id;
	private String de058_network_data;
	private String de059_echo_data;
	private String de060_terminal_batch;
	private String de061_additional_pos_amount;
	private String de062_terminal_invoice;
	private String de063_total_req_rsp;
	private String de065_terminal_device_id;
	private String de066_terminal_imei;
	private String de067_terminal_mobile_number;
	private String de068_terminal_encrypted_pan;
	private String de069_retention_data;
	private String de070_network_management_code;
	private String de090_original_data;
	private String de095_replacement_amount;
	private String de102_from_account_numer;
	private String de103_to_account_number;
	private String de104_terminal_geaographical_data;
	private String de120_pos_data_code;
	private String de121_reserve_field;
	private String de122_text_field;
	private String de126_switch_private_data;
	private String de127_private_data;
	private String sys_id;
	private String tran_type;
	private String from_account;
	private String to_account;
	private String switch_terminal_settle_id;
	private String switch_terminal_batch_nr;
	private long txn_id;
	private String previous_txn_id;
	private String next_txn_id;
	//private Integer participant_id;
	private String participant_id;
	private String sponsor_code;
	private String source_node;
	private String destination_node;
	private String destination_config;
	private String network_name;
	private String reversal_attempt_count;
	private String instrument;
	private String sms_dms_type;
	private String is_international;
	// CARD/UPI/QRCODE/IMPS/ ->by device
	private String instrumentType;
	private String sessionKey;
	private String otp;
	private String payerMobileNo;
	private String ksn;
	private String ksn1;
	private String is_two_ksn;
	// UPI FIELD
	private String tranId;
	private String msgId;
	private String networkTxnReqDateTime;
	private String networkTxnResDateTime;
	private String loadBalanceId;
	private String switchInsertDateTime;

	private String bankTxnRefNo;
	private String bankTxnDate;
	private String bankRrn;
	private String bankStan;
	private String bankAuthcode;
	private String fromAccountName;
	private String bankResCode;
	private String bankTxnStatus;
	private String bankTxnDesc;
	private String prodType;
	private String refId;
	private String payeeInstrument;
	private String payerInstrument;
	private String payeeMobileNo;
	private String payerName;
	private String subMid;
	private String minAmount;
	private String expirePeriod;
	private String seqNo;
	private String payeeEmailId;
	private String payerEmailId;
	private String bankAlias;
	private String pincode;
	private String note;
	private String cardProduct;
	private String cardDesc;
	private String cardBankName;
	private String cardBin;
	private String cardMasked;
	private String cardType;
	private String isInternationalCard;
	private String isAccountType;
	
	private String accountType;
	private String accountNumber;
	private String channel;
	private String channelCode;
	private String availableBalance;
	private String accountCategory;
	private String loadBalanceCount;
	private String linkedGLAccounType;
	private String accountHolderName;
	private String accountHolderEmailId;
	
	public String getIsAccountType() {
		return isAccountType;
	}
	public void setIsAccountType(String isAccountType) {
		this.isAccountType = isAccountType;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	
	public String getDe002_pan() {
		return de002_pan;
	}
	public void setDe002_pan(String de002_pan) {
		this.de002_pan = de002_pan;
	}
	public String getDe003_processing_code() {
		return de003_processing_code;
	}
	public void setDe003_processing_code(String de003_processing_code) {
		this.de003_processing_code = de003_processing_code;
	}
	public String getDe004_amount() {
		return de004_amount;
	}
	public void setDe004_amount(String de004_amount) {
		this.de004_amount = de004_amount;
	}
	public String getDe005_settle_amount() {
		return de005_settle_amount;
	}
	public void setDe005_settle_amount(String de005_settle_amount) {
		this.de005_settle_amount = de005_settle_amount;
	}
	public String getDe007_transaction_date() {
		return de007_transaction_date;
	}
	public void setDe007_transaction_date(String de007_transaction_date) {
		this.de007_transaction_date = de007_transaction_date;
	}
	public String getDe009_conversion_rate_settle() {
		return de009_conversion_rate_settle;
	}
	public void setDe009_conversion_rate_settle(String de009_conversion_rate_settle) {
		this.de009_conversion_rate_settle = de009_conversion_rate_settle;
	}
	public String getDe011_stan() {
		return de011_stan;
	}
	public void setDe011_stan(String de011_stan) {
		this.de011_stan = de011_stan;
	}
	public String getDe012_local_time() {
		return de012_local_time;
	}
	public void setDe012_local_time(String de012_local_time) {
		this.de012_local_time = de012_local_time;
	}
	public String getDe013_local_date() {
		return de013_local_date;
	}
	public void setDe013_local_date(String de013_local_date) {
		this.de013_local_date = de013_local_date;
	}
	public String getDe014_expiry_date() {
		return de014_expiry_date;
	}
	public void setDe014_expiry_date(String de014_expiry_date) {
		this.de014_expiry_date = de014_expiry_date;
	}
	public String getDe015_settlement_date() {
		return de015_settlement_date;
	}
	public void setDe015_settlement_date(String de015_settlement_date) {
		this.de015_settlement_date = de015_settlement_date;
	}
	public String getDe016_conversion_date() {
		return de016_conversion_date;
	}
	public void setDe016_conversion_date(String de016_conversion_date) {
		this.de016_conversion_date = de016_conversion_date;
	}
	public String getDe018_mcc() {
		return de018_mcc;
	}
	public void setDe018_mcc(String de018_mcc) {
		this.de018_mcc = de018_mcc;
	}
	public String getDe019_acquiring_currency_code() {
		return de019_acquiring_currency_code;
	}
	public void setDe019_acquiring_currency_code(String de019_acquiring_currency_code) {
		this.de019_acquiring_currency_code = de019_acquiring_currency_code;
	}
	public String getDe022_pos_entry_mode() {
		return de022_pos_entry_mode;
	}
	public void setDe022_pos_entry_mode(String de022_pos_entry_mode) {
		this.de022_pos_entry_mode = de022_pos_entry_mode;
	}
	public String getDe023_card_sequence_nr() {
		return de023_card_sequence_nr;
	}
	public void setDe023_card_sequence_nr(String de023_card_sequence_nr) {
		this.de023_card_sequence_nr = de023_card_sequence_nr;
	}
	public String getDe024_nii() {
		return de024_nii;
	}
	public void setDe024_nii(String de024_nii) {
		this.de024_nii = de024_nii;
	}
	public String getDe025_pos_condition_code() {
		return de025_pos_condition_code;
	}
	public void setDe025_pos_condition_code(String de025_pos_condition_code) {
		this.de025_pos_condition_code = de025_pos_condition_code;
	}
	public String getDe026_tran_fee_amount() {
		return de026_tran_fee_amount;
	}
	public void setDe026_tran_fee_amount(String de026_tran_fee_amount) {
		this.de026_tran_fee_amount = de026_tran_fee_amount;
	}
	public String getDe027_tran_fee_settle() {
		return de027_tran_fee_settle;
	}
	public void setDe027_tran_fee_settle(String de027_tran_fee_settle) {
		this.de027_tran_fee_settle = de027_tran_fee_settle;
	}
	public String getDe032_acquirer_inst_id() {
		return de032_acquirer_inst_id;
	}
	public void setDe032_acquirer_inst_id(String de032_acquirer_inst_id) {
		this.de032_acquirer_inst_id = de032_acquirer_inst_id;
	}
	public String getDe033_forward_inst_id() {
		return de033_forward_inst_id;
	}
	public void setDe033_forward_inst_id(String de033_forward_inst_id) {
		this.de033_forward_inst_id = de033_forward_inst_id;
	}
	public String getDe035_track_2() {
		return de035_track_2;
	}
	public void setDe035_track_2(String de035_track_2) {
		this.de035_track_2 = de035_track_2;
	}
	public String getDe037_rrn() {
		return de037_rrn;
	}
	public void setDe037_rrn(String de037_rrn) {
		this.de037_rrn = de037_rrn;
	}
	public String getDe038_auth_code() {
		return de038_auth_code;
	}
	public void setDe038_auth_code(String de038_auth_code) {
		this.de038_auth_code = de038_auth_code;
	}
	public String getDe039_response() {
		return de039_response;
	}
	public void setDe039_response(String de039_response) {
		this.de039_response = de039_response;
	}
	public String getDe040_service_restriction_code() {
		return de040_service_restriction_code;
	}
	public void setDe040_service_restriction_code(String de040_service_restriction_code) {
		this.de040_service_restriction_code = de040_service_restriction_code;
	}
	public String getDe041_terminal_id() {
		return de041_terminal_id;
	}
	public void setDe041_terminal_id(String de041_terminal_id) {
		this.de041_terminal_id = de041_terminal_id;
	}
	public String getDe042_mid() {
		return de042_mid;
	}
	public void setDe042_mid(String de042_mid) {
		this.de042_mid = de042_mid;
	}
	public String getDe043_merchant_name_loc() {
		return de043_merchant_name_loc;
	}
	public void setDe043_merchant_name_loc(String de043_merchant_name_loc) {
		this.de043_merchant_name_loc = de043_merchant_name_loc;
	}
	public String getDe044_additional_rsp_data() {
		return de044_additional_rsp_data;
	}
	public void setDe044_additional_rsp_data(String de044_additional_rsp_data) {
		this.de044_additional_rsp_data = de044_additional_rsp_data;
	}
	public String getDe045_track_1() {
		return de045_track_1;
	}
	public void setDe045_track_1(String de045_track_1) {
		this.de045_track_1 = de045_track_1;
	}
	public String getDe048_additional_data() {
		return de048_additional_data;
	}
	public void setDe048_additional_data(String de048_additional_data) {
		this.de048_additional_data = de048_additional_data;
	}
	public String getDe049_tran_currency_code() {
		return de049_tran_currency_code;
	}
	public void setDe049_tran_currency_code(String de049_tran_currency_code) {
		this.de049_tran_currency_code = de049_tran_currency_code;
	}
	public String getDe050_settle_currency_code() {
		return de050_settle_currency_code;
	}
	public void setDe050_settle_currency_code(String de050_settle_currency_code) {
		this.de050_settle_currency_code = de050_settle_currency_code;
	}
	public String getDe052_pin_data() {
		return de052_pin_data;
	}
	public void setDe052_pin_data(String de052_pin_data) {
		this.de052_pin_data = de052_pin_data;
	}
	public String getDe053_security_control_info() {
		return de053_security_control_info;
	}
	public void setDe053_security_control_info(String de053_security_control_info) {
		this.de053_security_control_info = de053_security_control_info;
	}
	public String getDe054_additional_amount() {
		return de054_additional_amount;
	}
	public void setDe054_additional_amount(String de054_additional_amount) {
		this.de054_additional_amount = de054_additional_amount;
	}
	public String getDe055_emv() {
		return de055_emv;
	}
	public void setDe055_emv(String de055_emv) {
		this.de055_emv = de055_emv;
	}
	public String getDe056_message_reason_code() {
		return de056_message_reason_code;
	}
	public void setDe056_message_reason_code(String de056_message_reason_code) {
		this.de056_message_reason_code = de056_message_reason_code;
	}
	public String getDe057_acquirer_rec_id() {
		return de057_acquirer_rec_id;
	}
	public void setDe057_acquirer_rec_id(String de057_acquirer_rec_id) {
		this.de057_acquirer_rec_id = de057_acquirer_rec_id;
	}
	public String getDe058_network_data() {
		return de058_network_data;
	}
	public void setDe058_network_data(String de058_network_data) {
		this.de058_network_data = de058_network_data;
	}
	public String getDe059_echo_data() {
		return de059_echo_data;
	}
	public void setDe059_echo_data(String de059_echo_data) {
		this.de059_echo_data = de059_echo_data;
	}
	public String getDe060_terminal_batch() {
		return de060_terminal_batch;
	}
	public void setDe060_terminal_batch(String de060_terminal_batch) {
		this.de060_terminal_batch = de060_terminal_batch;
	}
	public String getDe061_additional_pos_amount() {
		return de061_additional_pos_amount;
	}
	public void setDe061_additional_pos_amount(String de061_additional_pos_amount) {
		this.de061_additional_pos_amount = de061_additional_pos_amount;
	}
	public String getDe062_terminal_invoice() {
		return de062_terminal_invoice;
	}
	public void setDe062_terminal_invoice(String de062_terminal_invoice) {
		this.de062_terminal_invoice = de062_terminal_invoice;
	}
	public String getDe063_total_req_rsp() {
		return de063_total_req_rsp;
	}
	public void setDe063_total_req_rsp(String de063_total_req_rsp) {
		this.de063_total_req_rsp = de063_total_req_rsp;
	}
	public String getDe065_terminal_device_id() {
		return de065_terminal_device_id;
	}
	public void setDe065_terminal_device_id(String de065_terminal_device_id) {
		this.de065_terminal_device_id = de065_terminal_device_id;
	}
	public String getDe066_terminal_imei() {
		return de066_terminal_imei;
	}
	public void setDe066_terminal_imei(String de066_terminal_imei) {
		this.de066_terminal_imei = de066_terminal_imei;
	}
	public String getDe067_terminal_mobile_number() {
		return de067_terminal_mobile_number;
	}
	public void setDe067_terminal_mobile_number(String de067_terminal_mobile_number) {
		this.de067_terminal_mobile_number = de067_terminal_mobile_number;
	}
	public String getDe068_terminal_encrypted_pan() {
		return de068_terminal_encrypted_pan;
	}
	public void setDe068_terminal_encrypted_pan(String de068_terminal_encrypted_pan) {
		this.de068_terminal_encrypted_pan = de068_terminal_encrypted_pan;
	}
	public String getDe069_retention_data() {
		return de069_retention_data;
	}
	public void setDe069_retention_data(String de069_retention_data) {
		this.de069_retention_data = de069_retention_data;
	}
	public String getDe070_network_management_code() {
		return de070_network_management_code;
	}
	public void setDe070_network_management_code(String de070_network_management_code) {
		this.de070_network_management_code = de070_network_management_code;
	}
	public String getDe090_original_data() {
		return de090_original_data;
	}
	public void setDe090_original_data(String de090_original_data) {
		this.de090_original_data = de090_original_data;
	}
	public String getDe095_replacement_amount() {
		return de095_replacement_amount;
	}
	public void setDe095_replacement_amount(String de095_replacement_amount) {
		this.de095_replacement_amount = de095_replacement_amount;
	}
	public String getDe102_from_account_numer() {
		return de102_from_account_numer;
	}
	public void setDe102_from_account_numer(String de102_from_account_numer) {
		this.de102_from_account_numer = de102_from_account_numer;
	}
	public String getDe103_to_account_number() {
		return de103_to_account_number;
	}
	public void setDe103_to_account_number(String de103_to_account_number) {
		this.de103_to_account_number = de103_to_account_number;
	}
	public String getDe104_terminal_geaographical_data() {
		return de104_terminal_geaographical_data;
	}
	public void setDe104_terminal_geaographical_data(String de104_terminal_geaographical_data) {
		this.de104_terminal_geaographical_data = de104_terminal_geaographical_data;
	}
	public String getDe120_pos_data_code() {
		return de120_pos_data_code;
	}
	public void setDe120_pos_data_code(String de120_pos_data_code) {
		this.de120_pos_data_code = de120_pos_data_code;
	}
	public String getDe121_reserve_field() {
		return de121_reserve_field;
	}
	public void setDe121_reserve_field(String de121_reserve_field) {
		this.de121_reserve_field = de121_reserve_field;
	}
	public String getDe122_text_field() {
		return de122_text_field;
	}
	public void setDe122_text_field(String de122_text_field) {
		this.de122_text_field = de122_text_field;
	}
	public String getDe126_switch_private_data() {
		return de126_switch_private_data;
	}
	public void setDe126_switch_private_data(String de126_switch_private_data) {
		this.de126_switch_private_data = de126_switch_private_data;
	}
	public String getDe127_private_data() {
		return de127_private_data;
	}
	public void setDe127_private_data(String de127_private_data) {
		this.de127_private_data = de127_private_data;
	}
	public String getSys_id() {
		return sys_id;
	}
	public void setSys_id(String sys_id) {
		this.sys_id = sys_id;
	}
	public String getTran_type() {
		return tran_type;
	}
	public void setTran_type(String tran_type) {
		this.tran_type = tran_type;
	}
	public String getFrom_account() {
		return from_account;
	}
	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}
	public String getTo_account() {
		return to_account;
	}
	public void setTo_account(String to_account) {
		this.to_account = to_account;
	}
	/*
	public long getSwitch_terminal_settle_id() {
		return switch_terminal_settle_id;
	}
	public void setSwitch_terminal_settle_id(long switch_terminal_settle_id) {
		this.switch_terminal_settle_id = switch_terminal_settle_id;
	}
	public long getSwitch_terminal_batch_nr() {
		return switch_terminal_batch_nr;
	}
	public void setSwitch_terminal_batch_nr(long switch_terminal_batch_nr) {
		this.switch_terminal_batch_nr = switch_terminal_batch_nr;
	}
	*/
	public long getTxn_id() {
		return txn_id;
	}
	public void setTxn_id(long txn_id) {
		this.txn_id = txn_id;
	}
	/*
	public long getPrevious_txn_id() {
		return previous_txn_id;
	}
	public void setPrevious_txn_id(long previous_txn_id) {
		this.previous_txn_id = previous_txn_id;
	}
	public long getNext_txn_id() {
		return next_txn_id;
	}
	public void setNext_txn_id(long next_txn_id) {
		this.next_txn_id = next_txn_id;
	}
	*/
	
	public String getSwitch_terminal_settle_id() {
		return switch_terminal_settle_id;
	}
	public void setSwitch_terminal_settle_id(String switch_terminal_settle_id) {
		this.switch_terminal_settle_id = switch_terminal_settle_id;
	}
	public String getSwitch_terminal_batch_nr() {
		return switch_terminal_batch_nr;
	}
	public void setSwitch_terminal_batch_nr(String switch_terminal_batch_nr) {
		this.switch_terminal_batch_nr = switch_terminal_batch_nr;
	}
	public String getPrevious_txn_id() {
		return previous_txn_id;
	}
	public void setPrevious_txn_id(String previous_txn_id) {
		this.previous_txn_id = previous_txn_id;
	}
	public String getNext_txn_id() {
		return next_txn_id;
	}
	public void setNext_txn_id(String next_txn_id) {
		this.next_txn_id = next_txn_id;
	}

	/*
	 public Integer getParticipant_id(){ 
	 return participant_id; 
	 }
	  public void setParticipant_id(Integer participant_id) { this.participant_id =
	  participant_id; }
	 */
	public String getSponsor_code() {
		return sponsor_code;
	}
	public void setSponsor_code(String sponsor_code) {
		this.sponsor_code = sponsor_code;
	}
	public String getSource_node() {
		return source_node;
	}
	public void setSource_node(String source_node) {
		this.source_node = source_node;
	}
	public String getDestination_node() {
		return destination_node;
	}
	public void setDestination_node(String destination_node) {
		this.destination_node = destination_node;
	}
	public String getDestination_config() {
		return destination_config;
	}
	public void setDestination_config(String destination_config) {
		this.destination_config = destination_config;
	}
	public String getNetwork_name() {
		return network_name;
	}
	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}
	/*
	public int getReversal_attempt_count() {
		return reversal_attempt_count;
	}
	public void setReversal_attempt_count(int reversal_attempt_count) {
		this.reversal_attempt_count = reversal_attempt_count;
	}
	 */
	public String getInstrument() {
		return instrument;
	}
	public String getReversal_attempt_count() {
		return reversal_attempt_count;
	}
	public void setReversal_attempt_count(String reversal_attempt_count) {
		this.reversal_attempt_count = reversal_attempt_count;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getSms_dms_type() {
		return sms_dms_type;
	}
	public void setSms_dms_type(String sms_dms_type) {
		this.sms_dms_type = sms_dms_type;
	}
	public String getIs_international() {
		return is_international;
	}
	public void setIs_international(String is_international) {
		this.is_international = is_international;
	}
	public String getInstrumentType() {
		return instrumentType;
	}
	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getPayerMobileNo() {
		return payerMobileNo;
	}
	public void setPayerMobileNo(String payerMobileNo) {
		this.payerMobileNo = payerMobileNo;
	}
	public String getKsn() {
		return ksn;
	}
	public void setKsn(String ksn) {
		this.ksn = ksn;
	}
	public String getKsn1() {
		return ksn1;
	}
	public void setKsn1(String ksn1) {
		this.ksn1 = ksn1;
	}
	public String getIs_two_ksn() {
		return is_two_ksn;
	}
	public void setIs_two_ksn(String is_two_ksn) {
		this.is_two_ksn = is_two_ksn;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getNetworkTxnReqDateTime() {
		return networkTxnReqDateTime;
	}
	public void setNetworkTxnReqDateTime(String networkTxnReqDateTime) {
		this.networkTxnReqDateTime = networkTxnReqDateTime;
	}
	public String getNetworkTxnResDateTime() {
		return networkTxnResDateTime;
	}
	public void setNetworkTxnResDateTime(String networkTxnResDateTime) {
		this.networkTxnResDateTime = networkTxnResDateTime;
	}
	public String getLoadBalanceId() {
		return loadBalanceId;
	}
	public void setLoadBalanceId(String loadBalanceId) {
		this.loadBalanceId = loadBalanceId;
	}
	public String getSwitchInsertDateTime() {
		return switchInsertDateTime;
	}
	public void setSwitchInsertDateTime(String switchInsertDateTime) {
		this.switchInsertDateTime = switchInsertDateTime;
	}
	public String getBankTxnRefNo() {
		return bankTxnRefNo;
	}
	public void setBankTxnRefNo(String bankTxnRefNo) {
		this.bankTxnRefNo = bankTxnRefNo;
	}
	public String getBankTxnDate() {
		return bankTxnDate;
	}
	public void setBankTxnDate(String bankTxnDate) {
		this.bankTxnDate = bankTxnDate;
	}
	public String getBankRrn() {
		return bankRrn;
	}
	public void setBankRrn(String bankRrn) {
		this.bankRrn = bankRrn;
	}
	public String getBankStan() {
		return bankStan;
	}
	public void setBankStan(String bankStan) {
		this.bankStan = bankStan;
	}
	public String getBankAuthcode() {
		return bankAuthcode;
	}
	public void setBankAuthcode(String bankAuthcode) {
		this.bankAuthcode = bankAuthcode;
	}
	public String getFromAccountName() {
		return fromAccountName;
	}
	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}
	public String getBankResCode() {
		return bankResCode;
	}
	public void setBankResCode(String bankResCode) {
		this.bankResCode = bankResCode;
	}
	public String getBankTxnStatus() {
		return bankTxnStatus;
	}
	public void setBankTxnStatus(String bankTxnStatus) {
		this.bankTxnStatus = bankTxnStatus;
	}
	public String getBankTxnDesc() {
		return bankTxnDesc;
	}
	public void setBankTxnDesc(String bankTxnDesc) {
		this.bankTxnDesc = bankTxnDesc;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getPayeeInstrument() {
		return payeeInstrument;
	}
	public void setPayeeInstrument(String payeeInstrument) {
		this.payeeInstrument = payeeInstrument;
	}
	public String getPayerInstrument() {
		return payerInstrument;
	}
	public void setPayerInstrument(String payerInstrument) {
		this.payerInstrument = payerInstrument;
	}
	public String getPayeeMobileNo() {
		return payeeMobileNo;
	}
	public void setPayeeMobileNo(String payeeMobileNo) {
		this.payeeMobileNo = payeeMobileNo;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getSubMid() {
		return subMid;
	}
	public void setSubMid(String subMid) {
		this.subMid = subMid;
	}
	public String getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}
	public String getExpirePeriod() {
		return expirePeriod;
	}
	public void setExpirePeriod(String expirePeriod) {
		this.expirePeriod = expirePeriod;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getPayeeEmailId() {
		return payeeEmailId;
	}
	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}
	public String getPayerEmailId() {
		return payerEmailId;
	}
	public void setPayerEmailId(String payerEmailId) {
		this.payerEmailId = payerEmailId;
	}
	public String getBankAlias() {
		return bankAlias;
	}
	public void setBankAlias(String bankAlias) {
		this.bankAlias = bankAlias;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCardProduct() {
		return cardProduct;
	}
	public void setCardProduct(String cardProduct) {
		this.cardProduct = cardProduct;
	}
	public String getCardDesc() {
		return cardDesc;
	}
	public void setCardDesc(String cardDesc) {
		this.cardDesc = cardDesc;
	}
	public String getCardBankName() {
		return cardBankName;
	}
	public void setCardBankName(String cardBankName) {
		this.cardBankName = cardBankName;
	}
	public String getCardBin() {
		return cardBin;
	}
	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
	}
	public String getCardMasked() {
		return cardMasked;
	}
	public void setCardMasked(String cardMasked) {
		this.cardMasked = cardMasked;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIsInternationalCard() {
		return isInternationalCard;
	}
	public void setIsInternationalCard(String isInternationalCard) {
		this.isInternationalCard = isInternationalCard;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getAccountCategory() {
		return accountCategory;
	}
	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}
	public String getLoadBalanceCount() {
		return loadBalanceCount;
	}
	public void setLoadBalanceCount(String loadBalanceCount) {
		this.loadBalanceCount = loadBalanceCount;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getLinkedGLAccounType() {
		return linkedGLAccounType;
	}
	public void setLinkedGLAccounType(String linkedGLAccounType) {
		this.linkedGLAccounType = linkedGLAccounType;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public String getAccountHolderEmailId() {
		return accountHolderEmailId;
	}
	public void setAccountHolderEmailId(String accountHolderEmailId) {
		this.accountHolderEmailId = accountHolderEmailId;
	}
	
	public String getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(String participant_id) {
		this.participant_id = participant_id;
	}
	
}