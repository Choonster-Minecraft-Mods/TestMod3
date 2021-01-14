package choonster.testmod3.init;

import choonster.testmod3.advancements.criterion.FluidContainerItemPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;

/**
 * Registers this mod's advancement criterion and predicates.
 *
 * @author Choonster
 */
public class ModCriterion {
	public static void register() {
		ItemPredicate.register(FluidContainerItemPredicate.TYPE, FluidContainerItemPredicate::deserialize);
	}
}
