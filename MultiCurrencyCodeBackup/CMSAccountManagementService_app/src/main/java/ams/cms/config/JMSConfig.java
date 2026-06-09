/*
 * package ams.cms.config;
 * 
 * import javax.jms.Queue;
 * 
 * import org.apache.activemq.ActiveMQConnectionFactory; import
 * org.apache.activemq.command.ActiveMQQueue; import
 * org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.jms.config.DefaultJmsListenerContainerFactory; import
 * org.springframework.jms.core.JmsTemplate;
 * 
 * @Configuration public class JMSConfig {
 * 
 * public static final String SMS_QUEUE = "AMS.sms.queue"; public static
 * final String EMAIL_QUEUE = "AMS.email.queue";
 * 
 * 
 * @Bean
 * 
 * @Qualifier("smsQueue") public Queue smsQueue() { return new
 * ActiveMQQueue(SMS_QUEUE); }
 * 
 * @Bean
 * 
 * @Qualifier("emailQueue") public Queue emailQueue() { return new
 * ActiveMQQueue(EMAIL_QUEUE); }
 * 
 * @Bean public ActiveMQConnectionFactory activeMQConnectionFactory() {
 * ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
 * factory.setBrokerURL("tcp://localhost:61616"); return factory; }
 * 
 * @Bean public JmsTemplate jmsTemplate() { return new
 * JmsTemplate(activeMQConnectionFactory()); }
 * 
 * @Bean public DefaultJmsListenerContainerFactory jmsListenerContainerFactory()
 * { DefaultJmsListenerContainerFactory factory = new
 * DefaultJmsListenerContainerFactory();
 * factory.setConnectionFactory(activeMQConnectionFactory());
 * factory.setConcurrency("3-10");
 * 
 * return factory; } }
 */