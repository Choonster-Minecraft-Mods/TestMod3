package choonster.testmod3.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Copy of {@link RegistryObject} for non-Forge registries.
 *
 * @author Choonster
 */
public class VanillaRegistryObject<T> implements Supplier<T> {
	private final ResourceLocation name;
	private final ResourceKey<? super T> key;

	@Nullable
	private T value;

	public static <T, U extends T> VanillaRegistryObject<U> of(final ResourceLocation name, final Registry<T> registry) {
		return new VanillaRegistryObject<>(name, registry);
	}

	private static final VanillaRegistryObject<?> EMPTY = new VanillaRegistryObject<>();

	private static <T> VanillaRegistryObject<T> empty() {
		@SuppressWarnings("unchecked")
		final VanillaRegistryObject<T> t = (VanillaRegistryObject<T>) EMPTY;
		return t;
	}

	private VanillaRegistryObject() {
		name = null;
		key = null;
	}

	private VanillaRegistryObject(final ResourceLocation name, final Registry<? super T> registry) {
		this.name = name;
		key = ResourceKey.create(registry.key(), name);

		updateReference(registry);
	}

	/**
	 * Directly retrieves the wrapped Registry Object. This value will automatically be updated when the backing registry is updated.
	 * Will throw NPE if the value is null, use isPresent to check first. Or use any of the other guarded functions.
	 */
	@Override
	@Nonnull
	public T get() {
		final T ret = value;
		Objects.requireNonNull(ret, () -> "Registry Object not present: " + name);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public void updateReference(final Registry<? super T> registry) {
		value = (T) registry.get(getId());
	}

	public ResourceLocation getId() {
		return name;
	}

	public ResourceKey<? super T> getKey() {
		return key;
	}

	public Stream<T> stream() {
		return isPresent() ? Stream.of(get()) : Stream.of();
	}

	/**
	 * Return {@code true} if there is a mod object present, otherwise {@code false}.
	 *
	 * @return {@code true} if there is a mod object present, otherwise {@code false}
	 */
	public boolean isPresent() {
		return value != null;
	}

	/**
	 * If a mod object is present, invoke the specified consumer with the object,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if a mod object is present
	 * @throws NullPointerException if mod object is present and {@code consumer} is
	 *                              null
	 */
	public void ifPresent(final Consumer<? super T> consumer) {
		if (isPresent()) {
			consumer.accept(get());
		}
	}

	/**
	 * If a mod object is present, and the mod object matches the given predicate,
	 * return an {@code RegistryObject} describing the value, otherwise return an
	 * empty {@code RegistryObject}.
	 *
	 * @param predicate a predicate to apply to the mod object, if present
	 * @return an {@code RegistryObject} describing the value of this {@code RegistryObject}
	 * if a mod object is present and the mod object matches the given predicate,
	 * otherwise an empty {@code RegistryObject}
	 * @throws NullPointerException if the predicate is null
	 */
	public VanillaRegistryObject<T> filter(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (!isPresent()) {
			return this;
		} else {
			return predicate.test(get()) ? this : empty();
		}
	}

	/**
	 * If a mod object is present, apply the provided mapping function to it,
	 * and if the result is non-null, return an {@code Optional} describing the
	 * result.  Otherwise return an empty {@code Optional}.
	 *
	 * @param <U>    The type of the result of the mapping function
	 * @param mapper a mapping function to apply to the mod object, if present
	 * @return an {@code Optional} describing the result of applying a mapping
	 * function to the mod object of this {@code RegistryObject}, if a mod object is present,
	 * otherwise an empty {@code Optional}
	 * @throws NullPointerException if the mapping function is null
	 * @apiNote This method supports post-processing on optional values, without
	 * the need to explicitly check for a return status.
	 */
	public <U> Optional<U> map(final Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(mapper.apply(get()));
		}
	}

	/**
	 * If a value is present, apply the provided {@code Optional}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@code Optional}.  This method is similar to {@link #map(Function)},
	 * but the provided mapper is one whose result is already an {@code Optional},
	 * and if invoked, {@code flatMap} does not wrap it with an additional
	 * {@code Optional}.
	 *
	 * @param <U>    The type parameter to the {@code Optional} returned by
	 * @param mapper a mapping function to apply to the mod object, if present
	 *               the mapping function
	 * @return the result of applying an {@code Optional}-bearing mapping
	 * function to the value of this {@code Optional}, if a value is present,
	 * otherwise an empty {@code Optional}
	 * @throws NullPointerException if the mapping function is null or returns
	 *                              a null result
	 */
	public <U> Optional<U> flatMap(final Function<? super T, Optional<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent()) {
			return Optional.empty();
		} else {
			return Objects.requireNonNull(mapper.apply(get()));
		}
	}

	/**
	 * If a mod object is present, lazily apply the provided mapping function to it,
	 * returning a supplier for the transformed result. If this object is empty, or the
	 * mapping function returns {@code null}, the supplier will return {@code null}.
	 *
	 * @param <U>    The type of the result of the mapping function
	 * @param mapper A mapping function to apply to the mod object, if present
	 * @return A {@code Supplier} lazily providing the result of applying a mapping
	 * function to the mod object of this {@code RegistryObject}, if a mod object is present,
	 * otherwise a supplier returning {@code null}
	 * @throws NullPointerException if the mapping function is {@code null}
	 * @apiNote This method supports post-processing on optional values, without
	 * the need to explicitly check for a return status.
	 */
	public <U> Supplier<U> lazyMap(final Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		return () -> isPresent() ? mapper.apply(get()) : null;
	}

	/**
	 * Return the mod object if present, otherwise return {@code other}.
	 *
	 * @param other the mod object to be returned if there is no mod object present, may
	 *              be null
	 * @return the mod object, if present, otherwise {@code other}
	 */
	public T orElse(final T other) {
		return isPresent() ? get() : other;
	}

	/**
	 * Return the mod object if present, otherwise invoke {@code other} and return
	 * the result of that invocation.
	 *
	 * @param other a {@code Supplier} whose result is returned if no mod object
	 *              is present
	 * @return the mod object if present otherwise the result of {@code other.get()}
	 * @throws NullPointerException if mod object is not present and {@code other} is
	 *                              null
	 */
	public T orElseGet(final Supplier<? extends T> other) {
		return isPresent() ? get() : other.get();
	}

	/**
	 * Return the contained mod object, if present, otherwise throw an exception
	 * to be created by the provided supplier.
	 *
	 * @param <X>               Type of the exception to be thrown
	 * @param exceptionSupplier The supplier which will return the exception to
	 *                          be thrown
	 * @return the present mod object
	 * @throws X                    if there is no mod object present
	 * @throws NullPointerException if no mod object is present and
	 *                              {@code exceptionSupplier} is null
	 * @apiNote A method reference to the exception constructor with an empty
	 * argument list can be used as the supplier. For example,
	 * {@code IllegalStateException::new}
	 */
	public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
		if (isPresent()) {
			return get();
		} else {
			throw exceptionSupplier.get();
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof VanillaRegistryObject) {
			return Objects.equals(((VanillaRegistryObject<?>) obj).name, name);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
}
