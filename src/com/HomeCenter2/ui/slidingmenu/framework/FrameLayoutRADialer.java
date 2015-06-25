package com.HomeCenter2.ui.slidingmenu.framework;

import java.util.UUID;

import android.content.Context;
import android.util.Base64;
import android.widget.FrameLayout;

public class FrameLayoutRADialer extends FrameLayout {
	public FrameLayoutRADialer(Context context) {
		super(context);
		String id = generateUUID();
		id = id + "radialer_";
		int idHash = id.hashCode();
		if (idHash < 0)
			idHash = idHash * (-1);
		setId(idHash);
	}

	private String generateUUID() {
		UUID uuid = UUID.randomUUID();
		byte[] uuidArr = asByteArray(uuid);

		String id = Base64.encodeToString(uuidArr, Base64.DEFAULT);
		id = id.trim();
		return id;
	}

	public static byte[] asByteArray(UUID uuid) {

		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		byte[] buffer = new byte[16];

		for (int i = 0; i < 8; i++) {
			buffer[i] = (byte) (msb >>> 8 * (7 - i));
		}
		for (int i = 8; i < 16; i++) {
			buffer[i] = (byte) (lsb >>> 8 * (7 - i));
		}

		return buffer;

	}

	public static UUID toUUID(byte[] byteArray) {

		long msb = 0;
		long lsb = 0;
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (byteArray[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (byteArray[i] & 0xff);
		UUID result = new UUID(msb, lsb);

		return result;
	}

}
