package com.sqrt26

import java.io.File
import java.io.PrintWriter

object Hello extends App {

  val name = "wish-upon-a-wm"

  object Graphics extends Enumeration {
    type Graphics = Value
    val SDL, VNC = Value
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
      case _ =>
        ""
    }
  }

  def getDomainXML(domainType: String = "kvm", osType: String = "hvm",
    domainName: String, memorySize: Integer, diskFile: String,
    graphics: String) = {
    val graphicsSelection = Graphics.withName(graphics)
<domain type={ domainType }>
  <name>{ domainName }</name>
  <os>
    <type>{ osType }</type>
  </os>
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

  def buildDomainXML(domainName: String) = {
    val networkXML = getNetworkXML("default")
    writeToFile(networkXML.mkString, "network.xml")

    val domainXML = getDomainXML(domainName="fool", memorySize=524288,
      diskFile="disk.img", graphics="VNC")

    writeToFile(domainXML.mkString, "domain.xml")
    println(domainXML)
  }

  def printUsage = println(s"usage: $name <domain-name>")

  if (args.size == 1) {
    val domainName = args.head
    buildDomainXML(domainName)
  } else {
    printUsage
  }

}
