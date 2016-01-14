package com.choonster.testmod3.scala

import com.choonster.testmod3.scala.init.ModRecipes
import com.choonster.testmod3.scala.proxy.IProxy
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{Mod, SidedProxy}

@Mod(modid = TestMod3Scala.MODID, version = TestMod3Scala.VERSION, modLanguage = "scala")
object TestMod3Scala {
  final val MODID = "testmod3scala"
  final val VERSION = "1.0"

  @SidedProxy(clientSide = "com.choonster.testmod3.scala.proxy.CombinedClientProxy", serverSide = "com.choonster.testmod3.scala.proxy.DedicatedServerProxy")
  var proxy: IProxy = null

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    proxy.preInit()
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    ModRecipes.addRecipes()
    proxy.init()
  }

  @EventHandler
  def postInit(event: FMLPostInitializationEvent): Unit = {
    proxy.postInit()
  }
}
