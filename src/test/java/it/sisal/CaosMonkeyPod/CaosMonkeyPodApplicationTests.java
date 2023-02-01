package it.sisal.CaosMonkeyPod;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CaosMonkeyPodApplicationTest {
    @Mock
    private CoreV1Api api;
    @InjectMocks
    private CaosMonkeyPodApplication application;

    @BeforeEach
    public void setUp() throws Exception {

        MockitoAnnotations.openMocks(this).close();
    }
    @Test
    public void deleteTwoRandomPods() throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        CaosMonkeyPodApplication application = new CaosMonkeyPodApplication();
        
        V1PodList podList = new V1PodList();
        List<V1Pod> pods = new ArrayList<>();
        V1Pod pod1 = new V1Pod();
        pod1.setMetadata(new V1ObjectMeta().namespace("testing"));
        pods.add(pod1);
        V1Pod pod2 = new V1Pod();
        pod2.setMetadata(new V1ObjectMeta().namespace("testing"));
        pods.add(pod2);
        podList.setItems(pods);

        when(api.listPodForAllNamespaces(null, null, null, null, null, "app=nginx", null, null, null, null)).thenReturn(podList);
        //test that both pods are deleted 
        V1Pod podDeleted =  application.deleteRandomPod();
        assert(podDeleted.getMetadata().getNamespace().equals("testing"));
        V1Pod podDeleted2 =  application.deleteRandomPod();
        assert(podDeleted2.getMetadata().getNamespace().equals("testing"));
    }
    @Test
    public void deletesNoPods() throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        CaosMonkeyPodApplication applicationCaosMonkey = new CaosMonkeyPodApplication();
        
        V1PodList podList = new V1PodList();
        List<V1Pod> pods = new ArrayList<>();
        V1Pod pod1 = new V1Pod();
        pod1.setMetadata(new V1ObjectMeta().namespace("noTesting"));
        pods.add(pod1);
        V1Pod pod2 = new V1Pod();
        pod2.setMetadata(new V1ObjectMeta().namespace("noTesting"));
        pods.add(pod2);
        podList.setItems(pods);

        when(api.listPodForAllNamespaces(null, null, null, null, null, "app=nginx", null, null, null, null)).thenReturn(podList);
        //test that no  pod is deleted since they have namespace different from testing
        int numberDeletedPods=0;
        for (V1Pod v1Pod : pods) {
        	if(applicationCaosMonkey.deleteRandomPod().getMetadata().getNamespace()=="testing") {
        		numberDeletedPods++;
        	}
	}
        
      assertEquals(0,numberDeletedPods);
    }
    
    
}
