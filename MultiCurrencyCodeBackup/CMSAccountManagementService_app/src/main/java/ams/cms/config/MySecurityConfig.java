package ams.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ams.cms.jwt.JwtAuthenticationFilter;
//import ams.cms.jwt.MontraAuthenticationFilter;
import ams.cms.security.CustomUserDetailsService;
import ams.cms.security.EntryPointUnauthorizedHandler;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private EntryPointUnauthorizedHandler unauthorizedHandler;
	
	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	
	//@Autowired
	//MontraAuthenticationFilter montraAuthenticationFilter;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService; 
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		try
		{
			http.csrf().disable()
			.cors().disable()
			.exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler).and()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //Handling CORS Error
			//.antMatchers("/signup/**").permitAll()
			.antMatchers("/apiky/**").permitAll()
			//.antMatchers("/signIn/**").permitAll()
			.antMatchers("/verifyMobileNumber/**").permitAll()
			.antMatchers("/qrcode/**").permitAll()
			
			.antMatchers("/notify-by/**").permitAll()
			.antMatchers("/address_proof_document_type/**").permitAll()
			.antMatchers("/identity_proof_document_type/**").permitAll()
			.antMatchers("/channels/**").permitAll()
			
			.antMatchers("/account-txn-limit/**").permitAll()
			
			.antMatchers("/mcc-wise-interest/**").permitAll()
			.antMatchers("/card-account-linkage/**").permitAll()
			.antMatchers("/account-credit-card-txn/**").permitAll()
			
			.antMatchers("/account-wise-credit-interest/**").permitAll()
			.antMatchers("/preAccountMaster/**").permitAll()
			.antMatchers("/preSubAccountMaster/**").permitAll()
			.antMatchers("/account-wise-kyc-details/**").permitAll()
			
			.antMatchers("/credit_limit/**").permitAll()
			.antMatchers("/account-load/**").permitAll()
			.antMatchers("/account/**").permitAll()
			
			.antMatchers("/account-statement/**").permitAll()
			.antMatchers("/accountTxnMaster/**").permitAll()
			.antMatchers("/accountType/**").permitAll()
			
			.antMatchers("/blocked_mcc_account_type_wise/**").permitAll()
			.antMatchers("/account_type_wallet/**").permitAll()
			.antMatchers("/account_type_category/**").permitAll()
			
			.antMatchers("/category_type/**").permitAll()
			.antMatchers("/revolvingCreditCard/**").permitAll()
			.antMatchers("/accountType/**").permitAll()
			.antMatchers("/getChargeMasterList/**").permitAll()
			.antMatchers("/gl-account-load/**").permitAll()
			
			.antMatchers("/gl-account-type/**").permitAll()
			.antMatchers("/gl-account-type-creation/**").permitAll()
			.antMatchers("/instanceAccount/**").permitAll()
			.antMatchers("/mcc_code/**").permitAll()
			
			.antMatchers("/participant_wallet/**").permitAll()
			.antMatchers("/tax_type_config/**").permitAll()
			.antMatchers("/wallet_account/**").permitAll()			
			
			.antMatchers("/customerId/**").permitAll()
			
			.antMatchers("/customerIdTable/**").permitAll()
			
			.antMatchers("/accountTypeCharges/**").permitAll()
			
			.antMatchers("/chargeRelatedMaster/**").permitAll()
			
			.antMatchers("/transaction_type_master/**").permitAll()
			
			.antMatchers("/qrcode/**").permitAll() //Added for Qr Code Generation
			
			.antMatchers("/loginbyReact/**").permitAll()//Added for test puropose
			
			.antMatchers("/tranMaster/**").permitAll()
			
			.antMatchers("/transactionHandler/**").permitAll()
			
			.antMatchers("/image/**").permitAll()
			
			.antMatchers("/gl-account-statement/**").permitAll()
			
			.antMatchers("/txn/**").permitAll() //for testing purpose
			
			.antMatchers("/journalTransfer/**").permitAll()
			
			.antMatchers("/country_code_api/**").permitAll()
			
			.antMatchers("/tempBlock/**").permitAll()
			
			.antMatchers("/accountWiseCharge/**").permitAll()
			
			.antMatchers("/txnReport/**").permitAll()
			
			.antMatchers("/denomination_master/**").permitAll()
			
			.antMatchers("/applicationName/**").permitAll()
			
			.antMatchers("/approve_upgrade_tier/**").permitAll()
			
			.antMatchers("/accountclouser/**").permitAll()
			
			.antMatchers("/thirdParty/**").permitAll()
			
			.antMatchers("/accountClosure/**").permitAll()
			
			.antMatchers("/bulkTransfer/**").permitAll()
			
			.antMatchers("/nubanCode/**").permitAll()  //added by ankit
			.antMatchers("/nubanType/**").permitAll()	//added by ankit
			
			.antMatchers("/actuator/**").permitAll()	//Health 
			
			//.antMatchers("/welcome").permitAll()
			.antMatchers("/NGN/**").permitAll()
			.antMatchers("/participant_master/**").permitAll()
			.antMatchers("/getIvPhraseSalt/**").permitAll()
			.antMatchers("/getApiHash/**").permitAll()
			
			.antMatchers("/feeTypeMaster/**").permitAll()
			.antMatchers("/vatTypeMaster/**").permitAll()
			.antMatchers("/dormancy/**").permitAll()
			.antMatchers("/currencywalletaccount/**").permitAll()
			
			.antMatchers("/accountlrstcsmaster/**").permitAll()
			.antMatchers("/transactionControl/**").permitAll()
			
			.anyRequest().authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
			http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			//http.addFilterBefore(montraAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(customUserDetailsService);
	}
	*/
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
	}
	
	/*
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	*/
	
	/*@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	*/
	
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
}
