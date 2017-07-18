package com.illposed.osc;

/**
 * OSCBundleリスナ
 */
public interface OSCBundleListener {

	/**
	 * OSCBundleを受信
	 * 
	 * @param bundle
	 *            OSCBundle
	 */
	void acceptBundle(OSCBundle bundle);
}
