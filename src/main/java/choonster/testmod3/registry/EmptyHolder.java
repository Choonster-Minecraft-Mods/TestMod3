package choonster.testmod3.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EmptyHolder<T> implements Holder<T> {
	@Override
	public boolean isBound() {
		return false;
	}

	@Override
	public boolean is(final ResourceLocation p_205727_) {
		return false;
	}

	@Override
	public boolean is(final ResourceKey<T> p_205725_) {
		return false;
	}

	@Override
	public boolean is(final TagKey<T> p_205719_) {
		return false;
	}

	@Override
	public boolean is(final Predicate<ResourceKey<T>> p_205723_) {
		return false;
	}

	@Override
	public Either<ResourceKey<T>, T> unwrap() {
		throw emptyException();
	}

	@Override
	public Optional<ResourceKey<T>> unwrapKey() {
		throw emptyException();
	}

	@Override
	public Kind kind() {
		return Kind.DIRECT;
	}

	@Override
	public String toString() {
		return "EmptyHolder";
	}

	@Override
	public boolean isValidInRegistry(final Registry<T> p_205721_) {
		return false;
	}

	@Override
	public Stream<TagKey<T>> tags() {
		return Stream.of();
	}

	@Override
	public T value() {
		throw emptyException();
	}

	private IllegalStateException emptyException() {
		return new IllegalStateException("Trying to access empty holder");
	}
}
