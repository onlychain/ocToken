package org.onlychain.secp256k1;

@SuppressWarnings("serial")
public final class InvalidPublicKeyException extends RuntimeException {
	public InvalidPublicKeyException(String message) {
		super(message);
	}
}
