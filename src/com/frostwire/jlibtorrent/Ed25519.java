package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.ed25519;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Ed25519 {

    public final static int SEED_SIZE = ed25519.seed_size;
    public final static int PRIVATE_KEY_SIZE = ed25519.private_key_size;
    public final static int PUBLIC_KEY_SIZE = ed25519.public_key_size;
    public final static int SIGNATURE_SIZE = ed25519.signature_size;
    public final static int SCALAR_SIZE = ed25519.scalar_size;
    public final static int SHARED_SECRET_SIZE = ed25519.shared_secret_size;

    private Ed25519() {
    }

    public static int createSeed(byte[] seed) {
        if (seed == null || seed.length != SEED_SIZE) {
            throw new IllegalArgumentException("seed buffer must be not null and of size " + SEED_SIZE);
        }
        char_vector v = Vectors.new_char_vector(SEED_SIZE);
        int r = ed25519.create_seed(v);

        Vectors.char_vector2bytes(v, seed);

        return r;
    }

    public static void createKeypair(byte[] publicKey, byte[] privateKey, byte[] seed) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key buffer must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (privateKey == null || privateKey.length != PRIVATE_KEY_SIZE) {
            throw new IllegalArgumentException("private key buffer must be not null and of size " + PRIVATE_KEY_SIZE);
        }

        char_vector v1 = Vectors.new_char_vector(PUBLIC_KEY_SIZE);
        char_vector v2 = Vectors.new_char_vector(PRIVATE_KEY_SIZE);

        ed25519.create_keypair(v1, v2, Vectors.bytes2char_vector(seed));

        Vectors.char_vector2bytes(v1, publicKey);
        Vectors.char_vector2bytes(v2, privateKey);
    }

    public static void sign(byte[] signature, byte[] message, byte[] publicKey, byte[] privateKey) {
        if (signature == null || signature.length != SIGNATURE_SIZE) {
            throw new IllegalArgumentException("signature buffer must be not null and of size " + SIGNATURE_SIZE);
        }

        char_vector v1 = Vectors.new_char_vector(SIGNATURE_SIZE);

        ed25519.sign(v1, Vectors.bytes2char_vector(message), Vectors.bytes2char_vector(publicKey), Vectors.bytes2char_vector(privateKey));

        Vectors.char_vector2bytes(v1, signature);
    }

    public static int verify(byte[] signature, byte[] message, byte[] privateKey) {
        return ed25519.verify(Vectors.bytes2char_vector(signature), Vectors.bytes2char_vector(message), Vectors.bytes2char_vector(privateKey));
    }

    public static void add_scalar(byte[] publicKey, byte[] privateKey, byte[] scalar) {
        if (publicKey == null || publicKey.length != PUBLIC_KEY_SIZE) {
            throw new IllegalArgumentException("public key buffer must be not null and of size " + PUBLIC_KEY_SIZE);
        }
        if (privateKey == null || privateKey.length != PRIVATE_KEY_SIZE) {
            throw new IllegalArgumentException("private key buffer must be not null and of size " + PRIVATE_KEY_SIZE);
        }

        char_vector v1 = Vectors.new_char_vector(PUBLIC_KEY_SIZE);
        char_vector v2 = Vectors.new_char_vector(PRIVATE_KEY_SIZE);

        ed25519.create_keypair(v1, v2, Vectors.bytes2char_vector(scalar));

        Vectors.char_vector2bytes(v1, publicKey);
        Vectors.char_vector2bytes(v2, privateKey);
    }

    public static void key_exchange(byte[] sharedSecret, byte[] publicKey, byte[] privateKey) {
        if (sharedSecret == null || sharedSecret.length != SHARED_SECRET_SIZE) {
            throw new IllegalArgumentException("shared secret buffer must be not null and of size " + SHARED_SECRET_SIZE);
        }

        char_vector v1 = Vectors.new_char_vector(SHARED_SECRET_SIZE);

        ed25519.key_exchange(v1, Vectors.bytes2char_vector(publicKey), Vectors.bytes2char_vector(privateKey));

        Vectors.char_vector2bytes(v1, sharedSecret);
    }
}