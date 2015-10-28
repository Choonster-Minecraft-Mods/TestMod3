package com.choonster.testmod3.scala.init

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * A Scala object that adds crafting recipes.
 */
object ModRecipes {

  /**
   * Add some crafting recipes.
   */
  def addRecipes(): Unit = {
    addRecipe(new ItemStack(Items.beef), "AAA", "AAA", "AAA", 'A', Items.rotten_flesh)
  }

  /**
   * Slightly hackish method to convert a sequence of [[Any]] to a sequence of [[AnyRef]] by replacing anything that's not an [[AnyRef]] with `null`.
   *
   * This allows a Scala method to accept [[Char]] values in an [[Any*]] argument and then pass that to a Java method's [[Object...]] argument.
   *
   * @param input The [[Any]] values
   * @return The [[AnyRef]] values
   */
  private def anyToAnyRef(input: Seq[Any]): Seq[AnyRef] = {
    input.map {
      case o: AnyRef => o
      case _ => null
    }
  }

  /**
   * A Scala wrapper of [[GameRegistry.addRecipe()]] that accepts [[Char]] values.
   * @param output The recipe's output
   * @param input The recipe's input
   */
  private def addRecipe(output: ItemStack, input: Any*): Unit = {
    GameRegistry.addRecipe(output, anyToAnyRef(input): _*)
  }
}
