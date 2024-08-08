# Skyblock Networth Calculator for Java
A Skyblock networth calculator library for Java that's fast and accurate. The calculations this library performs are based upon the ones seen in SkyHelper's networth calculator which offers the best.

## Getting Started
First, add the following Maven repository to your `build.gradle` file.

```groovy
repositories {
	exclusiveContent {
		forRepository {
			maven { url "https://maven.azureaaron.net/releases" }
		}

		filter {
			includeGroup "net.azureaaron"
		}
	}
}
```

Second, add the library as a dependency.

```groovy
dependencies {
	implementation("net.azureaaron:networth-calculator:<insert latest version>")
}
```

## Usage
This library was made for use in Fabric Mods, however due to the library's DFU-based architecture there is no dependency on one singular NBT parsing library meaning this it's possible to use this anywhere; the only catch is that you will need a `DynamicOps` instance suited to how your NBT parser works, Minecraft provides its own `NbtOps` instance which can be used in mods.

## Requirements
As of Minecraft 1.21, the game ships with all of these dependencies so if you're using this in a mod then this section can be ignored.

- Java 21
- DataFixerUpper 8.0.16+
- FastUtil 8.5.12+
- GSON 2.10.1+
- Guava 32.1.2+ (required by DFU)

## Special Thanks
- SkyHelper Team: For creating their networth calculator which was referenced for the library's calculations.