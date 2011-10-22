package jp.yagni.media;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;


/**
 * This class is client for notify to MediaContentProvider.
 * 
 * @author YUKI Kaoru
 */
public class MediaScannerNotifier implements MediaScannerConnectionClient
{
    private static final String TAG = "MediaScannerNotifier";
    
    private MediaScannerConnection mConnection;
    private File mFile;

    private MediaScannerNotifier(Context context)
    {
        mConnection = new MediaScannerConnection(context, this);
    }
    
    /**
     * @param context
     * @param file
     */
    public MediaScannerNotifier(Context context, File file)
    {
        this(context);
        
        mFile = file;
    }
    
    /**
     * @param context
     * @param uri
     */
    public MediaScannerNotifier(Context context, Uri uri)
    {
        this(context);
        
        String [] columns = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(uri, columns, null, null, null);
        try {
            if (c.moveToFirst()) {
                mFile = new File(c.getString(0));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public void onMediaScannerConnected()
    {
        Log.d(TAG, "Started scan of " + mFile.getAbsolutePath());
        mConnection.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri)
    {
        Log.d(TAG, "Completed scan of " + path);
        mConnection.disconnect();
    }
    
    public void scan()
    {
        mConnection.connect();
    }
}
