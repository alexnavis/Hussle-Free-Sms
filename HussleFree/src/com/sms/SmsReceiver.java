package com.sms;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	static final String ACTION =
			            "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
	
		System.out.println(intent.getAction());
		Log.d(null, "Intent ReceivedVVVVVVVVVVVVVVVVVVVv "  + intent.getAction());

		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		android.telephony.SmsMessage smsMessage[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
		smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}

		// show first message
		Log.d(null,"Received SMS: " + smsMessage[0].getMessageBody() );
		Toast toast = Toast.makeText(context, "Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
		toast.show();
			
		Uri uriSms = Uri.parse("content://sms/inbox");
		List<String> messagesList = getSms(context);
		
		for(String m : messagesList) {
			Log.d(null, "M"  + m);
		}
		
		abortBroadcast();
		Log.d(null, "Intent ReceivedVVVVVVVVVVVVVVVVVVVv end"  + intent.getAction());
	}
	
	public List<String> getSms(Context context) {
		Uri mSmsQueryUri = Uri.parse("content://sms/inbox");
		List<String> messages = new ArrayList<String>();
		Cursor cursor = null;
		
		cursor = context.getContentResolver().query(mSmsQueryUri, null, null, null, null);
		if (cursor == null) {
		Log.i(null, "cursor is null. uri: " + mSmsQueryUri);
		return messages;
		}

		for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
		final String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
		messages.add(body);
		}	
		
		return messages;
	}
}
