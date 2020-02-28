package org.onlychain.secp256k1;



import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.onlychain.crypto.Hash;
import org.onlychain.utils.WalletUtils;
import org.onlychain.utils.OcMath;
import org.onlychain.wallet.tranfer.Script;

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
		byte[] privateKey = hex2bin("2603e40800407582ca68f327bdc7222922c7e53d6b2f73c12e4cdc6407752500");

		PublicKey publicKey = Secp256k1.createPublicKey(privateKey);
		System.out.println(bin2hex(publicKey.serialize(false)));
		System.out.println(bin2hex(publicKey.serialize(true)));
		String sha= "01010101b0de12e604703e2c3be4bd6f89cdf91f9e8a67678e7e4e17d81391262df4b6a600006a473045022100c92f4ed311cf6c435dc1ea7a7ff0b52dc896f7fd53dd45927d1eee2ecf9e1b7e02205145e133b0618b0d3f234ee22b09d7063b67b4a82cf5f6789e406ac1d3661134210362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207010066220677d3bc00001976a91400e40caa57a129e567df123f4e8043f8e185288788ac000000000000000000009aa4e0f205dea3010362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207010030";

//		Hash.sha256(Hash.sha256(HexBin.decode(sha)));
//		byte[] message = hex2bin("5df6e0e2761359d30a8275058e299fcc0381534545f55cf43e41983f5d4c9456");
		byte[] message =Hash.sha256(hex2bin(sha));
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
		PublicKey publicKey3 = PublicKey.parse(hex2bin("0362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207"));

		Signature sig22 = Signature.parse(hex2bin("3045022046d90235e7523d997b6e823ac0282a194ed5c56d5a4503dd037b3a3ef1195c2a022100b65cbbfca4416caceb5371702c8169f5a36b030621f9493b0af0e8bb2437ee21"));
		byte[] message22 =OcMath.hexStringToByteArray("0101010507955a5752fde226320f076dbdebb13a5c0803e15eef493d7a5616c08298b21a01006a47304502205637f441cbc83981b21550b105b8b6d3e6c6a02f64e9934a92d8a6ab7766a8f8022100bc639ecc3478ec881c547639e23c6bd99ec786e5964cd0477060e864c8d8ae42210362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207a8b341383302ea0015d92f8a6a21792d468da481eaf080768d3bf11de60881d600006b483046022100cbeac7dbbb1b72d512cb1c416ccd0cee5f7f0b50b2375481019c93690d4581780221009b2489f02492bb8c04e3b1f3564c9afd5c9a71e43954b44d1267cb8a90ed1632210362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207bbafdeddec47698bac37f7256a050ab4875a4a630bce093865e3105bff1e4d5000006a473045022048843c4c1c1dc7d1ac994acfff9997386287223842c2e03fe0dd4523dcd7388e022100907c83b8773ea3586509829acf987b17fc3d21c656a104ab300c20d543ea4f9a210362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207cb6200e099f1fc2e4e6b008631018e8689d53548f784bde76959b835408aa4c401006946304402201fdef17d656be7b8d0060d4b938f7d2845d281499afe7bd13297415111e0d35a02200e6cdc39810d61d35945297d2412810311a6e7214b22cc45003b75f957ca0d0a210362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207f9294117bcb75b5863de5880f0ca19deb588de0f6e66f879f9637bf97690fd71000069463044022019d76acacd870976e96ce7a3caee89e8cb0aa0851a20d1cb758f33d46445a117022047f227c9925a8535544b600ddbb35f67d776f39a6c3e031cda80ad38bcc2b06f210362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e2070100000000540be400001976a9148adcf33c1bc6669c5746824587c698065883aa1788ac00000000000000000000d398deda882ee7560362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e2070100");
		PublicKey publicKey22 = PublicKey.parse(hex2bin("0362c8a8031cc95d2c49ebf0cd5f1adedada9862211f4a5846ccc0ec5a14e1e207"));
		System.out.println("\n\n\n\n\nhashmsg："+OcMath.toHexStringNoPrefix(Hash.sha256(Hash.sha256(message22))));

		if (Secp256k1.verify(publicKey22,  Hash.sha256(Hash.sha256(message22)), sig22)) {
			System.out.println("=============验证成功=========两次hash==================");
		} else if (Secp256k1.verify(publicKey22, Hash.sha256(message22), sig22)){
			System.out.println("===22===========验证成功=========一次hash==================");
		} else {
			System.out.println("==============验证失败");
		}
		System.out.println("\n\n\n\n");
		if (Secp256k1.verify(publicKey3, message, sig22)) {
			System.out.println("验证成功");
		} else {
			System.out.println("验证失败");
		}


		System.out.println(WalletUtils.getTxId(OcMath.hexStringToByteArray("1")));
		System.out.println(OcMath.toHexStringNoPrefix(Hash.sha256(OcMath.hexStringToByteArray("1"))));


//		System.out.println(bin2hex(publicKey.serialize(true)));
		System.out.println(Script.vOutScript("5f378522c9190a4058477c1b329eb52834d35b02"));
		System.out.println(OcMath.toHexStringNoPrefix(Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray("01010101b0de12e604703e2c3be4bd6f89cdf91f9e8a67678e7e4e17d81391262df4b6a600006a473045022100872f656051dc7f2ffb34f3b554cdcfc936ae45d99687a0822433a391515e3a3a02204876a3d97b5e6ec1bc6b65f80c2105f5f058d343d3aa76d9594fbf4315a8e36c21025fee0dc100cc5adcac3ad455ab28b8aa6c89e080a946b3ba085f1750ede9b503010066220677d3bc00001976a91400e40caa57a129e567df123f4e8043f8e185288788ac00000000000000000000e0dee2f2058122025fee0dc100cc5adcac3ad455ab28b8aa6c89e080a946b3ba085f1750ede9b5030100")))));
	}

}



