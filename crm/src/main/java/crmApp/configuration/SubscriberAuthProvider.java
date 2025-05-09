//package crmApp.configuration;
//
//import crmApp.entity.SubscriberEntity;
//import crmApp.repository.SubscriberRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//@Component
//@Slf4j
//public class SubscriberAuthProvider implements AuthenticationProvider {
//    @Autowired
//    SubscriberRepository subscriberRepository;
//
//    @Override
//    public Authentication authenticate(Authentication auth) {
//        String login = auth.getName();
//        System.out.println(login);
//        Optional<SubscriberEntity> subsOpt;
//        System.out.println(subscriberRepository.findByLogin("admin").get());
//        subsOpt = subscriberRepository.findByLogin(login);
//        if (subsOpt.isEmpty()){
//            subsOpt=subscriberRepository.findByPassportData(login);
//            if (subsOpt.isEmpty()){
//                log.error("error");
//                throw new EntityNotFoundException("неправильный логин");
//            }
//        }
//        SubscriberEntity subs=subsOpt.get();
//        System.out.println(subs.getAuthorities());
//        return new UsernamePasswordAuthenticationToken(
//                subs.getLogin(),
//                subs.getPassword(),
//                subs.getAuthorities()
//        );
//
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
//
////TODO: сделать почеловечески orelse для авторизации по паспорту