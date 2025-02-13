package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

@Configuration
class ModuleConfig {

    public ModuleConfig() {
    }

    @Bean
    public Game game() {
        return new Game(
                pluginLifecycles(),
                entityProcessors(),
                postProcessors(),
                gameEventService()
        );
    }

    @Bean
    public List<IEntityProcessingService> entityProcessors() {
        return ServiceLoader.load(IEntityProcessingService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public List<IPluginLifecycle> pluginLifecycles() {
        return ServiceLoader.load(IPluginLifecycle.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public List<IPostEntityProcessingService> postProcessors() {
        return ServiceLoader.load(IPostEntityProcessingService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public IGameEventService gameEventService() {
        return ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }
}