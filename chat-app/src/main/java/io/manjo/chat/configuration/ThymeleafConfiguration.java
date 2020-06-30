package io.manjo.chat.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

@SuppressWarnings({"unused"})
@Configuration
public class ThymeleafConfiguration implements WebMvcConfigurer, ApplicationContextAware {

    private final static String PREFIX             = "templates/";
    private final static String SUFFIX             = ".html";
    private final static String CHARACTER_ENCODING = "UTF-8";
    private final static String CONTENT_TYPE       = "text/html";

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Bean
    public ViewResolver viewResolver(MessageSource source) {
        ThymeleafViewResolver          view     = new ThymeleafViewResolver();
        SpringResourceTemplateResolver template = new SpringResourceTemplateResolver();
        SpringTemplateEngine           engine   = new SpringTemplateEngine();

        engine.addDialect(java8TimeDialect());
        engine.addDialect(layoutDialect());
        engine.setTemplateEngineMessageSource(source);
        engine.setTemplateResolver(template);
        engine.setEnableSpringELCompiler(true);

        template.setApplicationContext(context);
        template.setPrefix(PREFIX);
        template.setCacheable(false);
        template.setSuffix(SUFFIX);
        template.setTemplateMode(HTML);

        view.setTemplateEngine(engine);
        view.setCharacterEncoding(CHARACTER_ENCODING);
        view.setContentType(CONTENT_TYPE);

        return view;
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

}
