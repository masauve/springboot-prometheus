This project is based on the Snowdrop SpringBoot Booster found here: https://github.com/snowdrop/spring-boot-http-booster

It adds Prometheus monitoring using the Micrometer.io library.

The following code changes were done:

pom.xml
'''
    <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-spring-legacy</artifactId>
       <version>1.0.3</version>
   </dependency>
       <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-registry-prometheus</artifactId>
       <version>1.0.3</version>
   </dependency>
'''

application.properties - Disable Spring security check for metrics.

management.security.enabled:false 


MetricsConfiguration class - This class is optional. It is use for adding custom tags to the metrics to for easy identification in Prometheus graphs and console.

'''
@Configuration
public class MetricsConfiguration{
    @Bean
    MeterRegistryCustomizer meterRegistryCustomizer (MeterRegistry meterRegistry){
        return meterRegistry1 -> {
           meterRegistry.config()
           .commonTags("application","myapp");
        };
    }
}
'''

To deploy to OpenShift, create a project and deploy using maven:

'''
./mvnw clean fabric8:deploy -Popenshift
'''

To make the endpoint discoverable by Prometheus, the following annotations needs to be added to the Kubernetes service:

'''
    prometheus.io/path: /prometheus
    prometheus.io/port: '8080'
    prometheus.io/scrape: 'true'
'''

Finally, add the OpenShift namespace to the prometheus configuration in your OpenShift deployment

In your Prometheus namespace:
'''
oc get cm prometheus -o yaml > prometheus.yaml
'''

edit prometheus.yaml to add the namespace of your SpringBoot application:
'''
      kubernetes_sd_configs:
      - role: pod
        namespaces:
          names:
          - prometheus
          - ${YOUR_NAMESPACE}

    ....
      
      kubernetes_sd_configs:
      - role: endpoints
        namespaces:
          names:
          - prometheus
          - ${YOUR_NAMESPACE}

'''

restart prometheus and reload the configuration.



