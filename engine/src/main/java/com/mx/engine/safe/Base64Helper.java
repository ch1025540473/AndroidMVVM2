package com.mx.engine.safe;

import android.util.Base64;

import java.nio.charset.Charset;

/**
 * A helper class that encode/decode Base64 strings.<br/>
 * This class can convert from string to string, which is a usual handy usage.<br/>
 * Besides that, you can also build your custom encoder for some special cases.<br/>
 * <br/>
 * Created by chenbaocheng on 16/4/20.
 */
public final class Base64Helper {

    /**
     * On Android, this value is UTF-8
     */
    public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    //Cannot new a Base64Helper yourself, use newBuilder() to build one.
    private Base64Helper() {
    }

    //This builder holds some parameters for encoding/decoding
    private Builder builder = null;

    /**
     * @param plainText The text needs to be encoded in Base64
     * @return the encoded string
     */
    public String encodeString(String plainText) {
        return new String(encodeBytes(plainText.getBytes(builder.getCharSet())), builder.getCharSet()).intern();
    }

    /**
     * @param input The bytes needs to be encoded in Base64
     * @return the encoded bytes
     */
    public byte[] encodeBytes(byte[] input) {
        return Base64.encode(input, builder.getFlags());
    }

    /**
     * @param encodedText The text needs to be decoded in Base64
     * @return the pain text after decoding
     */
    public String decodeString(String encodedText) {
        return new String(decodeBytes(encodedText.getBytes(builder.getCharSet())), builder.getCharSet()).intern();
    }

    /**
     * @param input The bytes needs to be decoded in Base64
     * @return the decoded bytes
     */
    public byte[] decodeBytes(byte[] input) {
        return Base64.decode(input, builder.getFlags());
    }

    /**
     * Get a builder who is able to build a Base64Helper instance.
     *
     * @return a new builder.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * @param plainText The text needs to be encoded in Base64
     * @return the encoded string
     */
    public static String encode(String plainText) {
        return newBuilder().build().encodeString(plainText);
    }

    /**
     * @param encodedText The text needs to be decoded in Base64
     * @return the pain text after decoding
     */
    public static String decode(String encodedText) {
        return newBuilder().build().decodeString(encodedText);
    }

    public static class Builder {
        private Charset charSet = DEFAULT_CHARSET;
        private int flags = Base64.NO_WRAP;

        public int getFlags() {
            return flags;
        }

        public void setFlags(int flags) {
            this.flags = flags;
        }

        public Charset getCharSet() {
            return charSet;
        }

        public void setCharSet(Charset charSet) {
            this.charSet = charSet;
        }

        public void setCharset(String charSetName) {
            setCharSet(Charset.forName(charSetName));
        }

        /**
         * build a Base64Helper instance.
         *
         * @return a Base64Helper instance.
         */
        public Base64Helper build() {
            Base64Helper helper = new Base64Helper();
            helper.builder = this;

            return helper;
        }
    }
}
