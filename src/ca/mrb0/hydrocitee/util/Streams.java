package ca.mrb0.hydrocitee.util;

import java.io.IOException;
import java.io.InputStream;

public class Streams {

	public static int unpack16(byte[] arr, int offs) {
		return ((int)(0xff & arr[offs+1]) << 8) | (int)(0xff & arr[offs]);
	}

	public static long unpack32(byte[] arr, int offs) {
		long out = 0;
		out |= (long)(0xff & arr[offs]);
		out |= (long)(0xff & arr[offs+1]) << 8;
		out |= (long)(0xff & arr[offs+2]) << 16;
		out |= (long)(0xff & arr[offs+3]) << 24;
		return out;
	}

	public static long[] readLongBlock(InputStream is, int count) throws IOException {
		long[] offsets = new long[count];
		for(int i = 0; i < count; i++) {
			byte[] offs = new byte[4];
			if (is.read(offs) != offs.length) {
				throw new IllegalArgumentException("couldn't read all the offsets");
			}
			offsets[i] = unpack32(offs, 0);
		}
		return offsets;
	}

	public static int[] readByteBlock(InputStream is, int count) throws IOException {
		int[] bytes = new int[count];
		for(int i = 0; i < count; i++) {
			int b = is.read();
			if (b == -1) {
				throw new IllegalArgumentException("couldn't read all the bytes");
			}
			bytes[i] = b;
		}
		return bytes;
	}

	public static int arrayIndexOf(byte[] haystack, byte needle) {
		for(int i = 0; i < haystack.length; i++) {
			if (haystack[i] == needle) {
				return i;
			}
		}
		return -1;
	}

}
