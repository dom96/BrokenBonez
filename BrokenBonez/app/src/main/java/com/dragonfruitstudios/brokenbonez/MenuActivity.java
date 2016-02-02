package com.dragonfruitstudios.brokenbonez;
        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.text.method.Touch;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.WindowManager;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.ImageView;
        import android.widget.Toast;
        import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;

public class MenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView image = new ImageView(this);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String[] s = {"tvwithinfo.png"};
        AssetLoader a = new AssetLoader(this, s);
        a.AddAssets(this, s);
        Bitmap bm = a.getBitmapByName("tvwithinfo.png");
        image.setImageBitmap(bm);
        setContentView(image);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ImageView image = new ImageView(this);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String[] s = {"tv.png"};
        AssetLoader a = new AssetLoader(this, s);
        a.AddAssets(this, s);
        Bitmap bm = a.getBitmapByName("tv.png");
        image.setImageBitmap(bm);
        setContentView(image);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_myactivity, menu);
        return true;
    }

    // Touch Handler
    @Override
    public boolean onTouchEvent(MotionEvent event){
        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();
        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);
        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();
        // specify methods in relation to motion events
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                openOptionsMenu();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                Log.d("FINGER WENT UP", "A finger has touched the screen and moved up!");
                Log.d("ACTION EVENT CANCELLED", "Something else took control of the touch event!");
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startGame:
                Toast.makeText(getApplicationContext(),	item.getTitle() + "Let's Play!", Toast.LENGTH_SHORT).show();
                try {
                    Class GameActivity = Class.forName("com.dragonfruitstudios.brokenbonez.GameActivity");
                    Intent intent = new Intent(this, GameActivity);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.hiScore:
                Toast.makeText(getApplicationContext(),	item.getTitle() + "What's your score?", Toast.LENGTH_SHORT).show();
                break;
            case R.id.helpGuide:
                Toast.makeText(getApplicationContext(),	item.getTitle() + "Need some help?", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }





}