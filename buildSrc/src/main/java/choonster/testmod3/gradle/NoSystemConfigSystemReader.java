package choonster.testmod3.gradle;

import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.SystemReader;

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
	public FileBasedConfig openSystemConfig(final Config parent, final FS fs) {
		// Based on the SystemReader.Default#openSystemConfig implementation
		return new FileBasedConfig(parent, null, fs) {
			@Override
			public void load() {
				// empty, do not load
			}

			@Override
			public boolean isOutdated() {
				// regular class would bomb here
				return false;
			}
		};
	}
}
