package choonster.testmod3.gradle;

import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class GitVersion {
	static Optional<String> getGitCommit(final File projectDir) {
		final var gitDir = new File(projectDir, ".git");

		if (!gitDir.exists()) {
			return Optional.empty();
		}

		final var builder = new FileRepositoryBuilder();
		builder.readEnvironment();

		builder.findGitDir(projectDir);

		if (builder.getGitDir() == null) {
			throw new IllegalStateException("No .git directory found!");
		}

		try (
				final var repository = builder.build();
				final var reader = repository.newObjectReader()
		) {
			final var head = repository.resolve("HEAD");

			if (head == null) {
				return Optional.empty();
			}

			final var abbreviated = reader.abbreviate(head);
			return Optional.of(abbreviated.name());
		} catch (final IOException e) {
			throw new IllegalStateException("Failed to get commit from Git repository", e);
		}
	}
}
