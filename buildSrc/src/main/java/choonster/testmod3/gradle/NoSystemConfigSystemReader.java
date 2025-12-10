package choonster.testmod3.gradle;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.SystemReader;

import java.util.Objects;

/**
 * A {@link SystemReader} implementation that disables reading of system config.
 *
 * @author Choonster
 */
class NoSystemConfigSystemReader extends SystemReader.Delegate {
	public NoSystemConfigSystemReader(final SystemReader delegate) {
		super(delegate);
	}

	@Override
	public String getenv(final String variable) {
		if (Objects.equals(variable, Constants.GIT_CONFIG_NOSYSTEM_KEY)) {
			return "true";
		}

		return super.getenv(variable);
	}
}
