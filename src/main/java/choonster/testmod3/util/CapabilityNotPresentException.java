package choonster.testmod3.util;

import net.minecraftforge.common.capabilities.Capability;

/**
 * Exception thrown when an expected {@link Capability} is not present.
 *
 * @author Choonster
 */
public class CapabilityNotPresentException extends RuntimeException {
	private static final String MESSAGE = "Required Capability not present";

	public CapabilityNotPresentException() {
		this(MESSAGE);
	}

	public CapabilityNotPresentException(final String message) {
		super(message);
	}

	public CapabilityNotPresentException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CapabilityNotPresentException(final Throwable cause) {
		this(MESSAGE, cause);
	}

	public CapabilityNotPresentException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
