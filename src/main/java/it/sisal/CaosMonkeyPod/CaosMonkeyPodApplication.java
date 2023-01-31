package it.sisal.CaosMonkeyPod;

import io.kubernetes.client.util.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class CaosMonkeyPodApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaosMonkeyPodApplication.class, args);
    }

	private CoreV1Api api;
    
    @Scheduled(fixedRate = 10000)
    public  V1Pod deleteRandomPod() throws IOException, ApiException {
        // Code to interact with Kubernetes API server and delete a random pod in the "testing" namespace
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        
        if(this.api==null ) {
        	this.api = new CoreV1Api();
        }

        V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, "app=nginx", null, null, null, null);
        List<V1Pod> pods = podList.getItems();
        List<V1Pod> testingPods = new ArrayList<>();
        for (V1Pod pod : pods) {
            if (pod.getMetadata().getNamespace().equals("testing")) {
                testingPods.add(pod);
            }
        }

        Random random = new Random();
        int randomIndex = random.nextInt(testingPods.size());
        V1Pod randomPod = testingPods.get(randomIndex);

        V1Pod pod = api.deleteNamespacedPod(randomPod.getMetadata().getName(), randomPod.getMetadata().getNamespace(), null, null, null, null, null, null);

        System.out.println("Deleted pod: " + randomPod.getMetadata().getName());
		return pod;
    }

	public CoreV1Api getApi() {
		return api;
	}

	public void setApi(CoreV1Api api) {
		this.api = api;
	}

	public CaosMonkeyPodApplication() {
		super();
	}

	public CaosMonkeyPodApplication(CoreV1Api api) {
		super();
		this.api = api;
	}
	
    	
}