package pl.polidea.webimagesampleapp;

import pl.polidea.webimageview.WebImageView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WebImageView.getCache(this).clear();
		final WebImageView imageView1 = (WebImageView) findViewById(R.id.web1);
		final WebImageView imageView2 = (WebImageView) findViewById(R.id.web2);
		final WebImageView imageView3 = (WebImageView) findViewById(R.id.web3);
		final WebImageView imageView4 = (WebImageView) findViewById(R.id.web4);

		imageView1
				.setImageURL("http://addyosmani.com/blog/wp-content/uploads/2012/03/Google-doodle-of-Richard-007.jpg");
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
