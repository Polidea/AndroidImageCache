package pl.polidea.webimageview;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;

import android.graphics.Bitmap;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class WebClientTest {

    WebClient client;
    WebInterface httpClient;

    @Before
    public void setup() {
        client = new WebClient();
        httpClient = mock(WebInterface.class);
        client.setWebInterface(httpClient);
    }

    @Test
    public void testMockingHttpClient() {
        // then
        assertTrue(new MockUtil().isMock(client.httpClient));
    }

    @Test
    public void testAddingLinkToDownload() throws ClientProtocolException, IOException {
        // given
        final OnWebClientResultListener clientResultListener = mock(OnWebClientResultListener.class);
        final String path = "http://www.google.pl";
        when(httpClient.execute(path)).thenReturn(mock(HttpResponse.class));

        // when
        client.requestForImage(path, clientResultListener);

        // then
        assertTrue(client.pathsWaitingForDownloading.contains(path));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingLinkToDownloadWithoutCallback() {
        // given
        final String path = "http://";

        // when
        client.requestForImage(path, null);

        // then
        // see annotation
    }

    @Test
    public void testAddingTwoSameLinks() {
        // given
        final OnWebClientResultListener clientResultListener1 = mock(OnWebClientResultListener.class);
        final OnWebClientResultListener clientResultListener2 = mock(OnWebClientResultListener.class);
        final String path = "http://";

        // when
        client.requestForImage(path, clientResultListener1);
        client.requestForImage(path, clientResultListener2);

        // then
        assertEquals(1, client.pathsWaitingForDownloading.size());
    }

    @Test
    public void testSuccessfulDownload() {
        // given
        final String path = "http://";
        final OnWebClientResultListener clientResultListener = mock(OnWebClientResultListener.class);

        // when
        client.requestForImage(path, clientResultListener);

        // then
        verify(clientResultListener, times(1)).onWebHit(anyString(), any(Bitmap.class));
    }

    @Test
    public void testNoMissOnSuccessfulDownload() {
        // given
        final String path = "http://";
        final OnWebClientResultListener clientResultListener = mock(OnWebClientResultListener.class);

        // when
        client.requestForImage(path, clientResultListener);

        // then
        verify(clientResultListener, times(0)).onWebMiss(anyString());
    }

    @Test
    public void testPoolSizeAfterSuccessfulDonwload() {

        // TODO: rename pls
        // given
        final String path = "http://";
        final OnWebClientResultListener clientResultListener = mock(OnWebClientResultListener.class);

        // when
        client.requestForImage(path, clientResultListener);

        // then
        assertTrue(client.pathsWaitingForDownloading.isEmpty() && client.downloadingPaths.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFailureDownload() throws ClientProtocolException, IOException {
        // given
        final OnWebClientResultListener clientResultListener = mock(OnWebClientResultListener.class);
        final String path = "http://www.google.pl";
        when(httpClient.execute(path)).thenThrow(ClientProtocolException.class);

        // when
        client.requestForImage(path, clientResultListener);

        // then
        verify(clientResultListener, times(1)).onWebMiss(anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNoHitOnFailureDownload() throws ClientProtocolException, IOException {
        // given
        final OnWebClientResultListener clientResultListener = mock(OnWebClientResultListener.class);
        final String path = "http://www.google.pl";
        when(httpClient.execute(path)).thenThrow(ClientProtocolException.class);

        // when
        client.requestForImage(path, clientResultListener);

        // then
        verify(clientResultListener, times(0)).onWebHit(anyString(), any(Bitmap.class));
    }

    @Test
    public void testSuccessfullDownloadingSameLinks() {
        // given
        final String path = "http://";
        final OnWebClientResultListener clientResultListener1 = mock(OnWebClientResultListener.class);
        final OnWebClientResultListener clientResultListener2 = mock(OnWebClientResultListener.class);

        // when
        client.requestForImage(path, clientResultListener1);
        client.requestForImage(path, clientResultListener2);

        // then
        verify(clientResultListener1, times(1)).onWebHit(anyString(), any(Bitmap.class));
        verify(clientResultListener2, times(1)).onWebHit(anyString(), any(Bitmap.class));
    }

    @Test
    public void testFailureDownloadingSameLinks() {
        // given
        final String path = "http://";
        final OnWebClientResultListener clientResultListener1 = mock(OnWebClientResultListener.class);
        final OnWebClientResultListener clientResultListener2 = mock(OnWebClientResultListener.class);

        // when
        client.requestForImage(path, clientResultListener1);
        client.requestForImage(path, clientResultListener2);

        // then
        verify(clientResultListener1, times(1)).onWebMiss(anyString());
        verify(clientResultListener2, times(1)).onWebMiss(anyString());
    }

    @Test
    public void testMaximumSimultaneouslyDonwloadingFiles() {
        fail();
    }

    @Test
    public void testPendingDownloads() {
        fail();
    }

    @Test
    public void testWorkingThreadListeners() {
        // idea is to have more listeners than working threads
        fail();
    }

}
