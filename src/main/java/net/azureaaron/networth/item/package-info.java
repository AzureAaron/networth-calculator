/**
 * In order to get an instance of any record class in this package, use its {@link com.mojang.serialization.Codec Codec} to serialize
 * the custom data to the object via a {@link com.mojang.serialization.DynamicOps DynamicOps} instance.<br><br>
 * 
 * Instantiating the records directly is advised against as breaking changes to their signatures may be made each update, the Codec however
 * is stable and will always pull in the necessary fields.
 */
package net.azureaaron.networth.item;