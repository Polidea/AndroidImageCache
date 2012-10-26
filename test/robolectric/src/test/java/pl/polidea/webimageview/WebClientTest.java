package pl.polidea.webimageview;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;

import pl.polidea.imagecache.ImageCacheTestRunner;
import pl.polidea.imagecache.TestExecutorService;
import android.graphics.Bitmap;

@RunWith(ImageCacheTestRunner.class)
public class WebClientTest {

    WebClient client;
    WebInterface httpClient;
    TestExecutorService executorService;
    Bitmap resultBitmap;

    @Before
    public void setup() {
        client = new WebClient();
        executorService = new TestExecutorService();
        httpClient = mock(WebInterface.class);
        client.setWebInterface(httpClient);
        client.setTaskExecutor(executorService);
    }

    @Test
    public void testMockingHttpClient() {
        // then
        assertTrue(new MockUtil().isMock(client.httpClient));
    }

    @Test
    public void testOverridingExecutor() {
        // then
        assertTrue(client.taskExecutor instanceof TestExecutorService);
    }

    @Test
    public void testAddingLinkToDownload() throws ClientProtocolException, IOException {
        // given
        final WebCallback clientResultListener = mock(WebCallback.class);
        final String path = "http://www.google.pl";
        when(httpClient.execute(path)).thenReturn(mock(InputStream.class));

        // when
        client.requestForImage(path, clientResultListener);

        // then
        assertEquals(1, client.pendingTasks.size());
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
        final WebCallback clientResultListener1 = mock(WebCallback.class);
        final WebCallback clientResultListener2 = mock(WebCallback.class);
        final String path = "http://";

        // when
        client.requestForImage(path, clientResultListener1);
        client.requestForImage(path, clientResultListener2);

        // then
        assertEquals(1, client.pendingTasks.size());
    }

    @Test
    public void testSuccessfulDownload() throws ClientProtocolException, IOException {
        // given
        final String path = "http://";
        final WebCallback clientResultListener = mock(WebCallback.class);
        when(httpClient.execute(path)).thenReturn(mock(InputStream.class));

        // when
        client.requestForImage(path, clientResultListener);
        executorService.startCommands();

        // then
        verify(clientResultListener, times(1)).onWebHit(anyString(), any(Bitmap.class));
    }

    @Test
    public void testNoMissOnSuccessfulDownload() throws ClientProtocolException, IOException {
        // given
        final String path = "http://";
        final WebCallback clientResultListener = mock(WebCallback.class);
        when(httpClient.execute(path)).thenReturn(mock(InputStream.class));

        // when
        client.requestForImage(path, clientResultListener);
        executorService.startCommands();

        // then
        verify(clientResultListener, times(0)).onWebMiss(anyString());
    }

    @Test
    public void testPoolSizeAfterSuccessfulDonwload() throws ClientProtocolException, IOException {

        // TODO: rename pls
        // given
        final String path = "http://";
        final WebCallback clientResultListener = mock(WebCallback.class);
        when(httpClient.execute(path)).thenReturn(mock(InputStream.class));

        // when
        client.requestForImage(path, clientResultListener);
        executorService.startCommands();

        // then
        assertEquals(0, client.pendingTasks.size());
        assertEquals(0, client.workingTasks.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFailureDownload() throws ClientProtocolException, IOException {
        // given
        final WebCallback clientResultListener = mock(WebCallback.class);
        final String path = "http://www.google.pl";
        when(httpClient.execute(path)).thenThrow(ClientProtocolException.class);

        // when
        client.requestForImage(path, clientResultListener);
        executorService.startCommands();

        // then
        verify(clientResultListener, times(1)).onWebMiss(anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNoHitOnFailureDownload() throws ClientProtocolException, IOException {
        // given
        final WebCallback clientResultListener = mock(WebCallback.class);
        final String path = "http://www.google.pl";
        when(httpClient.execute(path)).thenThrow(ClientProtocolException.class);

        // when
        client.requestForImage(path, clientResultListener);
        executorService.startCommands();

        // then
        verify(clientResultListener, times(0)).onWebHit(anyString(), any(Bitmap.class));
    }

    @Test
    public void testSuccessfullDownloadingSameLinks() throws ClientProtocolException, IOException {
        // given
        final String path = "http://";
        final WebCallback clientResultListener1 = mock(WebCallback.class);
        final WebCallback clientResultListener2 = mock(WebCallback.class);
        when(httpClient.execute(path)).thenReturn(mock(InputStream.class));

        // when
        client.requestForImage(path, clientResultListener1);
        client.requestForImage(path, clientResultListener2);
        executorService.startCommands();

        // then
        verify(clientResultListener1, times(1)).onWebHit(anyString(), any(Bitmap.class));
        verify(clientResultListener2, times(1)).onWebHit(anyString(), any(Bitmap.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFailureDownloadingSameLinks() throws ClientProtocolException, IOException {
        // given
        final String path = "http://";
        final WebCallback clientResultListener1 = mock(WebCallback.class);
        final WebCallback clientResultListener2 = mock(WebCallback.class);
        when(httpClient.execute(path)).thenThrow(ClientProtocolException.class);

        // when
        client.requestForImage(path, clientResultListener1);
        client.requestForImage(path, clientResultListener2);
        executorService.startCommands();

        // then
        verify(clientResultListener1, times(1)).onWebMiss(anyString());
        verify(clientResultListener2, times(1)).onWebMiss(anyString());
    }

    @Test
    public void testGettingBitmapFromWeb() throws ClientProtocolException, FileNotFoundException, IOException {
        // given
        final String path = "http://";
        final WebCallback callback = new WebCallback() {

            @Override
            public void onWebMiss(final String path) {
            }

            @Override
            public void onWebHit(final String path, final Bitmap bitmap) {
                resultBitmap = bitmap;
            }
        };

        when(httpClient.execute(path)).thenReturn(new FileInputStream("test/robolectric/res/key.png"));

        // when
        client.requestForImage(path, callback);
        executorService.startCommands();

        // then
        assertNotNull(resultBitmap);
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
