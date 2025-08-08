/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.nomina_api.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "nomina.topic";
    public static final String QUEUE    = "nomina.asiento";
    public static final String RK       = "payroll.posted";

    /* infra */
    @Bean TopicExchange exchange()             { return new TopicExchange(EXCHANGE); }
    @Bean Queue        queue()                 { return QueueBuilder.durable(QUEUE).build(); }
    @Bean Binding      binding()               { return BindingBuilder.bind(queue()).to(exchange()).with(RK); }

    /* JSON converter */
    @Bean Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /* usado para publicar */
    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                        Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }

    /* opcional, por si tuvieras @RabbitListener aquí */
    @Bean SimpleRabbitListenerContainerFactory containerFactory(
            ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }
}
