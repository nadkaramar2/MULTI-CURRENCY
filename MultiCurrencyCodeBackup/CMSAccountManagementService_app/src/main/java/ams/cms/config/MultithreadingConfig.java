package ams.cms.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class MultithreadingConfig 
{
	@Bean(name = "multiThreadBean")
	public Executor getThreadPoolExecutor() 
	{
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(15);
		executor.setMaxPoolSize(20);
		executor.setThreadNamePrefix("AMSThread-");
		executor.initialize();
		
		return executor;
	}
}
