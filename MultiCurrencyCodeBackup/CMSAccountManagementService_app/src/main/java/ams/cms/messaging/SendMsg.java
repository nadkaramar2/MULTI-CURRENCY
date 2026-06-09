/*
 * package ams.cms.messaging;
 * 
 * import javax.jms.Queue;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.jms.core.JmsTemplate; import
 * org.springframework.stereotype.Component;
 * 
 * @Component public class SendMsg {
 * 
 * @Autowired JmsTemplate jmsTemplate;
 * 
 * @Autowired Queue smsQueue;
 * 
 * public void publish(String message){
 * 
 * jmsTemplate.convertAndSend(smsQueue, message); }
 * 
 * 
 * }
 */