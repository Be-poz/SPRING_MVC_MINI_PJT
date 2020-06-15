package kr.co.kang.config;

import javax.annotation.Resource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.kang.beans.UserBean;
import kr.co.kang.interceptor.CheckLoginInterceptor;
import kr.co.kang.interceptor.CheckWriterInterceptor;
import kr.co.kang.interceptor.TopMenuInterceptor;
import kr.co.kang.mapper.BoardMapper;
import kr.co.kang.mapper.TopMenuMapper;
import kr.co.kang.mapper.UserMapper;
import kr.co.kang.service.BoardService;
import kr.co.kang.service.TopMenuService;

//Spring MVC ������Ʈ�� ���õ� ������ �ϴ� Ŭ����
@Configuration
//Controller ������̼��� ���õǾ��ִ� Ŭ������ COntroller�� ����Ѵ�.
@EnableWebMvc
//��ĵ�� ��Ű���� �����Ѵ�.
@ComponentScan("kr.co.kang.controller")
@ComponentScan("kr.co.kang.service")
@ComponentScan("kr.co.kang.dao")


@PropertySource("/WEB-INF/properties/db.properties")
public class ServletAppContext implements WebMvcConfigurer{
	
	@Value("${db.classname}")
	private String db_classname;
	
	@Value("${db.url}")
	private String db_url;
	
	@Value("${db.username}")
	private String db_username;
	
	@Value("${db.password}")
	private String db_password;
	
	@Autowired
	private TopMenuService topMenuService;
	
	@Autowired
	private BoardService boardService;
	
	@Resource(name="loginUserBean")
	private UserBean loginUserBean;
	

	
	//Controller�� �޼��尡 ��ȯ�ϴ� jsp�� �̸� �� �ڿ� ��ο� Ȯ���ڸ� �ٿ��ֵ��� �����Ѵ�.
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/",".jsp");
	}
	
	//���� ������ ��θ� �����Ѵ�.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}
	
	//�����ͺ��̽� ���� ������ �����ϴ� Bean
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource source=new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);
		
		return source;
	}
	
	//�������� ���� ������ �����ϴ� ��ü
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception{
		SqlSessionFactoryBean factoryBean=new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}
	
	//������ ������ ���� ��ü(Mapper ����)
	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception{ 
		MapperFactoryBean<BoardMapper> factoryBean=new MapperFactoryBean<BoardMapper>(BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Bean
	public MapperFactoryBean<TopMenuMapper> getTopMenuMapper(SqlSessionFactory factory) throws Exception{ 
		MapperFactoryBean<TopMenuMapper> factoryBean=new MapperFactoryBean<TopMenuMapper>(TopMenuMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Bean
	public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception{ 
		MapperFactoryBean<UserMapper> factoryBean=new MapperFactoryBean<UserMapper>(UserMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addInterceptors(registry);
		
		TopMenuInterceptor topMenuInterceptor=new TopMenuInterceptor(topMenuService,loginUserBean);
		CheckLoginInterceptor checkLoginInterceptor=new CheckLoginInterceptor(loginUserBean);
		CheckWriterInterceptor checkWriterInterceptor=new CheckWriterInterceptor(loginUserBean,boardService);
		
		InterceptorRegistration reg1=registry.addInterceptor(topMenuInterceptor);
		InterceptorRegistration reg2=registry.addInterceptor(checkLoginInterceptor);
		InterceptorRegistration reg3=registry.addInterceptor(checkWriterInterceptor);
		
		reg1.addPathPatterns("/**");
		reg2.addPathPatterns("/user/modify","user/logout","/board/*");
		reg2.excludePathPatterns("/board/main");
		reg3.addPathPatterns("/board/delete","/board/modify");
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer PropertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource res=new ReloadableResourceBundleMessageSource();
		res.setBasenames("/WEB-INF/properties/error_message");
		return res;
	}
	
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
	
}
