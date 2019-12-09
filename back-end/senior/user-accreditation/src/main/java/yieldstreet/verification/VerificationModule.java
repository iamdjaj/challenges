package yieldstreet.verification;

import java.util.List;

import play.Environment;
import play.inject.Binding;
import play.inject.Module;

import com.typesafe.config.Config;

public class VerificationModule extends Module {

    @Override
    public List<Binding<?>> bindings(Environment environment, Config config) {
        return List.of(bindClass(VerificationServiceProvider.class)
            .to(VerificationServiceProviderImpl.class));
    }

}
