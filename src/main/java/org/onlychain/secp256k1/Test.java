package org.onlychain.secp256k1;



import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.onlychain.crypto.Hash;
import org.onlychain.utils.OcMath;
import org.onlychain.wallet.OCUtils;
import org.onlychain.wallet.tranfer.Script;

import java.math.BigInteger;

public class Test {

	@SuppressWarnings("serial")
	static final class HexFormatException extends RuntimeException {

	}

	static boolean isHex(char c) {
		return (c >= '0' && c <= '9') || ((c | 32) >= 'a' && (c | 32) <= 'f');
	}

	static int hex2int(char c) {
		return ((c & 31) + 9) % 25;
	}

	static byte[] hex2bin(String hex) {
		if (hex.length() % 2 != 0)
			throw new HexFormatException();
		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < result.length; i++) {
			char c1 = hex.charAt(i * 2);
			char c2 = hex.charAt(i * 2 + 1);
			if (!isHex(c1) || !isHex(c2))
				throw new HexFormatException();
			result[i] = (byte) ((hex2int(c1) << 4) | hex2int(c2));
		}
		return result;
	}

	static String bin2hex(byte[] bin) {
		final String HEX_TEMPLATE = "0123456789abcdef";
		char[] hexChars = new char[bin.length * 2];
		for (int i = 0; i < bin.length; i++) {
			hexChars[i * 2] = HEX_TEMPLATE.charAt((bin[i] & 0xff) >>> 4);
			hexChars[i * 2 + 1] = HEX_TEMPLATE.charAt(bin[i] & 15);
		}
		return new String(hexChars);
	}

	public static void main(String[] args) {
		byte[] privateKey = hex2bin("cf2bb6823e3180e17cb043b5f5f3a78b456d656319c10fef8c7b231b19d0ec41");
		PublicKey publicKey = Secp256k1.createPublicKey(privateKey);
		System.out.println(bin2hex(publicKey.serialize(false)));
		System.out.println(bin2hex(publicKey.serialize(true)));
		String sha= "010101019aa9b9e37418ec493967a88c3f4847dff1feabb1bdb37c369cbc064f7729f64801006a473045022100fa548d5bb4e3688062903acd8aaa3ab837fa500680207d7ff6fa12500e56fb020220734b64af31cd6e481620bcf335d79d9d3c8c53c6a0075587d40bd5f73aa8d7b82102c83dbcd176ba7eee11dd65953b7678e4443c09aec58d194bd68299b6248a5be4030000000129117fb1001976a9141bed92c068c2a235c84049e653c95eecce285fb388ac0000000000000001001976a914cc8ce899d2481722ca80bbd0a63fee71c4fde13d88ac000000000000006f001976a914ac6ce899d2481722ca80bbd0a63fee71c4fde13d88ac01000000000000000000ce90bdf105deaf2a02c83dbcd176ba7eee11dd65953b7678e4443c09aec58d194bd68299b6248a5be40100";

//		Hash.sha256(Hash.sha256(HexBin.decode(sha)));
//		byte[] message = hex2bin("5df6e0e2761359d30a8275058e299fcc0381534545f55cf43e41983f5d4c9456");
		byte[] message =HexBin.decode(OCUtils.getTxId(sha));
		Signature sig = Secp256k1.sign(privateKey, message);


		System.out.println("签名结果:"+ HexBin.encode(sig.serialize()));

		if (Secp256k1.verify(publicKey, message, sig)) {
			System.out.println("+++验证成功");
		} else {
			System.out.println("验证失败");
		}

		byte[] message2 = hex2bin("5df6e0e2761359d30a8275058e299fcc0381534545f55cf43e41983f5d4c9458"); // 测试验证失败
		if (Secp256k1.verify(publicKey, message2, sig)) {
			System.out.println("验证成功");
		} else {
			System.out.println("验证失败");
		}

		PublicKey publicKey2 = PublicKey.parse(hex2bin("04c83dbcd176ba7eee11dd65953b7678e4443c09aec58d194bd68299b6248a5be406b6a7d59c6dba03f21b85a316f010216703fec39c37c13ae145a3e0a97f6484"));
		PublicKey publicKey3 = PublicKey.parse(hex2bin("02c83dbcd176ba7eee11dd65953b7678e4443c09aec58d194bd68299b6248a5be4"));
		Signature sig2 = Signature.parse(hex2bin("304402205e7c1ea4379b71b0d9fc0ae00d64b3c627e21cbbd94be8b9880f6877163bb778022062febbbf2604d7bc0fdee30fb6d7ebd1da8ae27ae883da84d9ee0b6e2a345402"));

		if (Secp256k1.verify(publicKey2, message, sig2)) {
			System.out.println("====验证成功");
		} else {
			System.out.println("验证失败");
		}
		if (Secp256k1.verify(publicKey3, message, sig2)) {
			System.out.println("验证成功");
		} else {
			System.out.println("验证失败");
		}


		System.out.println(OCUtils.getTxId(OcMath.hexStringToByteArray("1")));
		System.out.println(OcMath.toHexStringNoPrefix(Hash.sha256(OcMath.hexStringToByteArray("1"))));


//		System.out.println(bin2hex(publicKey.serialize(true)));
		System.out.println(Script.vOutScript("5f378522c9190a4058477c1b329eb52834d35b02"));
	}

}


