package com.junjunguo.phialmaps.activities;

import com.junjunguo.phialmaps.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Permission  extends AppCompatActivity
implements ActivityCompat.OnRequestPermissionsResultCallback, OnClickListener
{
  static String[] sPermission;
  static boolean isFirstForcedPermission;
  static int idCounter = 0;
  static boolean isAsking = false;

  /** Start a Permission-Request, and calls activity.finish().
   *  @param sPermission The Permission of android.Manifest.permission.xyz
   *  @param isFirstForcedPermission True, if the first one of sPermission is forced. **/
  public static void startRequest(String[] sPermission, boolean isFirstForcedPermission, Activity activity)
  {
    Permission.sPermission = sPermission;
    Permission.isFirstForcedPermission = isFirstForcedPermission;

    Intent intent = new Intent(activity, Permission.class);
    activity.startActivity(intent);
//    activity.finish();
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (sPermission == null) { finish(); return; }
    setContentView(R.layout.activity_text);
    Button okButton = (Button) findViewById(R.id.okTextButton);
    Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
    EditText listText = (EditText) findViewById(R.id.areaText);
    listText.setFocusable(false);
    listText.setText(getPermissionText());
    okButton.setOnClickListener(this);
    btn_cancel.setOnClickListener(this);
  }
  
  private CharSequence getPermissionText()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Asking for permissions:\n\n");
    String necessary = "Necessary:\n";
    if (!isFirstForcedPermission) { necessary = "Recommended:\n"; }
    for (String curPermission : sPermission)
    {
      sb.append(necessary);
      necessary = "Recommended:\n";
      sb.append(curPermission.replace('.', '\n'));
      sb.append("\n\n");
    }
    return sb;
  }

  @Override protected void onResume()
  {
    super.onResume();
    if (!isAsking) {}
    else if (checkPermission(sPermission[0], this))
    {
      finish();
    }
    else if (!isFirstForcedPermission)
    {
      finish();
    }
    else
    {
      logUser("App needs access!!!");
    }
    isAsking = false;
  }
  
  @Override
  public void onClick(View v)
  {
    if (v.getId()==R.id.okTextButton)
    {
      log("Selected: Permission-Ok");
      requestPermissionLater(sPermission);
      isAsking = true;
    }else {
      finish();
    }
  }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
            } else {
              if (!isFirstForcedPermission)
              {
                finish();
              }
              else
              {
                logUser("App needs access for this feature!");
              }
            }
    }

    /** Check if permission is already permitted.
     *  @param sPermission The Permission of android.Manifest.permission.xyz **/
    public static boolean checkPermission(String sPermission, Context context) {
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(context, sPermission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    
    /** Check for permission to permit.
     *  @param sPermission The Permission of android.Manifest.permission.xyz **/
    private void requestPermissionLater(String[] sPermission) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                sPermission)) {
                    ActivityCompat.requestPermissions(this,
                            sPermission,
                            getId());
//        } else {
//          logUser("Permission is not available: " + sPermission);
//          return false;
//        }
    }
    
    private int getId()
    {
      idCounter ++;
      return idCounter;
    }

    private void log(String str)
    {
      Log.i(Permission.class.getName(), str);
    }
    
    private void logUser(String str)
    {
      Log.i(Permission.class.getName(), str);
      try
      {
        Toast.makeText(this.getBaseContext(), str, Toast.LENGTH_SHORT).show();
      }
      catch (Exception e) { e.printStackTrace(); }
    }
}

