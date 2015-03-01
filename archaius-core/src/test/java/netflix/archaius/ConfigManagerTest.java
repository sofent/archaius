package netflix.archaius;

import netflix.archaius.config.EnvironmentConfig;
import netflix.archaius.config.MapConfig;
import netflix.archaius.config.SimpleDynamicConfig;
import netflix.archaius.config.SystemConfig;

import org.junit.Test;

public class ConfigManagerTest {
    @Test
    public void testBasicReplacement() {
        SimpleDynamicConfig dyn = new SimpleDynamicConfig("FAST");
        
        ConfigManager config = ConfigManager.builder()
                .build();
        
        config.addConfig(dyn)
              .addConfig(MapConfig.builder("test")
                        .put("env",    "prod")
                        .put("region", "us-east")
                        .put("c",      123)
                        .build())
              .addConfig(new EnvironmentConfig())
              .addConfig(new SystemConfig());
        
        config.listen("abc").subscribe(new PropertyObserver<String>() {
            @Override
            public void onChange(String key, String previous, String next) {
                System.out.println("Configuration changed : " + key + " " + previous + " " + next);
            }

            @Override
            public void onError(String propName, Throwable error) {
                // TODO Auto-generated method stub
                
            }
        }, String.class, null);
        
        dyn.setProperty("abc", "${c}");
    }
}