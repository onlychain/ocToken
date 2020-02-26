package org.onlychain.secp256k1;

import java.math.BigInteger;

public final class Signature {
	private BigInteger r, s;

	public BigInteger getR() {
		return r;
	}

	public BigInteger getS() {
		return s;
	}

	public Signature(BigInteger r, BigInteger s) {
		this.r = r;
		this.s = s;
	}

	static final class ParseIndex {
		public int value;
	}

	static int parseInt(byte[] bytes, ParseIndex index) {
		if (index.value >= bytes.length) throw new InvalidSignatureException();
		int len = bytes[index.value] & 0xff;
		if (len < 128) {
			index.value++;
			if (index.value + len > bytes.length) throw new InvalidSignatureException();
			return len;
		}
		int lenBytes = len ^ 0x80;
		if (lenBytes > 4 || bytes.length - index.value < 1 + lenBytes) throw new InvalidSignatureException();
		int dataLen = bytes[index.value + 1] & 0xff;
		int dataOffset = index.value + 1 + dataLen;
		for (int i = index.value + 2; i < dataOffset; i++) {
			dataLen = (dataLen << 8) | (bytes[i] & 0xff);
		}
		if (bytes.length - index.value < 1 + lenBytes + dataOffset) throw new InvalidSignatureException();
		index.value = dataOffset;
		return dataLen;
	}

	static BigInteger parseU256(byte[] bytes, ParseIndex index) {
		if (bytes[index.value++] != 2) throw new InvalidSignatureException();
		int u256Len = parseInt(bytes, index);
		byte[] buffer = new byte[32];
		if (u256Len > 32) {
			while (bytes[index.value] == 0) {
				u256Len--;
				index.value++;
			}
			if (u256Len > 32) throw new InvalidSignatureException();
		}
		System.arraycopy(bytes, index.value, buffer, 32 - u256Len, u256Len);
		index.value += u256Len;
		return new BigInteger(1, buffer);
	}

	public static Signature parse(byte[] signBytes) {
		if (signBytes.length == 0) throw new InvalidSignatureException("长度太小");
		ParseIndex i = new ParseIndex();
		if (signBytes[i.value++] != 0x30) throw new InvalidSignatureException();
		parseInt(signBytes, i);

		BigInteger r = parseU256(signBytes, i);
		BigInteger s = parseU256(signBytes, i);
		return new Signature(r, s);
	}

	public byte[] serialize() {
		byte[] buffer = new byte[70];
		buffer[0] = 0x30;
		buffer[1] = 68;
		buffer[2] = 2;
		buffer[3] = 32;
		System.arraycopy(ModP.toByteArray(r), 0, buffer, 4, 32);
		buffer[36] = 2;
		buffer[37] = 32;
		System.arraycopy(ModP.toByteArray(s), 0, buffer, 38, 32);
		return buffer;
	}
}
