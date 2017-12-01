package com.sqrt26

import java.io.File
import java.io.PrintWriter

import org.rogach.scallop._

object WishUponAWm extends App {

  val name = "wish-upon-a-wm"
  val defaultNumberOfCpus = 1
  val oughtToBeEnough = 524288

  object Graphics extends Enumeration {
    type Graphics = Value
    val SDL, VNC, None = Value
  }

  def writeToFile(content: String, fileName: String) = {
    val outputFile = new PrintWriter(new File(fileName))
    outputFile.write(content)
    outputFile.close()
  }

  def getGraphics(graphicsType: Graphics.Value) = {
    graphicsType match {
      case Graphics.SDL =>
        <graphics type="sdl" display="0.0" />
      case Graphics.VNC =>
        <graphics type="vnc" autoport="yes" />
      case Graphics.None =>
        ""
    }
  }

  def getDomainXML(domainType: String = "kvm", osType: String = "hvm",
    domainName: String, cpus: Integer, memorySize: Integer,
    diskFile: String, graphics: String) = {
    val graphicsSelection = Graphics.withName(graphics)
<domain type={ domainType }>
  <name>{ domainName }</name>
  <os>
    <type>{ osType }</type>
  </os>
  <vcpu >{ cpus }</vcpu>
  <memory>{ memorySize }</memory>
  <devices>
    <disk type='file'>
      <source file={ diskFile }>
      </source>
      <target dev='hda'/>
    </disk>
    <interface type='network'>
      <source network='default'/>
    </interface>
    { getGraphics(graphicsSelection) }
  </devices>
</domain>
  }

  def getNetworkXML(networkName: String) = {
    <network>
      <name>{ networkName }</name>
    </network>
  }

  def buildDomainXML(config: Conf) = {
    val networkXML = getNetworkXML("default")
    writeToFile(networkXML.mkString, "network.xml")

    val domainXML = getDomainXML(domainName=config.name(),
      cpus=config.cpus(), memorySize=config.memory(),
      diskFile="disk.img", graphics="None")

    writeToFile(domainXML.mkString, "domain.xml")
    println(domainXML)
  }

  def printUsage = println(s"usage: $name <domain-name>")

  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
    val name = opt[String](required=true)
    val cpus = opt[Int](default=Some(defaultNumberOfCpus))
    val memory = opt[Int](default=Some(oughtToBeEnough))
    verify()
  }

  def main = {
    val conf = new Conf(args)
    buildDomainXML(conf)
  }

  main
}
