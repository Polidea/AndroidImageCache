package pl.polidea.webimagesampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import pl.polidea.webimageview.WebImageView;

public class MainActivity extends Activity {

    private static final String URL_3 = "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Bariloche-_Argentina2.jpg/220px-Bariloche-_Argentina2.jpg";
    private static final String URL_2 = "http://static.ddmcdn.com/gif/cheap-landscaping-1.jpg";
    private static final String URL_1 = "http://addyosmani.com/blog/wp-content/uploads/2012/03/Google-doodle-of-Richard-007.jpg";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebImageView.getCache(this).clear();
        final WebImageView imageView1 = (WebImageView) findViewById(R.id.web1);
        final WebImageView imageView2 = (WebImageView) findViewById(R.id.web2);
        final WebImageView imageView3 = (WebImageView) findViewById(R.id.web3);
        final WebImageView imageView4 = (WebImageView) findViewById(R.id.web4);

        imageView1.setImageURL(URL_1);

        imageView2.setImageURL(URL_2);

        imageView3.setImageURL(URL_3);

        imageView4.setImageURL(URL_2);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
